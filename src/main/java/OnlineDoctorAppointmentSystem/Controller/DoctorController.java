package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Model.DoctorModel;
import OnlineDoctorAppointmentSystem.Model.LoginModel;
import OnlineDoctorAppointmentSystem.Security.JwtAuthResponse;
import OnlineDoctorAppointmentSystem.Service.DoctorService;
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
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    //Getting All Doctors
    @GetMapping("/doctors-list")
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorsList=doctorService.getAllDoctors();
        return doctorsList;
    }

    //Adding Doctor
    @PostMapping("/add-doctor")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorModel doctorModel) {
        Doctor doctor = doctorService.addDoctor(doctorModel);
        return new ResponseEntity<>("Doctor added Successfully", HttpStatus.CREATED);
    }

    //Getting Doctor by Id
    @GetMapping("/doctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") Long docId) {
        Doctor doctor = doctorService.getDoctor(docId);
        if (doctor != null)
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //Updating particular Doctor by Id
    @PutMapping("/doctor/{id}")
    public ResponseEntity<String> updateDoctorById(@PathVariable("id") Long docId,@RequestBody DoctorModel doctorModel) {
        Doctor doctor = doctorService.updateDoctorById(docId,doctorModel);
        if (doctor != null)
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("No Doctor exists with the entered ID", HttpStatus.NOT_FOUND);
    }

    //Deleting particular Doctor by Id
    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable("id") Long docId) {
        String check = doctorService.deleteDoctor(docId);
        if (check.equalsIgnoreCase("Deleted"))
            return new ResponseEntity<>("Doctor Deleted Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("No Doctor exists with the entered ID", HttpStatus.NOT_FOUND);
    }


    //Login for Doctors
    /*@PostMapping("/doc-login")
    public ResponseEntity<JwtAuthResponse> docLogin(@RequestBody LoginModel loginModel)
    {
        Doctor doctor=doctorService.findByUserNameOrEmail(loginModel.getUserNameOrEmail());

            JwtAuthResponse jwtAuthResponse=doctorService.docLogin(loginModel);
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }*/

}