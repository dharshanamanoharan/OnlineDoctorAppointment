package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Model.DoctorModel;
import OnlineDoctorAppointmentSystem.Model.LoginModel;
import OnlineDoctorAppointmentSystem.Security.JwtAuthResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor addDoctor(DoctorModel doctorModel);
    Doctor getDoctor(Long docId);
    Doctor updateDoctorById(Long docId, DoctorModel doctorModel);
    String deleteDoctor(Long docId);

    //Doctor findByUserNameOrEmail(String userNameOrEmail) throws UsernameNotFoundException;

    //JwtAuthResponse docLogin(LoginModel loginModel);
}
