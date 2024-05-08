package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.AppointmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetails,Long> {
}
