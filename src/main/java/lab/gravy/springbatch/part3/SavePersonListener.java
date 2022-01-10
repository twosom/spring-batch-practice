package lab.gravy.springbatch.part3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeStep;

@Slf4j
public class SavePersonListener {


    public static class SavePersonJobExecutionListener implements JobExecutionListener {
        @Override
        public void beforeJob(JobExecution jobExecution) {
            log.info("beforeJob");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            int sum = jobExecution.getStepExecutions()
                    .parallelStream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum();
            log.info("afterJob : sum = {}", sum);
        }
    }


    public static class SavePersonAnnotationJobExecutionListener {

        @BeforeJob
        public void beforeJob(JobExecution jobExecution) {
            log.info("annotationBeforeJob");
        }

        @AfterJob
        public void afterJob(JobExecution jobExecution) {
            int sum = jobExecution.getStepExecutions()
                    .parallelStream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum();
            log.info("annotationAfterJob : sum = {}", sum);
        }
    }

    public static class SavePersonAnnotationStepExecutionListener {
        @BeforeStep
        public void beforeStep(StepExecution stepExecution) {
            log.info("annotationBeforeStep");
        }

        @AfterStep
        public ExitStatus afterStep(StepExecution stepExecution) {
            int writeCount = stepExecution.getWriteCount();
            log.info("annotationAfterStep : {}", writeCount);
            if (writeCount == 0) {
                return ExitStatus.FAILED;
            }
            return stepExecution.getExitStatus();
        }
    }

    public static class SavePersonProcessorListener implements ItemProcessListener<Person, Person> {
        @Override
        public void beforeProcess(Person item) {

        }

        @Override
        public void afterProcess(Person item, Person result) {

        }

        @Override
        public void onProcessError(Person item, Exception e) {
            log.info("error ! item age = {}", item.getAge());
        }
    }

}
