package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.Appointment;
import OnlineDoctorAppointmentSystem.Entity.AppointmentDetails;
import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Entity.User;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.AppointmentModel;
import OnlineDoctorAppointmentSystem.Model.SendEmailModel;
import OnlineDoctorAppointmentSystem.Repository.AppointmentDetailRepository;
import OnlineDoctorAppointmentSystem.Repository.DoctorRepository;
import OnlineDoctorAppointmentSystem.Repository.UserRepository;
import OnlineDoctorAppointmentSystem.Service.AppointmentService;
import OnlineDoctorAppointmentSystem.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/docplus.in")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentDetailRepository appointmentDetailRepository;
    //Booking a appointment
    @PostMapping("/book-appointment")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentModel appointmentModel)
    {
        Appointment appointment=appointmentService.addAppointment(appointmentModel);
        if(appointment==null)
        {
            return new ResponseEntity<>("Booking Failed", HttpStatus.NOT_FOUND);
        }
        else
        {
            User user=userRepository.findById(appointment.getUserId())
                    .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
            Doctor doctor=doctorRepository.findById(appointment.getDoctorId())
                    .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
            AppointmentDetails appointmentDetails=appointmentDetailRepository.findById(appointment.getAppointmentId())
                    .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
            //Mail for User
            SendEmailModel mail1 = new SendEmailModel();
            mail1.setEmailRecipient(user.getEmail());
            mail1.setEmailSubject("Appointment Details");
            mail1.setEmailMessage("Hello "+user.getFirstName()+" "+user.getLastName()+
                    "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                    " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                    appointment.getSlot()+"-"+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
            //userService.sendSimpleMail(mail1);
            log.info("Appointment Details: " +"Hello "+user.getFirstName()+" "+user.getLastName()+
                    "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                    " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                    appointment.getSlot()+"-"+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
            //Mail to address entered in appointment form
            if(!user.getEmail().equals(appointmentDetails.getPatientEmail())) {
                SendEmailModel mail2 = new SendEmailModel();
                mail2.setEmailRecipient(appointmentDetails.getPatientEmail());
                mail2.setEmailSubject("Appointment Details");
                mail2.setEmailMessage("Hello " + user.getFirstName() + " " + user.getLastName() +
                        "! Your appointment Status with Dr." + doctor.getFirstName() + " " + doctor.getLastName() +
                        " for Patient Name: " + appointmentDetails.getPatientFirstName() + " " + appointmentDetails.getPatientLastName() +
                        " scheduled on Date: " + appointment.getSlotDate() + ", " + "Time: " +
                        appointment.getSlot() + "-" + appointment.getSlotTime() + " is " + appointment.getStatus() + " !");
               // userService.sendSimpleMail(mail2);
            }
            //Appointment Request Mail for Doctor
            SendEmailModel mail3 = new SendEmailModel();
            mail3.setEmailRecipient(appointmentDetails.getDoctorEmail());
            mail3.setEmailSubject("Appointment Request");
            mail3.setEmailMessage("Hello "+"Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " ! You have a new appointment request scheduled on Date: "+appointment.getSlotDate()+
                    ", "+ "Time: "+appointment.getSlot()+" - "+appointment.getSlotTime()+" for "+appointmentDetails.getPatientVisitReason()+
                    " checkup and it is waiting for your approval.");
            //userService.sendSimpleMail(mail3);
            log.info("Appointment Request:"+" Hello "+"Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " ! You have a new appointment request scheduled on Date: "+appointment.getSlotDate()+
                    ", "+ "Time: "+appointment.getSlot()+" - "+appointment.getSlotTime()+" for "+appointmentDetails.getPatientVisitReason()+
                    " Checkup is waiting for your approval.");
            return new ResponseEntity<>("Booked Successfully", HttpStatus.CREATED);
        }
    }

    //Listing all Appointments
    @GetMapping("/all-appointments")
    public List<AppointmentDetails> getAllAppointment()
    {
        List<AppointmentDetails> appointmentList=appointmentService.getAllAppointment();
        return appointmentList;
    }

    //Get particular appointment by passing appointment details
    @GetMapping("/appointment/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable ("id") Long appointmentId)
    {
        Appointment appointment=appointmentService.getAppointment(appointmentId);
        return new ResponseEntity<>(appointment,HttpStatus.FOUND);
    }

    //Update apppointment
    @PutMapping("/appointment/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable ("id") Long appointmentId,@RequestBody AppointmentModel appointmentModel)
    {
        Appointment appointment = appointmentService.updateAppointment(appointmentId,appointmentModel);
        User user=userRepository.findById(appointment.getUserId())
                .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
        AppointmentDetails appointmentDetails=appointmentDetailRepository.findById(appointment.getAppointmentId())
                .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
        Doctor doctor=doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(()->new NoResourceException("Not found",HttpStatus.NOT_FOUND));
        if(appointment!=null) {
            SendEmailModel mail1 = new SendEmailModel();
            //Appointment update mail for user
            mail1.setEmailRecipient(user.getEmail());
            mail1.setEmailSubject("Appointment Status Update");
            mail1.setEmailMessage("Hello "+user.getFirstName()+" "+user.getLastName()+
                    "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                    " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                    appointment.getSlot()+"-"+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
            //userService.sendSimpleMail(mail1);
            log.info("Appointment Status Update: " +"Hello "+user.getFirstName()+" "+user.getLastName()+
                    "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                    " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                    " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                    appointment.getSlot()+"-"+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
            //Appointment Update Mail for Patient
            if(!user.getEmail().equals(appointmentDetails.getPatientEmail())) {
                SendEmailModel mail2 = new SendEmailModel();
                mail2.setEmailRecipient(appointmentDetails.getPatientEmail());
                mail2.setEmailSubject("Appointment Status Update");
                mail2.setEmailMessage("Hello "+user.getFirstName()+" "+user.getLastName()+
                        "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                        " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                        " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                        appointment.getSlot()+" - "+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
                //userService.sendSimpleMail(mail2);
                log.info("Appointment Status Update: " +"Hello "+user.getFirstName()+" "+user.getLastName()+
                        "! Your appointment Status with Dr."+doctor.getFirstName()+" "+doctor.getLastName() +
                        " for Patient Name: "+appointmentDetails.getPatientFirstName()+" "+appointmentDetails.getPatientLastName()+
                        " scheduled on Date: "+appointment.getSlotDate()+", "+ "Time: "+
                        appointment.getSlot()+" - "+appointment.getSlotTime()+" is "+appointment.getStatus()+" !");
            }
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        }
       else
            return new ResponseEntity<>("No appointment exists with the entered ID", HttpStatus.NOT_FOUND);
    }

    //Delete appointment
    @DeleteMapping("/appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable ("id") Long appointmentId)
    {
       String check= appointmentService.deleteAppointment(appointmentId);
        if (check.equalsIgnoreCase("Deleted"))
            return new ResponseEntity<>("Appointment deleted Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("No appointment exists with the entered ID", HttpStatus.NOT_FOUND);
    }
}
