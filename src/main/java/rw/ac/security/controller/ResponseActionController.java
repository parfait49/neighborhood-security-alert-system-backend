package rw.ac.security.controller;
import rw.ac.security.domain.ResponseAction; import rw.ac.security.service.ResponseActionService;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/response-actions")
public class ResponseActionController {
 private final ResponseActionService s; public ResponseActionController(ResponseActionService s){this.s=s;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestParam UUID incidentReportId,@RequestBody ResponseAction x){String m=s.save(incidentReportId,x); if(m.contains("does not exist"))return ResponseEntity.status(404).body(m); if(m.startsWith("Response actions"))return ResponseEntity.badRequest().body(m); return ResponseEntity.status(201).body(m);}
 @GetMapping("/all") public List<ResponseAction> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The response action with that id does not exist"));}
 @GetMapping("/resident/{nationalId}") public List<ResponseAction> residentHistory(@PathVariable String nationalId){return s.residentHistory(nationalId);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
}
