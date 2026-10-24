package rw.ac.security.controller;
import rw.ac.security.domain.IncidentType; import rw.ac.security.service.IncidentTypeService;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/incident-types")
public class IncidentTypeController {
 private final IncidentTypeService s; public IncidentTypeController(IncidentTypeService s){this.s=s;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestBody IncidentType x){s.save(x);return ResponseEntity.status(201).body("Incident type saved successfully");}
 @GetMapping("/all") public List<IncidentType> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The incident type with that id does not exist"));}
 @PutMapping("/update/{id}") public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody IncidentType x){String m=s.update(id,x);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @GetMapping("/unused") public List<IncidentType> unused(){return s.unused();}
}
