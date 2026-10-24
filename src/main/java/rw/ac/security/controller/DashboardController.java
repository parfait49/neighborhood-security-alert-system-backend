package rw.ac.security.controller;
import rw.ac.security.service.DashboardService; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController
public class DashboardController {
 private final DashboardService s; public DashboardController(DashboardService s){this.s=s;}
 @GetMapping("/api/dashboard/summary") public Map<String,Long> summary(){return s.summary();}
}
