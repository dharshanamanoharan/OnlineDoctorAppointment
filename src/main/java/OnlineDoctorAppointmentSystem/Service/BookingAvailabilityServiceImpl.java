package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.BookingAvailability;
import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.SlotModel;
import OnlineDoctorAppointmentSystem.Repository.BookingAvailabilityRepository;
import OnlineDoctorAppointmentSystem.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;

@Service
public class BookingAvailabilityServiceImpl implements BookingAvailabilityService {
    @Autowired
    BookingAvailabilityRepository bookingAvailabilityRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Override
    public HashSet<String> checkAvailability(SlotModel slotModel) {
        Doctor doctor=doctorRepository.findById(slotModel.getDoctorId())
                .orElseThrow(()->new NoResourceException("No Doctor Exists!", HttpStatus.NOT_FOUND));
        BookingAvailability booking=bookingAvailabilityRepository.findByDoctorId(doctor);
        HashSet<String> slots=booking.getBookingSlotAvailability().get(slotModel.getMonth()).get(slotModel.getDay() );
        return slots;
    }
}
