package rw.ac.security.repository;
import rw.ac.security.domain.SecurityGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
public interface SecurityGuardRepository extends JpaRepository<SecurityGuard, UUID> {
    @Query("SELECT DISTINCT g FROM SecurityGuard g JOIN g.incidentTypes t WHERE LOWER(t.name) = LOWER(:name)")
    List<SecurityGuard> findByIncidentTypeNameIgnoreCase(@Param("name") String name);
    boolean existsByPatrolZone_Id(UUID patrolZoneId);
}
