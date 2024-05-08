package OnlineDoctorAppointmentSystem.Repository;
import OnlineDoctorAppointmentSystem.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByEmail(String email);

    Optional<Object> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
