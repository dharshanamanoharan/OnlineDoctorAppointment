package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository <VerificationToken,Long>{
    
    VerificationToken findByEmail(String email);

    VerificationToken findByToken(String token);

    void deleteByUserId(Long userId);

    VerificationToken findByUserId(Long userId);
}
