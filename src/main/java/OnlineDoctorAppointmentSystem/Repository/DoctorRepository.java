package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long>
{
    Optional<Doctor> findByUserNameOrEmail(String userNameOrEmail, String userNameOrEmail1);
}
