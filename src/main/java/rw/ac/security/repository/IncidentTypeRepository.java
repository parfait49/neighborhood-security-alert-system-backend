package rw.ac.security.repository;
import rw.ac.security.domain.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;
public interface IncidentTypeRepository extends JpaRepository<IncidentType, UUID> {
    @Query("SELECT t FROM IncidentType t WHERE t.guards IS EMPTY")
    List<IncidentType> findUnusedIncidentTypes();
}
