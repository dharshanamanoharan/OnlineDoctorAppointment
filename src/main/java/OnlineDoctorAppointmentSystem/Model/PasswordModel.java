package OnlineDoctorAppointmentSystem.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordModel {
    private String userNameOrEmail;
    private String oldPassword;
    private String newPassword;
}
