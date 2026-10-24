package rw.ac.security.domain;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name="security_guard")
public class SecurityGuard {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    @ManyToOne
    @JoinColumn(name="patrol_zone_id")
    private PatrolZone patrolZone;
    @ManyToMany
    @JoinTable(name="security_guard_incident_type",
        joinColumns=@JoinColumn(name="security_guard_id"),
        inverseJoinColumns=@JoinColumn(name="incident_type_id"))
    private List<IncidentType> incidentTypes = new ArrayList<>();
    public SecurityGuard() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getFirstName(){return firstName;} public void setFirstName(String firstName){this.firstName=firstName;}
    public String getLastName(){return lastName;} public void setLastName(String lastName){this.lastName=lastName;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public PatrolZone getPatrolZone(){return patrolZone;} public void setPatrolZone(PatrolZone patrolZone){this.patrolZone=patrolZone;}
    public List<IncidentType> getIncidentTypes(){return incidentTypes;} public void setIncidentTypes(List<IncidentType> incidentTypes){this.incidentTypes=incidentTypes;}
}
