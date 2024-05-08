package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.BookingAvailability;
import OnlineDoctorAppointmentSystem.Model.SlotModel;

import java.util.HashSet;

public interface BookingAvailabilityService {
    HashSet<String> checkAvailability(SlotModel slotModel);
}
