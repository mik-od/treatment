package ua.ncherkasov.treatment.service.impl;

import org.springframework.stereotype.Service;
import ua.ncherkasov.treatment.service.CreateTreatmentService;
import ua.ncherkasov.treatment.service.SchedulerHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SchedulerHelperImpl implements SchedulerHelper {

    private final CreateTreatmentService createTreatmentService;

    public SchedulerHelperImpl(CreateTreatmentService createTreatmentService) {
        this.createTreatmentService = createTreatmentService;
    }

    @Override
    public ScheduledExecutorService scheduleCreatePlan() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(createTreatmentService::createTreatmentPlans,0,1, TimeUnit.MINUTES);
        return executor;
    }

    @Override
    public ScheduledExecutorService scheduleCreateTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(createTreatmentService::createTreatmentTasks,0,1, TimeUnit.MINUTES);
        return executor;
    }

}
