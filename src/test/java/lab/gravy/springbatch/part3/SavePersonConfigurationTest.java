package lab.gravy.springbatch.part3;

import lab.gravy.springbatch.BatchTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@ContextConfiguration(classes = {SavePersonConfiguration.class, BatchTestConfiguration.class})
class SavePersonConfigurationTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    PersonRepository personRepository;

    @AfterEach
    void tearDown() {
        personRepository.deleteAllInBatch();
    }


    @Test
    void test_step() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("savePersonStep");

        assertEquals(3, jobExecution.getStepExecutions().stream().mapToInt(StepExecution::getWriteCount).sum());
        assertEquals(3, personRepository.findAll().size());


    }

    @Test
    void test_allow_duplicate() throws Exception {
        //given
        //TODO Job Parameter 설정
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allowDuplicate", "false")
                .addString("chunkSize", "10")
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertEquals(3, jobExecution.getStepExecutions().parallelStream().mapToInt(StepExecution::getWriteCount).sum());
        assertEquals(3, personRepository.findAll().size());
    }


    @Test
    void test_not_allow_duplicate() throws Exception {
        //given
        //TODO Job Parameter 설정
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allowDuplicate", "true")
                .addString("chunkSize", "10")
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertEquals(100, jobExecution.getStepExecutions().parallelStream().mapToInt(StepExecution::getWriteCount).sum());
        assertEquals(100, personRepository.findAll().size());
    }
}