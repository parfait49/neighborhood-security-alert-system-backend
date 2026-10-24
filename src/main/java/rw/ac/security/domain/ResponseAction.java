package rw.ac.security.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name="response_action")
public class ResponseAction {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private String actionName;
    private String notes;
    private int minutesTaken;
    private LocalDate actionDate;
    @ManyToOne @JoinColumn(name="incident_report_id")
    private IncidentReport incidentReport;
    public ResponseAction() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getActionName(){return actionName;} public void setActionName(String actionName){this.actionName=actionName;}
    public String getNotes(){return notes;} public void setNotes(String notes){this.notes=notes;}
    public int getMinutesTaken(){return minutesTaken;} public void setMinutesTaken(int minutesTaken){this.minutesTaken=minutesTaken;}
    public LocalDate getActionDate(){return actionDate;} public void setActionDate(LocalDate actionDate){this.actionDate=actionDate;}
    public IncidentReport getIncidentReport(){return incidentReport;} public void setIncidentReport(IncidentReport incidentReport){this.incidentReport=incidentReport;}
}
