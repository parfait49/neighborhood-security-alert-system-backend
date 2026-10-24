package rw.ac.security.service;
import rw.ac.security.domain.*;
import rw.ac.security.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class SecurityGuardService {
 private final SecurityGuardRepository repo; private final PatrolZoneRepository zones; private final IncidentTypeRepository types;
 public SecurityGuardService(SecurityGuardRepository repo, PatrolZoneRepository zones, IncidentTypeRepository types){this.repo=repo;this.zones=zones;this.types=types;}
 public List<SecurityGuard> all(){return repo.findAll();}
 public Optional<SecurityGuard> byId(UUID id){return repo.findById(id);}
 public SecurityGuard save(SecurityGuard x){return repo.save(x);}
 public String update(UUID id, SecurityGuard x){return repo.findById(id).map(old->{old.setFirstName(x.getFirstName());old.setLastName(x.getLastName());old.setPhone(x.getPhone());old.setPatrolZone(x.getPatrolZone());old.setIncidentTypes(x.getIncidentTypes());repo.save(old);return "Security guard updated successfully";}).orElse("The security guard with that id does not exist");}
 public String delete(UUID id){return repo.findById(id).map(x->{repo.delete(x);return "Security guard deleted successfully";}).orElse("The security guard with that id does not exist");}
 public List<SecurityGuard> byIncidentType(String name){return repo.findByIncidentTypeNameIgnoreCase(name);}
 public String addIncidentType(UUID guardId, UUID typeId){
   Optional<SecurityGuard> og=repo.findById(guardId); if(og.isEmpty()) return "The security guard with that id does not exist";
   Optional<IncidentType> ot=types.findById(typeId); if(ot.isEmpty()) return "That incident type does not exist";
   SecurityGuard g=og.get(); IncidentType t=ot.get();
   if(g.getIncidentTypes().stream().anyMatch(v->v.getId().equals(typeId))) return "The security guard already has that incident type";
   g.getIncidentTypes().add(t); repo.save(g); return "Incident type added to the security guard";
 }
 public String moveZone(UUID guardId, int zoneNumber){
   Optional<SecurityGuard> og=repo.findById(guardId); if(og.isEmpty()) return "The security guard with that id does not exist";
   Optional<PatrolZone> oz=zones.findByZoneNumber(zoneNumber); if(oz.isEmpty()) return "The patrol zone with that zoneNumber does not exist";
   PatrolZone z=oz.get();
   if(repo.existsByPatrolZone_Id(z.getId()) && (og.get().getPatrolZone()==null || !og.get().getPatrolZone().getId().equals(z.getId()))) return "That patrol zone is taken by another security guard";
   og.get().setPatrolZone(z); repo.save(og.get()); return "Patrol zone changed successfully";
 }
}
