package lab.gravy.springbatch.part4.repository;

import lab.gravy.springbatch.part4.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    List<User> findAllByUpdateDate(LocalDate updateDate);

}
