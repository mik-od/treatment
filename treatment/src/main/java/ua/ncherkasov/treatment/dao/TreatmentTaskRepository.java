package ua.ncherkasov.treatment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ncherkasov.treatment.dao.entity.TreatmentTask;

@Repository
public interface TreatmentTaskRepository extends JpaRepository<TreatmentTask, Long> {

}
