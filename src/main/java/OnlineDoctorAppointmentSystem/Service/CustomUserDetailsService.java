package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Repository.DoctorRepository;
import OnlineDoctorAppointmentSystem.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws NoResourceException {
        //User Login
        OnlineDoctorAppointmentSystem.Entity.User user = userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new NoResourceException("User Not Found! No user exists with this name or emailID", HttpStatus.NOT_FOUND));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
        return new User(
                usernameOrEmail,
                user.getPassword(),
                authorities
        );
    }

}
