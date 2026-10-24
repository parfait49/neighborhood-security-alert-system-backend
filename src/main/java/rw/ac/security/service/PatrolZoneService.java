package rw.ac.security.service;
import rw.ac.security.domain.PatrolZone;
import rw.ac.security.repository.PatrolZoneRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class PatrolZoneService {
 private final PatrolZoneRepository repo;
 public PatrolZoneService(PatrolZoneRepository repo){this.repo=repo;}
 public List<PatrolZone> all(){return repo.findAll();}
 public Optional<PatrolZone> byId(UUID id){return repo.findById(id);}
 public PatrolZone save(PatrolZone x){return repo.save(x);}
 public String update(UUID id, PatrolZone x){return repo.findById(id).map(old->{old.setZoneNumber(x.getZoneNumber());old.setName(x.getName());repo.save(old);return "Patrol zone updated successfully";}).orElse("The patrol zone with that id does not exist");}
 public String delete(UUID id){return repo.findById(id).map(x->{repo.delete(x);return "Patrol zone deleted successfully";}).orElse("The patrol zone with that id does not exist");}
 public List<PatrolZone> free(){return repo.findFreePatrolZones();}
}
