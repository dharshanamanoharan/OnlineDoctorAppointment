package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserNameOrEmail(String userName, String email);

    Optional<User> findByUserName(String userName);

    Optional<User>findByEmail(String email);

    /*@Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    Long activeUsersCount();*/
}
