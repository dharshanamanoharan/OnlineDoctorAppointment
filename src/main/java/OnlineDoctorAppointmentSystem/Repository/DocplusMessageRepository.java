package OnlineDoctorAppointmentSystem.Repository;

import OnlineDoctorAppointmentSystem.Entity.DocplusMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocplusMessageRepository extends JpaRepository<DocplusMessage,Long> {
}
