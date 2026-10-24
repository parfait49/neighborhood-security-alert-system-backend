package rw.ac.security.repository;
import rw.ac.security.domain.ResponseAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
public interface ResponseActionRepository extends JpaRepository<ResponseAction, UUID> {
    @Query("SELECT a FROM ResponseAction a WHERE a.incidentReport.resident.nationalId = :nationalId ORDER BY a.actionDate DESC")
    List<ResponseAction> findResidentActionHistory(@Param("nationalId") String nationalId);
    boolean existsByIncidentReport_Id(UUID incidentReportId);
}
