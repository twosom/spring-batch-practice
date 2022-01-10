package lab.gravy.springbatch.part4;

import lab.gravy.springbatch.part4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SaveUserTasklet implements Tasklet {

    private final UserRepository userRepository;


    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {
        List<User> users = createUsers();

        Collections.shuffle(users);

        userRepository.saveAll(users);

        return RepeatStatus.FINISHED;
    }

    private List<User> createUsers() {
        return IntStream.range(0, 400)
                .mapToObj(index -> User.builder()
                        .totalAmount(isDefaultUser(index) ? 1_000 : isNormalUser(index) ? 200_000 : isSilverUser(index) ? 300_000 : 500_000)
                        .username("test username" + index)
                        .build())
                .collect(toList());
    }

    private boolean isSilverUser(int index) {
        return index >= 200 && index < 300;
    }

    private boolean isNormalUser(int index) {
        return index >= 100 && index < 200;
    }

    private boolean isDefaultUser(int index) {
        return index >= 0 && index < 100;
    }
}
