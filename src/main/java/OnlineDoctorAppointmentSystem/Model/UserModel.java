package OnlineDoctorAppointmentSystem.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String user_avatar;
    private Set<String> roles;
    private String role;
    private byte[] displayPicture;
}



