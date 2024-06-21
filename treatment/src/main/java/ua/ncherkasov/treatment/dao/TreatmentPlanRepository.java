package ua.ncherkasov.treatment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.ncherkasov.treatment.dao.entity.TreatmentPlan;

import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    List<TreatmentPlan> findByIsProcessed(boolean isProcessed);

    @Query(value = "SELECT tp FROM TreatmentPlan tp WHERE tp.isProcessed = ?1 and tp.nextExecutionTime <= ?2")
    List<TreatmentPlan> findByIsProcessedAndDate(boolean isProcessed, Date endDate);
}
