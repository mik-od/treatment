package ua.ncherkasov.treatment.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ncherkasov.treatment.dao.TreatmentTaskRepository;
import ua.ncherkasov.treatment.dao.entity.TreatmentActionEnum;
import ua.ncherkasov.treatment.dao.entity.TreatmentPlan;
import ua.ncherkasov.treatment.dao.TreatmentPlanRepository;
import ua.ncherkasov.treatment.dao.entity.TreatmentStatusEnum;
import ua.ncherkasov.treatment.dao.entity.TreatmentTask;
import ua.ncherkasov.treatment.service.CreateTreatmentService;
import ua.ncherkasov.treatment.service.SchedulerHelper;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static ua.ncherkasov.treatment.service.SchedulerHelper.calculateNextExecutionTime;
import static ua.ncherkasov.treatment.service.SchedulerHelper.normalizeRecurrencePattern;


@Service
@Transactional
public class CreateTreatmentServiceImpl implements CreateTreatmentService {

    Logger logger = LogManager.getLogger(CreateTreatmentServiceImpl.class);

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentTaskRepository treatmentTaskRepository;

    public CreateTreatmentServiceImpl(TreatmentPlanRepository treatmentPlanRepository,
                                      TreatmentTaskRepository treatmentTaskRepository) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.treatmentTaskRepository = treatmentTaskRepository;
    }

    public List<TreatmentTask> createTreatmentTasks() {
        updateTreatmentPlansForNextTime();
        return createActiveTreatmentTasks();
    }

    private List<TreatmentTask> createActiveTreatmentTasks() {
        List<TreatmentTask> treatmentTasks = new ArrayList<>();
        List<TreatmentPlan> treatmentPlans =
                treatmentPlanRepository.findByIsProcessedAndDate(false, Calendar.getInstance().getTime());
        for (TreatmentPlan treatmentPlan : treatmentPlans) {
            treatmentPlan.setProcessed(true);
            TreatmentTask treatmentTask = new TreatmentTask();
            treatmentTask.setTreatmentPlan(treatmentPlan);
            treatmentTask.setTreatmentActionEnum(treatmentPlan.getTreatmentAction());
            treatmentTask.setSubjectPatient(treatmentPlan.getSubjectPatient());
            treatmentTask.setStartTime(treatmentPlan.getNextExecutionTime());
            treatmentTask.setStatus(TreatmentStatusEnum.Active);
            treatmentTasks.add(treatmentTask);
        }
        treatmentTaskRepository.saveAll(treatmentTasks);
        return treatmentTasks;
    }

    private void updateTreatmentPlansForNextTime() {
        List<TreatmentPlan> treatmentPlans = treatmentPlanRepository.findByIsProcessed(true);
        treatmentPlans.parallelStream().forEach(tp -> {
            tp.setNextExecutionTime(SchedulerHelper.calculateNextExecutionTime(tp.getCronExpression(), tp.getEndTime()));
            tp.setProcessed(false);
        });
    }

    public List<TreatmentPlan> createTreatmentPlans() {
        logger.debug("Start createTreatmentPlans");
        List<TreatmentPlan> treatmentPlans = new ArrayList<>();
        Random random = new Random();

        int planCount = random.nextInt(3,5);
        for (int i = 0; i <= planCount; i++) {
            TreatmentPlan treatmentPlan = new TreatmentPlan();
            treatmentPlan.setSubjectPatient("Patient" + random.nextInt(100));
            treatmentPlan.setTreatmentAction(random.nextBoolean() ? TreatmentActionEnum.ActionA : TreatmentActionEnum.ActionB);
            treatmentPlan.setStartTime(
                    Date.from(LocalDateTime.now().minusMinutes(random.nextInt(5))
                                                 .plusMinutes(random.nextInt(10))
                                                 .atZone(ZoneId.systemDefault()).toInstant()));
            if (random.nextBoolean()) {
                treatmentPlan.setEndTime(
                        Date.from(LocalDateTime.now()
                                .plusHours(random.nextInt(24))
                                .atZone(ZoneId.systemDefault()).toInstant()));
            }

            String day = "day ";
            if (random.nextBoolean()) {
                StringBuilder daySb = new StringBuilder();

                int dayOfWeekCount = random.nextInt(1,5);
                for (int j = 0; j <= dayOfWeekCount; j++) {
                    daySb.append(DayOfWeek.of(random.nextInt(1,8)));
                    if (j != dayOfWeekCount) daySb.append(" and ");
                    else daySb.append(" ");
                }
                day = daySb.toString();
            }

            StringBuilder timeSb = new StringBuilder();
            int timeCount = random.nextInt(1,3);
            for (int j = 0; j <= timeCount; j++) {
                Calendar calendar = Calendar.getInstance();
                int minutes = calendar.get(Calendar.MINUTE);
                timeSb.append(random.nextInt(23)).append(":").append(random.nextInt(minutes, minutes + 1));
                if (j != timeCount) timeSb.append(" and ");
                else timeSb.append(" ");
            }

            String recurrencePattern = "every " + day + "at " + timeSb;
            logger.debug("{} was generated: ", recurrencePattern);
            treatmentPlan.setRecurrencePattern(recurrencePattern);
            String recurrencePatternNormalized = normalizeRecurrencePattern(recurrencePattern);
            treatmentPlan.setCronExpression(recurrencePatternNormalized);
            treatmentPlan.setNextExecutionTime(calculateNextExecutionTime(recurrencePatternNormalized, treatmentPlan.getEndTime()));

            treatmentPlans.add(treatmentPlan);
        }

        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setSubjectPatient("Patient" + random.nextInt(100));
        treatmentPlan.setTreatmentAction(random.nextBoolean() ? TreatmentActionEnum.ActionA : TreatmentActionEnum.ActionB);
        treatmentPlan.setStartTime(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        String recurrencePattern = "every day at " + hours + ":" + minutes;
        treatmentPlan.setRecurrencePattern(recurrencePattern);
        String recurrencePatternNormalized = normalizeRecurrencePattern(recurrencePattern);
        treatmentPlan.setCronExpression(recurrencePatternNormalized);
        treatmentPlan.setNextExecutionTime(calculateNextExecutionTime(recurrencePatternNormalized, treatmentPlan.getEndTime()));

        treatmentPlans.add(treatmentPlan);

        treatmentPlanRepository.saveAll(treatmentPlans);
        logger.debug("Finish createTreatmentPlans");
        return treatmentPlans;
    }

}
