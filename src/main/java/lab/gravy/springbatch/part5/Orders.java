package lab.gravy.springbatch.part5;

import lab.gravy.springbatch.part4.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Orders {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String itemName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int amount;

    private LocalDate createdDate;

    @Builder
    private Orders(String itemName, int amount, LocalDate createdDate) {
        this.itemName = itemName;
        this.amount = amount;
        this.createdDate = createdDate;
    }


}
