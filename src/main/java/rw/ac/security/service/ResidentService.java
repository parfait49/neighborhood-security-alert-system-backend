package rw.ac.security.service;
import rw.ac.security.domain.Resident;
import rw.ac.security.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class ResidentService {
 private final ResidentRepository repo;
 public ResidentService(ResidentRepository repo){this.repo=repo;}
 public List<Resident> all(){return repo.findAll();}
 public Optional<Resident> byId(UUID id){return repo.findById(id);}
 public Resident save(Resident x){return repo.save(x);}
 public String update(UUID id, Resident x){return repo.findById(id).map(old->{old.setFirstName(x.getFirstName());old.setLastName(x.getLastName());old.setNationalId(x.getNationalId());old.setPhone(x.getPhone());old.setStreet(x.getStreet());repo.save(old);return "Resident updated successfully";}).orElse("The resident with that id does not exist");}
 public String delete(UUID id){return repo.findById(id).map(x->{repo.delete(x);return "Resident deleted successfully";}).orElse("The resident with that id does not exist");}
 public List<Resident> byStreet(String street){return repo.findByStreetIgnoreCaseOrderByLastNameAsc(street);}
 public List<Resident> ofGuard(UUID id){return repo.findResidentsOfSecurityGuard(id);}
}
