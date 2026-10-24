package rw.ac.security.service;
import rw.ac.security.repository.*;
import rw.ac.security.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
@Service
public class DashboardService {
 private final ResidentRepository residents; private final SecurityGuardRepository guards; private final PatrolZoneRepository zones; private final IncidentReportRepository reports; private final ResponseActionRepository actions;
 public DashboardService(ResidentRepository r, SecurityGuardRepository g, PatrolZoneRepository z, IncidentReportRepository i, ResponseActionRepository a){residents=r;guards=g;zones=z;reports=i;actions=a;}
 public Map<String,Long> summary(){
   Map<String,Long> m=new LinkedHashMap<>();
   m.put("residents", residents.count()); m.put("securityGuards",guards.count()); m.put("freePatrolZones",(long)zones.findFreePatrolZones().size());
   m.put("reportsToday",reports.countByReportDate(LocalDate.now()));
   long reported=reports.findByStatusOrderByReportDateAsc(ReportStatus.REPORTED).size(); m.put("reported",reported); m.put("responseActions",actions.count()); return m;
 }
}
