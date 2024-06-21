package ua.ncherkasov.treatment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.ncherkasov.treatment.service.SchedulerHelper;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TreatmentApplication implements CommandLineRunner {

	@Autowired
	private SchedulerHelper schedulerHelper;

	public static void main(String[] args) {
		SpringApplication.run(TreatmentApplication.class, args);
	}


	@Override
	public void run(String... args) throws InterruptedException {
		schedulerHelper.scheduleCreateTask();
        try (ScheduledExecutorService executor = schedulerHelper.scheduleCreatePlan()) {
            executor.awaitTermination(1, TimeUnit.DAYS);
        }
    }
}
