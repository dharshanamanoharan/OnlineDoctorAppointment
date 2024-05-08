package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.User;
import OnlineDoctorAppointmentSystem.Model.UserModel;
import OnlineDoctorAppointmentSystem.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/docplus.in")
public class UserController {
        @Autowired
        private UserService userService;
        //Getting All Users
        @GetMapping("/users-list")
        public List<User> getAllUsers() {
            List<User> usersList=userService.getAllUsers();
            return usersList;
        }

        //Adding User
        @PostMapping("/add-user")
        public ResponseEntity<String> addUser(@RequestBody UserModel userModel) {
            User user = userService.registerUser(userModel);
            return new ResponseEntity<>("User added Successfully", HttpStatus.CREATED);
        }

        //Getting User by Id
        @GetMapping("/user/{id}")
        public ResponseEntity<UserModel> getUserById(@PathVariable("id") Long userId) {
            UserModel user = userService.getUser(userId);
            if (user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //Updating particular User by Id
        @PutMapping("/user/{id}")
        public ResponseEntity<String> updateUserById(@PathVariable("id") Long userId,@RequestBody UserModel userModel) {
           User user = userService.updateUserById(userId,userModel);
            if (user != null)
                return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
            else
                return new ResponseEntity<>("No User exists with the entered ID", HttpStatus.NOT_FOUND);
        }

        //Deleting particular Doctor by Id
        @DeleteMapping("/user/{id}")
        public ResponseEntity<String> deleteUserById(@PathVariable("id") Long userId) {
            String check = userService.deleteUser(userId);
            if (check.equalsIgnoreCase("Deleted"))
                return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
            else
                return new ResponseEntity<>("No User exists with the entered ID", HttpStatus.NOT_FOUND);
        }


    @PostMapping("/user")
    public ResponseEntity<User> loggeduser(@RequestParam("userNameOrEmail") String userNameOrEmail){
            User user=userService.findByUserNameOrEmail(userNameOrEmail);
            return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
