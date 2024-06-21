package ua.ncherkasov.treatment.service;

import ua.ncherkasov.treatment.dao.entity.TreatmentPlan;
import ua.ncherkasov.treatment.dao.entity.TreatmentTask;

import java.util.List;

public interface CreateTreatmentService {

    List<TreatmentPlan> createTreatmentPlans();

    List<TreatmentTask> createTreatmentTasks();
}
