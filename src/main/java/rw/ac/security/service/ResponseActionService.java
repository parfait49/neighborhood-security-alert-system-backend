package rw.ac.security.service;
import rw.ac.security.domain.*;
import rw.ac.security.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
@Service
public class ResponseActionService {
 private final ResponseActionRepository repo; private final IncidentReportRepository reports;
 public ResponseActionService(ResponseActionRepository repo, IncidentReportRepository reports){this.repo=repo;this.reports=reports;}
 public List<ResponseAction> all(){return repo.findAll();}
 public Optional<ResponseAction> byId(UUID id){return repo.findById(id);}
 public String save(UUID reportId, ResponseAction action){
   Optional<IncidentReport> oi=reports.findById(reportId);
   if(oi.isEmpty()) return "The incident report with that id does not exist";
   if(oi.get().getStatus()!=ReportStatus.RESOLVED) return "Response actions are only for RESOLVED incident reports";
   action.setActionDate(LocalDate.now()); action.setIncidentReport(oi.get()); repo.save(action);
   return "Response action saved successfully";
 }
 public String delete(UUID id){return repo.findById(id).map(a->{repo.delete(a);return "Response action deleted successfully";}).orElse("The response action with that id does not exist");}
 public List<ResponseAction> residentHistory(String nationalId){return repo.findResidentActionHistory(nationalId);}
}
