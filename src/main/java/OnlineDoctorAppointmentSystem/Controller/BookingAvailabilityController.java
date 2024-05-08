package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.BookingAvailability;
import OnlineDoctorAppointmentSystem.Model.SlotModel;
import OnlineDoctorAppointmentSystem.Service.BookingAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@CrossOrigin("*")
@RestController
@RequestMapping("/docplus.in")
public class BookingAvailabilityController {
    @Autowired
    BookingAvailabilityService bookingAvailabilityService;
    @PostMapping("/check-availability")
    public HashSet<String> checkAvailability(@RequestBody SlotModel slotModel)
    {
        HashSet<String> booking=bookingAvailabilityService.checkAvailability(slotModel);
        return booking;
    }
}
