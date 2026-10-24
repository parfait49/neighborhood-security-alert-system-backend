package rw.ac.security.service;
import rw.ac.security.domain.*;
import rw.ac.security.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
@Service
public class IncidentReportService {
 private final IncidentReportRepository repo; private final SecurityGuardRepository guards; private final ResidentRepository residents; private final ResponseActionRepository actions;
 public IncidentReportService(IncidentReportRepository repo, SecurityGuardRepository guards, ResidentRepository residents, ResponseActionRepository actions){this.repo=repo;this.guards=guards;this.residents=residents;this.actions=actions;}
 public List<IncidentReport> all(){return repo.findAll();}
 public Optional<IncidentReport> byId(UUID id){return repo.findById(id);}
 public String save(IncidentReport x){
   if(x.getReportDate()==null || x.getReportDate().isBefore(LocalDate.now())) return "Incident report date cannot be in the past";
   if(x.getSecurityGuard()!=null && repo.existsBySecurityGuard_IdAndReportDateAndStatusNot(x.getSecurityGuard().getId(), x.getReportDate(), ReportStatus.DISMISSED)) return "Security guard is already dispatched on that date";
   x.setStatus(ReportStatus.REPORTED); repo.save(x); return "Incident report saved successfully";
 }
 public String update(UUID id, IncidentReport x){return repo.findById(id).map(old->{old.setReportDate(x.getReportDate());old.setDescription(x.getDescription());old.setLocation(x.getLocation());old.setResident(x.getResident());old.setSecurityGuard(x.getSecurityGuard());repo.save(old);return "Incident report updated successfully";}).orElse("The incident report with that id does not exist");}
 public String delete(UUID id){
   Optional<IncidentReport> oi=repo.findById(id); if(oi.isEmpty()) return "The incident report with that id does not exist";
   if(actions.existsByIncidentReport_Id(id)) return "That incident report has response actions, delete them first";
   repo.delete(oi.get()); return "Incident report deleted successfully";
 }
 public List<IncidentReport> byStatus(ReportStatus status){return repo.findByStatusOrderByReportDateAsc(status);}
 public List<IncidentReport> between(LocalDate start, LocalDate end){return repo.findByReportDateBetweenOrderByReportDateAsc(start,end);}
 public String changeStatus(UUID id, ReportStatus next){
   Optional<IncidentReport> oi=repo.findById(id); if(oi.isEmpty()) return "The incident report with that id does not exist";
   IncidentReport i=oi.get(); ReportStatus current=i.getStatus();
   boolean allowed=(current==ReportStatus.REPORTED && (next==ReportStatus.DISPATCHED || next==ReportStatus.DISMISSED)) ||
     (current==ReportStatus.DISPATCHED && (next==ReportStatus.RESOLVED || next==ReportStatus.DISMISSED));
   if(!allowed) return "Cannot change status from "+current+" to "+next;
   i.setStatus(next); repo.save(i); return "Incident report status updated successfully";
 }
 public List<Object[]> stats(){return repo.countReportsByStatus();}
 @Transactional public String cancelDay(UUID guardId, LocalDate date){int n=repo.dismissReportsForGuardOnDate(guardId,date); return n+" incident reports cancelled";}
 public Page<IncidentReport> page(int page,int size,String sort){return repo.findAll(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, sort)));}
 public long countToday(){return repo.countByReportDate(LocalDate.now());}
}
