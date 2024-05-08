package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.BookingAvailability;
import OnlineDoctorAppointmentSystem.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingAvailabilityRepository extends JpaRepository<BookingAvailability,Long> {

    BookingAvailability findByDoctorId(Doctor doctorId);

}
