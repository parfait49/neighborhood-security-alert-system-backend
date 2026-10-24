package rw.ac.security.controller;
import rw.ac.security.domain.Resident; import rw.ac.security.service.ResidentService; import rw.ac.security.repository.SecurityGuardRepository;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/residents")
public class ResidentController {
 private final ResidentService s; private final SecurityGuardRepository guards; public ResidentController(ResidentService s, SecurityGuardRepository guards){this.s=s;this.guards=guards;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestBody Resident x){s.save(x);return ResponseEntity.status(201).body("Resident saved successfully");}
 @GetMapping("/all") public List<Resident> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The resident with that id does not exist"));}
 @PutMapping("/update/{id}") public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody Resident x){String m=s.update(id,x);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @GetMapping("/by-street") public List<Resident> byStreet(@RequestParam String street){return s.byStreet(street);}
 @GetMapping("/of-security-guard/{securityGuardId}") public ResponseEntity<?> ofGuard(@PathVariable UUID securityGuardId){if(!guards.existsById(securityGuardId))return ResponseEntity.status(404).body("The security guard with that id does not exist"); return ResponseEntity.ok(s.ofGuard(securityGuardId));}
}
