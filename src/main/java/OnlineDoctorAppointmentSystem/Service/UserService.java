package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.User;
import OnlineDoctorAppointmentSystem.Entity.VerificationToken;
import OnlineDoctorAppointmentSystem.Model.LoginModel;
import OnlineDoctorAppointmentSystem.Model.SendEmailModel;
import OnlineDoctorAppointmentSystem.Model.UserModel;
import OnlineDoctorAppointmentSystem.Security.JwtAuthResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
     User registerUser(UserModel userModel);
     void saveVerificationTokenForUser(String token, User user);
    String validateVerificationToken(String token);
    VerificationToken generateNewVerificationToken(String oldToken);

    JwtAuthResponse userLogin(LoginModel loginModel);

    void createPasswordResetToken(String email, String token,User user);
    String validatePasswordResetToken(String token);
    Optional<User> getUserByPasswordResetToken(String token);
    void changePassword(User user, String newPassword);
    boolean checkIfValidOldPassword(User user, String oldPassword);
    User findByUserNameOrEmail(String userNameOrEmail) throws UsernameNotFoundException;
    String sendSimpleMail(SendEmailModel mail);

    List<User> getAllUsers();

    UserModel getUser(Long userId);

    User updateUserById(Long userId, Optional<MultipartFile> file, String firstName, String lastName, String userName, String email, String password, String role, Set<String> roles);

    String deleteUser(Long userId);


}
