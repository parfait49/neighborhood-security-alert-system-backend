package rw.ac.security.repository;
import rw.ac.security.domain.IncidentReport;
import rw.ac.security.domain.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface IncidentReportRepository extends JpaRepository<IncidentReport, UUID> {
    List<IncidentReport> findByStatusOrderByReportDateAsc(ReportStatus status);
    List<IncidentReport> findByReportDateBetweenOrderByReportDateAsc(LocalDate start, LocalDate end);
    boolean existsBySecurityGuard_IdAndReportDateAndStatusNot(UUID securityGuardId, LocalDate reportDate, ReportStatus status);
    boolean existsBySecurityGuard_PatrolZone_Id(UUID patrolZoneId);
    long countByReportDate(LocalDate reportDate);
    @Query("SELECT i.status, COUNT(i) FROM IncidentReport i GROUP BY i.status")
    List<Object[]> countReportsByStatus();
    @Modifying
    @Transactional
    @Query("UPDATE IncidentReport i SET i.status = rw.ac.security.domain.ReportStatus.DISMISSED WHERE i.securityGuard.id = :guardId AND i.reportDate = :date AND i.status <> rw.ac.security.domain.ReportStatus.RESOLVED")
    int dismissReportsForGuardOnDate(@Param("guardId") UUID guardId, @Param("date") LocalDate date);
    @Query("SELECT i FROM IncidentReport i WHERE i.reportDate = :date AND i.securityGuard.id = :guardId AND i.status <> rw.ac.security.domain.ReportStatus.DISMISSED")
    List<IncidentReport> findNonDismissedForGuardOnDate(@Param("date") LocalDate date, @Param("guardId") UUID guardId);
}
