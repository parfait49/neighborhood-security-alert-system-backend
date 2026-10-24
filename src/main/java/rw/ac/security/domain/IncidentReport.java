package rw.ac.security.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name="incident_report")
public class IncidentReport {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private LocalDate reportDate;
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    private String description;
    private String location;
    @ManyToOne @JoinColumn(name="resident_id")
    private Resident resident;
    @ManyToOne @JoinColumn(name="security_guard_id")
    private SecurityGuard securityGuard;
    public IncidentReport() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public LocalDate getReportDate(){return reportDate;} public void setReportDate(LocalDate reportDate){this.reportDate=reportDate;}
    public ReportStatus getStatus(){return status;} public void setStatus(ReportStatus status){this.status=status;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public String getLocation(){return location;} public void setLocation(String location){this.location=location;}
    public Resident getResident(){return resident;} public void setResident(Resident resident){this.resident=resident;}
    public SecurityGuard getSecurityGuard(){return securityGuard;} public void setSecurityGuard(SecurityGuard securityGuard){this.securityGuard=securityGuard;}
}
