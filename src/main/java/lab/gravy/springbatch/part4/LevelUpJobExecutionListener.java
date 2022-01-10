package lab.gravy.springbatch.part4;

import lab.gravy.springbatch.part4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.List;

import static java.time.LocalDate.now;

@Slf4j
@RequiredArgsConstructor
public class LevelUpJobExecutionListener implements JobExecutionListener {

    private final UserRepository userRepository;


    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        List<User> updatedUserList = userRepository.findAllByUpdateDate(now());
        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        log.info("회원등급 업데이트 배치 프로그램");
        log.info("-----------------------");
        log.info("총 데이터 처리 {}건, 처리 시간 {}millis", updatedUserList.size(), time);
    }
}
