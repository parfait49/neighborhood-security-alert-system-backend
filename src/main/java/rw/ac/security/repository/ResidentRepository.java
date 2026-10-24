package rw.ac.security.repository;
import rw.ac.security.domain.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
public interface ResidentRepository extends JpaRepository<Resident, UUID> {
    List<Resident> findByStreetIgnoreCaseOrderByLastNameAsc(String street);
    @Query("SELECT DISTINCT r FROM Resident r JOIN IncidentReport i ON i.resident = r WHERE i.securityGuard.id = :guardId")
    List<Resident> findResidentsOfSecurityGuard(@Param("guardId") UUID guardId);
}
