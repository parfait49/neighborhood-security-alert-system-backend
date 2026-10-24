package rw.ac.security.controller;
import rw.ac.security.domain.SecurityGuard; import rw.ac.security.service.SecurityGuardService;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*; import java.util.UUID;
@RestController @RequestMapping("/api/security-guards")
public class SecurityGuardController {
 private final SecurityGuardService s; public SecurityGuardController(SecurityGuardService s){this.s=s;}
 @PostMapping("/save") public ResponseEntity<?> save(@RequestBody SecurityGuard x){s.save(x);return ResponseEntity.status(201).body("Security guard saved successfully");}
 @GetMapping("/all") public List<SecurityGuard> all(){return s.all();}
 @GetMapping("/{id}") public ResponseEntity<?> byId(@PathVariable UUID id){return s.byId(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body("The security guard with that id does not exist"));}
 @PutMapping("/update/{id}") public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody SecurityGuard x){String m=s.update(id,x);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @DeleteMapping("/delete/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){String m=s.delete(id);return m.contains("does not exist")?ResponseEntity.status(404).body(m):ResponseEntity.ok(m);}
 @GetMapping("/by-incident-type") public List<SecurityGuard> byIncidentType(@RequestParam String name){return s.byIncidentType(name);}
 @PutMapping("/{securityGuardId}/incident-types/{incidentTypeId}") public ResponseEntity<?> addType(@PathVariable UUID securityGuardId,@PathVariable UUID incidentTypeId){String m=s.addIncidentType(securityGuardId,incidentTypeId); if(m.contains("does not exist"))return ResponseEntity.status(404).body(m); if(m.contains("already has"))return ResponseEntity.status(409).body(m); return ResponseEntity.ok(m);}
 @PatchMapping("/{securityGuardId}/patrol-zone") public ResponseEntity<?> moveZone(@PathVariable UUID securityGuardId,@RequestParam int zoneNumber){String m=s.moveZone(securityGuardId,zoneNumber); if(m.contains("does not exist"))return ResponseEntity.status(404).body(m); if(m.contains("taken"))return ResponseEntity.status(409).body(m); return ResponseEntity.ok(m);}
}
