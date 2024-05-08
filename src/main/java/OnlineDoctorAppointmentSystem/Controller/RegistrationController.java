package OnlineDoctorAppointmentSystem.Controller;
import OnlineDoctorAppointmentSystem.Entity.User;
import OnlineDoctorAppointmentSystem.Entity.VerificationToken;
import OnlineDoctorAppointmentSystem.Model.LoginModel;
import OnlineDoctorAppointmentSystem.Model.PasswordModel;
import OnlineDoctorAppointmentSystem.Model.SendEmailModel;
import OnlineDoctorAppointmentSystem.Model.UserModel;
import OnlineDoctorAppointmentSystem.Security.JwtAuthResponse;
import OnlineDoctorAppointmentSystem.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@Slf4j
@RequestMapping("/docplus.in/auth")
public class RegistrationController {
    @Autowired
    private UserService userService;

    //Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel) {
        User user = userService.registerUser(userModel);
        if (user != null) {
            //Create the verification token for the user with link
            String token= UUID.randomUUID().toString();
            userService.saveVerificationTokenForUser(token,user);

            //Send Mail to the User
            SendEmailModel mail=new SendEmailModel();
            mail.setEmailRecipient(user.getEmail());
            mail.setEmailSubject("Verifying the Registration");
            String mailUrl= "http://localhost:3000/docplus.in/verify-registration/"+token;
            mail.setEmailMessage("Greetings! Hello "+user.getFirstName()+" "+user.getLastName()+". Please click the link to verify your account: "+mailUrl);
            log.info("Greetings! Hello "+user.getFirstName()+" "+user.getLastName()+". Please click the link to verify your account: "+mailUrl);
            userService.sendSimpleMail(mail);
            return new ResponseEntity<>("Registered Successfully", HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>("Registration Failed", HttpStatus.CONFLICT);
        }
    }

    //Verifying the Registration
    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token)
    {
        String result=userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid"))
        {
            return new ResponseEntity<>("User verified Successfully!",HttpStatus.OK);
        }
        return new ResponseEntity<>("User verification Failed!Verification link has been expired!",HttpStatus.BAD_REQUEST);
    }

   @GetMapping("/resendVerificationToken")
    public ResponseEntity<String> resendVerificationToken(@RequestParam("email") String email) {

       User checkUser = userService.findByUserNameOrEmail(email);
       if (checkUser != null)
       {  if (!checkUser.isEnabled())
           {
               VerificationToken verificationToken = userService.generateNewVerificationToken(email);
               User user = verificationToken.getUser();
               String url= "http://localhost:3000/docplus.in/verify-registration/"+verificationToken.getToken();
               SendEmailModel mail=new SendEmailModel();
               mail.setEmailRecipient(user.getEmail());
               mail.setEmailSubject("Verifying the Registration");
               mail.setEmailMessage("Greetings! Hello "+user.getFirstName()+" "+user.getLastName()+". Please click the link to verify your account: "+url);
               log.info("Greetings! Hello "+user.getFirstName()+" "+user.getLastName()+". Please click the link to verify your account: "+url);
               //String mailStatus=userService.sendSimpleMail(mail);
               return new ResponseEntity<>("Verification Link Sent", HttpStatus.OK);
           }
           else
           {
               return new ResponseEntity<>("Already Verified", HttpStatus.CONFLICT);
           }
       }
       else
       {
           return new ResponseEntity<>("Please enter the registered email Id to get the verification link ", HttpStatus.BAD_REQUEST);
       }
   }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginModel loginModel)
    {
        User user=userService.findByUserNameOrEmail(loginModel.getUserNameOrEmail());
        if (user.isEnabled())
        {
            JwtAuthResponse jwtAuthResponse=userService.userLogin(loginModel);
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        }
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordModel passwordModel)
    {
        User user=userService.findByUserNameOrEmail(passwordModel.getUserNameOrEmail());
        if(user!=null)
        {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetToken(passwordModel.getUserNameOrEmail(), token,user);
            String url= "http://localhost:3000/docplus.in/reset-password/"+token;
            SendEmailModel mail=new SendEmailModel();
            mail.setEmailRecipient(user.getEmail());
            mail.setEmailSubject("Reset Password");
            mail.setEmailMessage("Click the link to reset your password: "+url);
            log.info("Click the link to reset your password: "+url);
            String mailStatus=userService.sendSimpleMail(mail);
            return new ResponseEntity<>("Password reset link has been sent", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel)
    {
        String result=userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid"))
        {
            return new ResponseEntity<>("Invalid Token",HttpStatus.CONFLICT);
        }
        Optional<User> user=userService.getUserByPasswordResetToken(token);
        if(user.isPresent())
        {
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return new ResponseEntity<>("Password Reset Successful",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Invalid Token",HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordModel passwordModel)
    {
       User user=userService.findByUserNameOrEmail(passwordModel.getUserNameOrEmail());
       if(!userService.checkIfValidOldPassword(user,passwordModel.getOldPassword()))
       {
           return new ResponseEntity<>("Invalid Old Password",HttpStatus.CONFLICT);
       }
       //Save New Password
        userService.changePassword(user,passwordModel.getNewPassword());
       return new ResponseEntity<>("Password changed successfully",HttpStatus.ACCEPTED);
    }
}
