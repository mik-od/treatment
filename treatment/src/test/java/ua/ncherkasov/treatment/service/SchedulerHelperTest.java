package ua.ncherkasov.treatment.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchedulerHelperTest {

    @Test
    public void normalizeRecurrencePatternTest() {
        String str = "every day at 08:01 and 16:02";
        Assertions.assertEquals("* 01,02 08,16 * * *", SchedulerHelper.normalizeRecurrencePattern(str));
        str = "every Monday and Friday at 00:00 and 10:00 and 15:00";
        Assertions.assertEquals("* 00,00,00 00,10,15 * * 1,5", SchedulerHelper.normalizeRecurrencePattern(str));
    }
}
