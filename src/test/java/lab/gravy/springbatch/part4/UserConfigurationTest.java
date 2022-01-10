package lab.gravy.springbatch.part4;

import lab.gravy.springbatch.BatchTestConfiguration;
import lab.gravy.springbatch.part4.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@ContextConfiguration(classes = {UserConfiguration.class, BatchTestConfiguration.class})
class UserConfigurationTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    UserRepository userRepository;

    @Test
    void test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(
                300,
                jobExecution.getStepExecutions()
                        .stream()
                        .filter(e -> e.getStepName().equals("userLevelUpStep"))
                        .mapToInt(StepExecution::getWriteCount)
                        .sum()
        );


        assertEquals(userRepository.count(), 400);
    }

}