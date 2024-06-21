package ua.ncherkasov.treatment.dao.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(indexes = {
    @Index(name = "processed_nextExecutionTime_idx", columnList = "isProcessed, nextExecutionTime")
})
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private TreatmentActionEnum treatmentActionEnum;

    @Column
    private String subjectPatient;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column
    private String recurrencePattern;

    @Column
    private String cronExpression;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextExecutionTime;

    @Column
    private Boolean isProcessed = false;

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getSubjectPatient() {
        return subjectPatient;
    }

    public void setSubjectPatient(String subjectPatient) {
        this.subjectPatient = subjectPatient;
    }

    public TreatmentActionEnum getTreatmentAction() {
        return treatmentActionEnum;
    }

    public void setTreatmentAction(TreatmentActionEnum treatmentActionEnum) {
        this.treatmentActionEnum = treatmentActionEnum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public Boolean getProcessed() {
        return isProcessed;
    }

    public void setProcessed(Boolean processed) {
        isProcessed = processed;
    }
}
