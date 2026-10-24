package rw.ac.security.controller;
import rw.ac.security.domain.PatrolZone; import rw.ac.security.service.PatrolZoneService;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/patrol-zones")
public class PatrolZoneController {
 private final PatrolZoneService s; public PatrolZoneController(PatrolZoneService s){this.s=s;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestBody PatrolZone x){s.save(x);return ResponseEntity.status(201).body("Patrol zone saved successfully");}
 @GetMapping("/all") public List<PatrolZone> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The patrol zone with that id does not exist"));}
 @PutMapping("/update/{id}") public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody PatrolZone x){String m=s.update(id,x);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @GetMapping("/free") public List<PatrolZone> free(){return s.free();}
}
