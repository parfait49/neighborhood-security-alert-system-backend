package rw.ac.security.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name="incident_type")
public class IncidentType {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private String name;
    @ManyToMany(mappedBy="incidentTypes")
    @JsonIgnore
    private List<SecurityGuard> guards = new ArrayList<>();
    public IncidentType() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public List<SecurityGuard> getGuards(){return guards;} public void setGuards(List<SecurityGuard> guards){this.guards=guards;}
}
