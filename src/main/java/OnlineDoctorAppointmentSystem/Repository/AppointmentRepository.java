package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
}
