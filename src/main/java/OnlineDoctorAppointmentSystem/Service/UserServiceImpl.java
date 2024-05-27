package OnlineDoctorAppointmentSystem.Service;
import OnlineDoctorAppointmentSystem.Entity.*;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.LoginModel;
import OnlineDoctorAppointmentSystem.Model.SendEmailModel;
import OnlineDoctorAppointmentSystem.Model.UserModel;
import OnlineDoctorAppointmentSystem.Security.JwtAuthResponse;
import OnlineDoctorAppointmentSystem.Security.JwtTokenProvider;
import OnlineDoctorAppointmentSystem.Repository.PasswordResetTokenRepository;
import OnlineDoctorAppointmentSystem.Repository.UserRepository;
import OnlineDoctorAppointmentSystem.Repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public User registerUser(UserModel userModel) {

       // check whether username already exists in the database
        if(!userRepository.findByUserName(userModel.getUserName()).isEmpty())
        {
            throw new NoResourceException("Username already exists!Try to register with a different User Name",HttpStatus.CONFLICT);
        }

        // check whether email already exists in database
        if(!userRepository.findByEmail(userModel.getEmail()).isEmpty())
        {
            throw new NoResourceException( "Email already exists!Try to register with a different email Id",HttpStatus.IM_USED);
        }

        User user=new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setUserName((userModel.getUserName()));
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        //Set<Role> roles=new HashSet<>();
        Set<String> roles=new HashSet<>();
        //Role userRole=roleRepository.findByName("ROLE_USER");
        //user.setDisplayPicture();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken=new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {

        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);

        if(verificationToken==null)
        {
            return "invalid";
        }

        User user=verificationToken.getUser();
        Calendar cal=Calendar.getInstance();

        if((verificationToken.getExpirationTime().getTime()
                -cal.getTime().getTime())<=0)
        {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String email) {
        VerificationToken verificationToken=verificationTokenRepository.findByEmail(email);
        if(verificationToken!=null){
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationTokenRepository.save(verificationToken);
            return verificationToken;
        }
        else return null;
    }

    @Override
    public JwtAuthResponse userLogin(LoginModel loginModel) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               loginModel.getUserNameOrEmail(),
                loginModel.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtTokenProvider.generateJwtToken(authentication);
        Optional<User> userOptional = userRepository.findByUserNameOrEmail(loginModel.getUserNameOrEmail(),
                loginModel.getUserNameOrEmail());

        Set<String> role = null;
        Long userId=null;
        if(userOptional.isPresent()) {
            User loggedInUser = userOptional.get();
            userId=loggedInUser.getId();
            //Optional<Role> optionalRole = loggedInUser.getRoles().stream().findFirst();
            //Set<String> optionalRole = loggedInUser.getRoles().stream().collect(Collectors.toSet());
            role=loggedInUser.getRoles();
            /*if (!optionalRole.isEmpty()) {
                Role userRole = optionalRole.;
                role = userRole.getName();
            }*/
        }
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setRole(role);
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setUserId(userId);
        return jwtAuthResponse;
    }
    @Override
    public void createPasswordResetToken(String email, String token,User user) {
        PasswordResetToken passwordResetToken=passwordResetTokenRepository.findByEmail(email);
        if(passwordResetToken!=null) {
            passwordResetToken.setToken(token);
            passwordResetTokenRepository.save(passwordResetToken);
        }
        else{
            PasswordResetToken newUser = new PasswordResetToken(user,token);
            passwordResetTokenRepository.save(newUser);
        }
    }
    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken=passwordResetTokenRepository.findByToken(token);

        if(passwordResetToken==null)
        {
            return "invalid";
        }

        User user=passwordResetToken.getUser();
        Calendar cal=Calendar.getInstance();

        if((passwordResetToken.getExpirationTime().getTime()
                -cal.getTime().getTime())<=0)
        {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    @Override
    public User findByUserNameOrEmail(String userNameOrEmail) throws UsernameNotFoundException {

        User user=userRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail)
                .orElseThrow(() -> new NoResourceException("User Not Found! No user exists with this name or emailID",HttpStatus.NOT_FOUND));
        return user;
    }
    @Override
    public String sendSimpleMail(SendEmailModel mail) {

                // Creating a simple mail message
                SimpleMailMessage mailMessage = new SimpleMailMessage();

                // Setting up necessary details
                mailMessage.setFrom(fromEmail);
                mailMessage.setTo(mail.getEmailRecipient());
                mailMessage.setText(mail.getEmailMessage());
                mailMessage.setSubject(mail.getEmailSubject());

                // Sending the mail
                javaMailSender.send(mailMessage);
                return "Mail Sent Successfully!";
            }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserModel getUser(Long userId) {
        User user=userRepository.findById(userId).orElseThrow
                (()->new NoResourceException("User not Found"+userId, HttpStatus.NOT_FOUND));
        UserModel user1=new UserModel();
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setUserName(user.getUserName());
        user1.setUser_avatar(user.getUser_avatar());
        user1.setDisplayPicture(user.getDisplayPicture());
        user1.setRoles(user.getRoles());
        user1.setRole(user.getRole());
        return user1;
    }

    @Override
    public User updateUserById(Long userId, Optional<MultipartFile> file, String firstName, String lastName, String userName, String email, String password, String role, Set<String> roles) {
        User user=userRepository.findById(userId).orElseThrow
                (()->new NoResourceException("User not Found"+userId, HttpStatus.NOT_FOUND));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            user.setEmail(email);
            if(!file.isEmpty()) {
                try {
                    user.setDisplayPicture(file.get().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        if(password!=null && password.trim().replaceAll("\\s", "")!="") {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(!roles.isEmpty())
        {
            //Set<String> roles=new HashSet<>();
            //Role userRole=roleRepository.findByName("ROLE_USER");
            //roles.add("ROLE_USER");
            //Role otherRole=roleRepository.findByName(userModel.getRole());
           // Set<String> otherRole=new HashSet<>();
            //if(otherRole!=null){roles.add(otherRole);}
            user.setRoles(roles);
            user.setRole(roles.toString());
            //log.info(userModel.getRoles().toString());
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public String deleteUser(Long userId) {
        User user=userRepository.findById(userId).orElseThrow
                (()->new NoResourceException("User not Found"+userId, HttpStatus.NOT_FOUND));
        if(user!=null) {
            passwordResetTokenRepository.deleteByUserId(userId);
            verificationTokenRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);
            return "Deleted";
        }
        return null;
    }
}


