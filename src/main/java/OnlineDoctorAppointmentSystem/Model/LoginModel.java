package OnlineDoctorAppointmentSystem.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel{
    private String userNameOrEmail;
    private String password;
}

