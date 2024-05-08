package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.Appointment;
import OnlineDoctorAppointmentSystem.Entity.AppointmentDetails;
import OnlineDoctorAppointmentSystem.Model.AppointmentModel;

import java.util.List;

public interface AppointmentService {
    Appointment addAppointment(AppointmentModel appointmentModel);

    List<AppointmentDetails> getAllAppointment();

    Appointment getAppointment(Long appointmentId);

    Appointment updateAppointment(Long appointmentId,AppointmentModel appointmentModel);

    String deleteAppointment(Long appointmentId);
}
