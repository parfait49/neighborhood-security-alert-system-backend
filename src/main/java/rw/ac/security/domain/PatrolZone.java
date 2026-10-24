package rw.ac.security.domain;
import jakarta.persistence.*;
import java.util.UUID;
@Entity
@Table(name="patrol_zone")
public class PatrolZone {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private int zoneNumber;
    private String name;
    public PatrolZone() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public int getZoneNumber(){return zoneNumber;} public void setZoneNumber(int zoneNumber){this.zoneNumber=zoneNumber;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
}
