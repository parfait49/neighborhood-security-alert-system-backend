package rw.ac.security.service;
import rw.ac.security.domain.IncidentType;
import rw.ac.security.repository.IncidentTypeRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class IncidentTypeService {
 private final IncidentTypeRepository repo;
 public IncidentTypeService(IncidentTypeRepository repo){this.repo=repo;}
 public List<IncidentType> all(){return repo.findAll();}
 public Optional<IncidentType> byId(UUID id){return repo.findById(id);}
 public IncidentType save(IncidentType x){return repo.save(x);}
 public String update(UUID id, IncidentType x){return repo.findById(id).map(old->{old.setName(x.getName());repo.save(old);return "Incident type updated successfully";}).orElse("The incident type with that id does not exist");}
 public String delete(UUID id){return repo.findById(id).map(x->{repo.delete(x);return "Incident type deleted successfully";}).orElse("The incident type with that id does not exist");}
 public List<IncidentType> unused(){return repo.findUnusedIncidentTypes();}
}
