const API = {
  dashboard: '/api/dashboard/summary',
  incidents: '/api/incident-reports',
  residents: '/api/residents',
  guards: '/api/security-guards',
  zones: '/api/patrol-zones',
  types: '/api/incident-types',
  actions: '/api/response-actions'
};
const config = {
  incidents: {label:'Incident reports', plural:'Incident reports', endpoint:API.incidents+'/all', fields:[['description','Description'],['location','Location'],['reportDate','Date'],['status','Status'],['resident','Resident'],['securityGuard','Assigned guard']], create:true},
  residents: {label:'Residents', plural:'Residents', endpoint:API.residents+'/all', fields:[['firstName','First name'],['lastName','Last name'],['nationalId','National ID'],['phone','Phone'],['street','Street']], create:true},
  guards: {label:'Security guards', plural:'Security guards', endpoint:API.guards+'/all', fields:[['firstName','First name'],['lastName','Last name'],['phone','Phone'],['patrolZone','Patrol zone'],['incidentTypes','Incident types']], create:true},
  zones: {label:'Patrol zones', plural:'Patrol zones', endpoint:API.zones+'/all', fields:[['zoneNumber','Zone #'],['name','Zone name']], create:true},
  types: {label:'Incident types', plural:'Incident types', endpoint:API.types+'/all', fields:[['name','Type name']], create:true},
  actions: {label:'Response actions', plural:'Response actions', endpoint:API.actions+'/all', fields:[['actionName','Action'],['notes','Notes'],['minutesTaken','Minutes taken'],['actionDate','Action date'],['incidentReport','Incident report']], create:false}
};
let currentView='dashboard', records={}, searchTerm='', deleteTarget=null, toastTimer=null;
const $=s=>document.querySelector(s), $$=s=>[...document.querySelectorAll(s)];
const titleCase=s=>s.replace(/([A-Z])/g,' $1').replace(/^./,c=>c.toUpperCase());
const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const getId=x=>x&&typeof x==='object'?x.id:x;
const displayValue=(v,key)=>{
  if(v==null||v==='') return '—';
  if(typeof v==='object'){
    if(Array.isArray(v)) return v.map(x=>x.name||[x.firstName,x.lastName].filter(Boolean).join(' ')||x.actionName||x.id).join(', ')||'—';
    if(key==='resident'||key==='securityGuard') return [v.firstName,v.lastName].filter(Boolean).join(' ')||v.id||'—';
    if(key==='patrolZone') return v.zoneNumber!=null?`Zone ${v.zoneNumber}${v.name?' · '+v.name:''}`:v.name||'—';
    if(key==='incidentReport') return v.description||v.id||'—';
    return v.name||v.id||JSON.stringify(v);
  }
  return String(v);
};
async function api(url,options={}){
  const response=await fetch(url,{...options,headers:{'Content-Type':'application/json',...(options.headers||{})}});
  const raw=await response.text(); let body=raw;
  try{body=raw?JSON.parse(raw):null}catch{}
  if(!response.ok) throw new Error(typeof body==='string'?body:(body?.message||body?.error||`Request failed (${response.status})`));
  return body;
}
function notify(message,type='success'){
  const el=$('#toast');el.textContent=message;el.className=`toast show ${type}`;
  clearTimeout(toastTimer);toastTimer=setTimeout(()=>el.className='toast',5000);
}
function initials(obj){return `${(obj.firstName||'?')[0]}${(obj.lastName||'')[0]||''}`.toUpperCase();}
function statusPill(status){const s=String(status||'').toUpperCase();return `<span class="status-pill status-${esc(s)}">${esc(s||'Unknown')}</span>`;}
function setupNav(){
  $$('.nav-link').forEach(b=>b.addEventListener('click',()=>navigate(b.dataset.view)));
  $$('[data-goto]').forEach(b=>b.addEventListener('click',()=>navigate(b.dataset.goto)));
  $('#mobileMenu').addEventListener('click',()=>$('#sidebar').classList.toggle('open'));
  $('#refreshBtn').addEventListener('click',refreshCurrent);
  $('#tableRefresh').addEventListener('click',refreshCurrent);
  $('#searchInput').addEventListener('input',e=>{searchTerm=e.target.value.toLowerCase();renderTable();});
  $('#quickReportBtn').addEventListener('click',()=>{navigate('incidents');openForm('incidents');});
  $('#addBtn').addEventListener('click',()=>openForm(currentView));
  $('#closeModal').addEventListener('click',closeForm);$('#cancelModal').addEventListener('click',closeForm);
  $('#modalBackdrop').addEventListener('click',e=>{if(e.target.id==='modalBackdrop')closeForm();});
  $('#recordForm').addEventListener('submit',submitForm);
  $('#cancelDelete').addEventListener('click',()=>$('#confirmBackdrop').classList.add('hidden'));
  $('#confirmDelete').addEventListener('click',performDelete);
}
function navigate(view){
  currentView=view;searchTerm='';
  $$('.nav-link').forEach(b=>b.classList.toggle('active',b.dataset.view===view));
  $('#sidebar').classList.remove('open');$('#searchInput').value='';
  const dashboard=view==='dashboard';
  $('#dashboardView').classList.toggle('hidden',!dashboard);$('#dataView').classList.toggle('hidden',dashboard);
  const names={dashboard:['Overview','COMMUNITY SAFETY AT A GLANCE','Good day, team.','Monitor reports, coordinate patrols, and help keep your neighborhood safe.'],incidents:['Incident reports','INCIDENT MANAGEMENT','Incident reports','Review incoming reports and coordinate the response.'],residents:['Residents','COMMUNITY DIRECTORY','Residents','Maintain resident contact and neighborhood information.'],guards:['Security guards','RESPONSE TEAM','Security guards','Review the team and their current patrol assignments.'],zones:['Patrol zones','PATROL COVERAGE','Patrol zones','Manage the areas covered by neighborhood patrols.'],types:['Incident types','REPORT CATEGORIES','Incident types','Maintain the incident categories used by the team.'],actions:['Response actions','RESPONSE HISTORY','Response actions','Review actions recorded for resolved incidents.']};
  const n=names[view]||names.dashboard;$('#crumb').textContent=n[0];$('#eyebrow').textContent=n[1];$('#pageTitle').textContent=n[2];$('#pageSubtitle').textContent=n[3];
  if(!dashboard){const c=config[view];$('#tableTitle').textContent=c.plural;$('#tableSubtitle').textContent=`${c.plural} stored in your PostgreSQL database`;$('#addBtn').classList.toggle('hidden',!c.create);$('#addBtn').textContent=view==='incidents'?'＋ Report incident':'＋ Add '+(view==='types'?'type':view==='zones'?'zone':view==='guards'?'guard':view==='residents'?'resident':'record');}
  refreshCurrent();
}
async function refreshCurrent(){
  if(currentView==='dashboard'){await loadDashboard();return;}
  try{records[currentView]=await api(config[currentView].endpoint);renderTable();setConnection(true);}
  catch(e){setConnection(false);$('#dataTable').innerHTML=`<div class="empty-state">Could not load records. ${esc(e.message)}<br><br>Check that Spring Boot is running on port 8080.</div>`;notify(e.message,'error');}
}
function setConnection(ok){$('#connectionText').textContent=ok?'API connected':'API connection issue';$('.pulse').classList.toggle('online',ok);}
async function loadDashboard(){
  try{
    const [summary,incidents]=await Promise.all([api(API.dashboard),api(API.incidents+'/all')]);
    records.incidents=incidents;setConnection(true);renderStats(summary);renderRecent(incidents);renderBreakdown(summary);
    $('#incidentNavCount').textContent=incidents.length;
  }catch(e){setConnection(false);$('#statsGrid').innerHTML=`<div class="panel" style="padding:20px;grid-column:1/-1">Unable to load dashboard: ${esc(e.message)}</div>`;notify('Dashboard could not refresh: '+e.message,'error');}
}
function renderStats(s){
 const items=[
  ['Registered residents',s.residents??0,'Community directory','♙','blue'],
  ['Security guards',s.securityGuards??0,'Response team','⬟','green'],
  ['Free patrol zones',s.freePatrolZones??0,'Available for assignment','⌖','purple'],
  ['Reports today',s.reportsToday??0,'Incidents logged today','⚑','orange']
 ];
 $('#statsGrid').innerHTML=items.map(([label,value,caption,icon,color])=>`<article class="stat-card"><div class="stat-top"><span>${label}</span><span class="stat-icon ${color}">${icon}</span></div><div class="stat-value">${esc(value)}</div><div class="stat-caption">${caption}</div></article>`).join('');
}
function renderBreakdown(s){
 const values=[['Reported',s.reported??0,'reported'],['Dispatched',s.dispatched??0,'dispatched'],['Resolved',s.resolved??0,'resolved'],['Dismissed',s.dismissed??0,'dismissed']];
 const max=Math.max(1,...values.map(v=>Number(v[1])||0));
 $('#statusBreakdown').innerHTML=values.map(([label,value,cls])=>`<div class="status-row"><span>${label}</span><div class="bar ${cls}"><span style="width:${Math.max(value?5:0,(Number(value)||0)/max*100)}%"></span></div><strong>${esc(value)}</strong></div>`).join('');
}
function renderRecent(items){
 const sorted=[...items].sort((a,b)=>String(b.reportDate||'').localeCompare(String(a.reportDate||''))).slice(0,5);
 if(!sorted.length){$('#recentIncidents').innerHTML='<div class="empty-state">No incident reports yet. Use “Report an incident” to add one.</div>';return;}
 $('#recentIncidents').innerHTML=`<table class="table"><thead><tr><th>Incident</th><th>Location</th><th>Date</th><th>Status</th></tr></thead><tbody>${sorted.map(x=>`<tr><td class="primary-cell">${esc(x.description||'Incident report')}</td><td>${esc(x.location||'—')}</td><td>${esc(x.reportDate||'—')}</td><td>${statusPill(x.status)}</td></tr>`).join('')}</tbody></table>`;
}
function renderTable(){
 const c=config[currentView];if(!c)return;const all=records[currentView]||[];
 const filtered=all.filter(r=>JSON.stringify(r).toLowerCase().includes(searchTerm));
 if(!filtered.length){$('#dataTable').innerHTML=`<div class="empty-state">${all.length?'No records match your search.':'No records found.'}</div>`;$('#recordCount').textContent=`0 of ${all.length} records`;return;}
 let columns=c.fields;
 $('#dataTable').innerHTML=`<table class="table"><thead><tr>${columns.map(([,label])=>`<th>${esc(label)}</th>`).join('')}<th>Actions</th></tr></thead><tbody>${filtered.map(r=>`<tr>${columns.map(([key])=>`<td>${key==='status'?statusPill(r[key]):key==='firstName'?`<div class="person-cell"><span class="mini-avatar">${esc(initials(r))}</span><span class="primary-cell">${esc([r.firstName,r.lastName].filter(Boolean).join(' '))}</span></div>`:esc(displayValue(r[key],key))}</td>`).join('')}<td><div class="row-actions">${currentView==='incidents'?statusAction(r):''}${currentView==='actions'?'':`<button class="mini-btn danger" data-delete="${esc(r.id)}" title="Delete record">Delete</button>`}</div></td></tr>`).join('')}</tbody></table>`;
 $('#recordCount').textContent=`${filtered.length} ${filtered.length===1?'record':'records'}${filtered.length!==all.length?' (filtered)':''}`;
 $$('[data-delete]').forEach(b=>b.addEventListener('click',()=>askDelete(currentView,b.dataset.delete)));
 $$('[data-status]').forEach(s=>s.addEventListener('change',()=>changeStatus(s.dataset.status,s.value)));
}
function statusAction(r){
 const options=['REPORTED','DISPATCHED','RESOLVED','DISMISSED'];
 return `<select class="mini-btn" data-status="${esc(r.id)}" aria-label="Change status">${options.map(s=>`<option value="${s}" ${s===r.status?'selected':''}>${s}</option>`).join('')}</select>`;
}
async function changeStatus(id,status){
 try{const msg=await api(`${API.incidents}/${encodeURIComponent(id)}/status?status=${encodeURIComponent(status)}`,{method:'PATCH'});notify(typeof msg==='string'?msg:'Incident status updated');await refreshCurrent();}
 catch(e){notify(e.message,'error');await refreshCurrent();}
}
function field(name,label,type='text',opts={}){
 const required=opts.required===false?'':'required';const full=opts.full?' full':'';
 if(type==='select')return `<div class="field${full}"><label for="${name}">${label}</label><select id="${name}" name="${name}" ${required}>${opts.options.map(o=>`<option value="${esc(o.value)}">${esc(o.label)}</option>`).join('')}</select>${opts.hint?`<span class="hint">${opts.hint}</span>`:''}</div>`;
 if(type==='textarea')return `<div class="field${full}"><label for="${name}">${label}</label><textarea id="${name}" name="${name}" ${required} placeholder="${esc(opts.placeholder||'')}"></textarea></div>`;
 return `<div class="field${full}"><label for="${name}">${label}</label><input id="${name}" name="${name}" type="${type}" ${required} ${opts.min?`min="${opts.min}"`:''} placeholder="${esc(opts.placeholder||'')}"></div>`;
}
function selectOptions(items,labelFn){return items.map(x=>({value:x.id,label:labelFn(x)}));}
async function openForm(view){
 const c=config[view];if(!c||!c.create)return;
 const today=new Date().toLocaleDateString('en-CA');
 let html='';
 try{
  if(view==='residents')html=field('firstName','First name')+field('lastName','Last name')+field('nationalId','National ID')+field('phone','Phone number','tel')+field('street','Street / neighborhood','text',{full:true});
  if(view==='zones')html=field('zoneNumber','Zone number','number',{min:'1'})+field('name','Zone name');
  if(view==='types')html=field('name','Incident type name','text',{full:true});
  if(view==='guards'){
   const zones=await api(API.zones+'/all');const types=await api(API.types+'/all');
   html=field('firstName','First name')+field('lastName','Last name')+field('phone','Phone number','tel')+field('patrolZoneId','Patrol zone','select',{options:[{value:'',label:'No zone assigned'},...selectOptions(zones,z=>`Zone ${z.zoneNumber} · ${z.name}`)],required:false,full:true});
   html+=`<div class="field full"><label for="incidentTypeIds">Incident types</label><select id="incidentTypeIds" name="incidentTypeIds" multiple size="${Math.min(Math.max(types.length,2),5)}">${types.map(t=>`<option value="${esc(t.id)}">${esc(t.name)}</option>`).join('')}</select><span class="hint">Hold Ctrl (Windows) to select more than one type. Optional; you can assign types later.</span></div>`;
  }
  if(view==='incidents'){
   const residents=await api(API.residents+'/all');const guards=await api(API.guards+'/all');
   html=field('description','What happened?','textarea',{full:true,placeholder:'Briefly describe the incident'})+field('location','Incident location','text',{full:true})+field('reportDate','Report date','date',{min:today})+
   field('residentId','Reporting resident','select',{options:[{value:'',label:'Select resident'},...selectOptions(residents,r=>`${r.firstName} ${r.lastName} · ${r.nationalId}`)]})+
   field('securityGuardId','Assign security guard','select',{options:[{value:'',label:'Select guard'},...selectOptions(guards,g=>`${g.firstName} ${g.lastName}`)],required:false});
  }
  $('#modalTitle').textContent=view==='incidents'?'Report an incident':`Add ${view==='zones'?'patrol zone':view==='types'?'incident type':view==='guards'?'security guard':'resident'}`;
  $('#formFields').innerHTML=html;$('#modalBackdrop').classList.remove('hidden');
 }catch(e){notify('Could not prepare form: '+e.message,'error');}
}
function closeForm(){$('#modalBackdrop').classList.add('hidden');$('#recordForm').reset();}
async function submitForm(e){
 e.preventDefault();const view=currentView;const form=new FormData(e.target);const val=k=>String(form.get(k)||'').trim();let body={},url='',method='POST';
 try{
  if(view==='residents'){url=API.residents+'/save';body={firstName:val('firstName'),lastName:val('lastName'),nationalId:val('nationalId'),phone:val('phone'),street:val('street')};}
  if(view==='zones'){url=API.zones+'/save';body={zoneNumber:Number(val('zoneNumber')),name:val('name')};}
  if(view==='types'){url=API.types+'/save';body={name:val('name')};}
  if(view==='guards'){
   url=API.guards+'/save';body={firstName:val('firstName'),lastName:val('lastName'),phone:val('phone'),patrolZone:val('patrolZoneId')?{id:val('patrolZoneId')}:null,incidentTypes:[...$('#incidentTypeIds').selectedOptions].map(o=>({id:o.value}))};
  }
  if(view==='incidents'){
   url=API.incidents+'/save';body={description:val('description'),location:val('location'),reportDate:val('reportDate'),status:'REPORTED',resident:{id:val('residentId')},securityGuard:val('securityGuardId')?{id:val('securityGuardId')}:null};
  }
  $('#saveBtn').disabled=true;$('#saveBtn').textContent='Saving…';
  const result=await api(url,{method,body:JSON.stringify(body)});
  closeForm();notify(typeof result==='string'?result:`${config[view].label} saved successfully`);await refreshCurrent();
 }catch(err){notify(err.message,'error');}
 finally{$('#saveBtn').disabled=false;$('#saveBtn').textContent='Save record';}
}
function askDelete(view,id){deleteTarget={view,id};$('#confirmBackdrop').classList.remove('hidden');}
async function performDelete(){
 if(!deleteTarget)return;const {view,id}=deleteTarget;const endpoints={residents:API.residents,guards:API.guards,zones:API.zones,types:API.types,incidents:API.incidents,actions:API.actions};
 try{const msg=await api(`${endpoints[view]}/delete/${encodeURIComponent(id)}`,{method:'DELETE'});$('#confirmBackdrop').classList.add('hidden');deleteTarget=null;notify(typeof msg==='string'?msg:'Record deleted');await refreshCurrent();}
 catch(e){$('#confirmBackdrop').classList.add('hidden');notify(e.message,'error');}
}
document.addEventListener('DOMContentLoaded',()=>{
 setupNav();$('#todayLabel').textContent=new Intl.DateTimeFormat('en-GB',{weekday:'short',day:'2-digit',month:'short',year:'numeric'}).format(new Date());
 navigate('dashboard');
});
