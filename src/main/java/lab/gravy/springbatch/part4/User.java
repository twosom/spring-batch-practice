package lab.gravy.springbatch.part4;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;

    @Enumerated(STRING)
    private Level level = Level.NORMAL;

    private int totalAmount;


    private LocalDate updateDate;


    @Builder
    private User(String username, int totalAmount) {
        this.username = username;
        this.totalAmount = totalAmount;
    }

    public boolean availableLevelUp() {
        return Level.availableLevelUp(this.getLevel(), this.getTotalAmount());
    }

    public Level levelUp() {
        Level nextLevel = level.getNextLevelByTotalAmount(this.getTotalAmount());
        this.level = nextLevel;
        this.updateDate = LocalDate.now();

        return nextLevel;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Level {
        VIP(500_000, null),
        GOLD(500_000, VIP),
        SILVER(300_000, GOLD),
        NORMAL(200_000, SILVER);

        private final int nextAmount;
        private final Level nextLevel;

        private static boolean availableLevelUp(Level level, int totalAmount) {
            if (Objects.isNull(level)) {
                return false;
            }

            if (Objects.isNull(level.nextLevel)) {
                return false;
            }

            return totalAmount >= level.nextAmount;
        }

        private Level getNextLevelByTotalAmount(int totalAmount) {
            return Arrays.stream(Level.values())
                    .filter(level -> totalAmount >= level.getNextAmount())
                    .sorted(Comparator.comparingInt(Level::getNextAmount).reversed())
                    .map(Level::getNextLevel)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(VIP);
        }
    }
}

