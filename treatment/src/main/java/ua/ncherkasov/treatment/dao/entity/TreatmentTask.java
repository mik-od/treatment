package ua.ncherkasov.treatment.dao.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "treatmentplan_idx", columnList = "treatmentplan_id")
})
public class TreatmentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private TreatmentActionEnum treatmentActionEnum;

    @Column
    private String subjectPatient;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column
    private TreatmentStatusEnum status;

    @ManyToOne
    @JoinColumn(name="treatmentplan_id", nullable = false, foreignKey = @ForeignKey(name = "treatmenttask_treatmentplan_fk"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private TreatmentPlan treatmentPlan;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TreatmentActionEnum getTreatmentActionEnum() {
        return treatmentActionEnum;
    }

    public void setTreatmentActionEnum(TreatmentActionEnum treatmentActionEnum) {
        this.treatmentActionEnum = treatmentActionEnum;
    }

    public String getSubjectPatient() {
        return subjectPatient;
    }

    public void setSubjectPatient(String subjectPatient) {
        this.subjectPatient = subjectPatient;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public TreatmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TreatmentStatusEnum status) {
        this.status = status;
    }

    public TreatmentPlan getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(TreatmentPlan treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
}
