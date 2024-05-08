package OnlineDoctorAppointmentSystem.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class UserModel {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String user_avatar;
    private Set<String> roles;
    private String role;
}



