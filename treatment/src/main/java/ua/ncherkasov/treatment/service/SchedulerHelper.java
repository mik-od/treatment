package ua.ncherkasov.treatment.service;

import org.springframework.scheduling.support.CronExpression;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

public interface SchedulerHelper {

    ScheduledExecutorService scheduleCreatePlan();

    ScheduledExecutorService scheduleCreateTask();

    static String normalizeRecurrencePattern(String recurrencePattern) {
        StringBuilder result = new StringBuilder();
        String[] words = recurrencePattern.split(" ");
        int counter = 1;
        String nextWord;
        if (words[counter].equalsIgnoreCase("DAY")) {
            result.append("*");
            counter += 2;
        }
        else {
            while ((counter < words.length) && !(nextWord = words[counter++]).equals("at")) {
                if (nextWord.equals("and")) {
                    result.append(",");
                } else {
                    result.append(DayOfWeek.valueOf(nextWord.toUpperCase()).getValue());
                }
            }

        }

        result.insert(0, " * * ");

        StringBuilder hours = new StringBuilder();
        StringBuilder minutes = new StringBuilder();
        while ((counter < words.length) ) {
            nextWord = words[counter];
            if (nextWord.equals("and")) {
                hours.append(",");
                minutes.append(",");
            }
            else {
                String[] time = nextWord.split(":");
                hours.append(time[0]);
                minutes.append(time[1]);
            }
            counter++;
        }

        result.insert(0, "* " + minutes + " " + hours);

        return result.toString();
    }

    static Date calculateNextExecutionTime(String recurrencePatternNormalized, Date endDate) {
        CronExpression exp = CronExpression.parse(recurrencePatternNormalized);
        LocalDateTime nextExecutionTime = exp.next(LocalDateTime.now());
        Date nextExecutionDate = null;
        if (nextExecutionTime!= null) {
            nextExecutionDate = Date.from(nextExecutionTime.atZone(ZoneId.systemDefault()).toInstant());
        }

        return nextExecutionDate == null || (endDate != null && nextExecutionDate.after(endDate)) ? null : nextExecutionDate;
    }
}
