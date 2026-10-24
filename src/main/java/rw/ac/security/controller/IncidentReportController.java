package rw.ac.security.controller;
import rw.ac.security.domain.*; import rw.ac.security.service.IncidentReportService;
import org.springframework.data.domain.Page; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/incident-reports")
public class IncidentReportController {
 private final IncidentReportService s; public IncidentReportController(IncidentReportService s){this.s=s;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestBody IncidentReport x){String m=s.save(x); if(m.equals("Incident report date cannot be in the past"))return ResponseEntity.badRequest().body(m); if(m.equals("Security guard is already dispatched on that date"))return ResponseEntity.status(409).body(m); return ResponseEntity.status(201).body(m);}
 @GetMapping("/all") public List<IncidentReport> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The incident report with that id does not exist"));}
 @PutMapping("/update/{id}") public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody IncidentReport x){String m=s.update(id,x);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id); if(m.contains("does not exist"))return ResponseEntity.status(404).body(m); if(m.contains("response actions"))return ResponseEntity.status(409).body(m); return ResponseEntity.ok(m);}
 @GetMapping("/by-status") public List<IncidentReport> byStatus(@RequestParam ReportStatus status){return s.byStatus(status);}
 @GetMapping("/between") public List<IncidentReport> between(@RequestParam String start,@RequestParam String end){return s.between(LocalDate.parse(start),LocalDate.parse(end));}
 @PatchMapping("/{id}/status") public ResponseEntity<?> status(@PathVariable UUID id,@RequestParam ReportStatus status){String m=s.changeStatus(id,status); if(m.contains("does not exist"))return ResponseEntity.status(404).body(m); if(m.startsWith("Cannot change"))return ResponseEntity.badRequest().body(m); return ResponseEntity.ok(m);}
 @GetMapping("/stats/by-status") public List<Object[]> stats(){return s.stats();}
 @PatchMapping("/cancel-day") public String cancelDay(@RequestParam UUID securityGuardId,@RequestParam String date){return s.cancelDay(securityGuardId,LocalDate.parse(date));}
 @GetMapping("/page") public Page<IncidentReport> page(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size,@RequestParam(defaultValue="reportDate") String sort){return s.page(page,size,sort);}
}
