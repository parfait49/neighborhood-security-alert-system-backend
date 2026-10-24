package rw.ac.security.domain;
import jakarta.persistence.*;
import java.util.UUID;
@Entity
@Table(name="resident")
public class Resident {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String nationalId;
    private String phone;
    private String street;
    public Resident() {}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getFirstName(){return firstName;} public void setFirstName(String firstName){this.firstName=firstName;}
    public String getLastName(){return lastName;} public void setLastName(String lastName){this.lastName=lastName;}
    public String getNationalId(){return nationalId;} public void setNationalId(String nationalId){this.nationalId=nationalId;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public String getStreet(){return street;} public void setStreet(String street){this.street=street;}
}
