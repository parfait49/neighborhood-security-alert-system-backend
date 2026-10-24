package rw.ac.security.repository;
import rw.ac.security.domain.PatrolZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface PatrolZoneRepository extends JpaRepository<PatrolZone, UUID> {
    Optional<PatrolZone> findByZoneNumber(int zoneNumber);
    @Query("SELECT z FROM PatrolZone z WHERE NOT EXISTS (SELECT g FROM SecurityGuard g WHERE g.patrolZone = z) ORDER BY z.zoneNumber ASC")
    List<PatrolZone> findFreePatrolZones();
}
