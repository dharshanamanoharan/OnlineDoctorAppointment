package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.*;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.AppointmentModel;
import OnlineDoctorAppointmentSystem.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    BookingAvailabilityRepository bookingAvailabilityRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AppointmentDetailRepository appointmentDetailRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    UserRepository userRepository;

    //Adding appointment
    @Override
    public Appointment addAppointment(AppointmentModel appointmentModel) {
        //Check for availability
        Doctor doctor=doctorRepository.findById(appointmentModel.getDoctorId())
                .orElseThrow(()->new NoResourceException());
       User user=userRepository.findById(appointmentModel.getUserId())
                .orElseThrow(()->new NoResourceException());
        BookingAvailability availability=bookingAvailabilityRepository.findByDoctorId(doctor);
        HashSet checkSlot=availability.getBookingSlotAvailability()
                .get(appointmentModel.getMonth()).get(appointmentModel.getDay());
        boolean check=checkSlot.contains((appointmentModel.getSlot()));
        if(check){
            Appointment appointment=new Appointment();
            appointment.setUserId(appointmentModel.getUserId());
            appointment.setDoctorId(appointmentModel.getDoctorId());
            appointment.setDay(appointmentModel.getDay());
            appointment.setMonth(appointmentModel.getMonth());
            appointment.setYear(appointmentModel.getYear());
            appointment.setSlot(appointmentModel.getSlot());
            appointment.setSlotTime(appointmentModel.getSlotTime());
            appointment.setSlotDate(appointmentModel.getDay()+"-"+appointmentModel.getMonth()+"-"+appointmentModel.getYear());
            appointment.setAmountPaid(appointmentModel.getAmountPaid());
            appointment.setPaymentID(appointmentModel.getPaymentID());
            appointment.setOrderID(appointmentModel.getOrderID());
            appointmentRepository.save(appointment);

            //Saving Appoint Details with Patient Details in separate table
            AppointmentDetails appointmentDetails=new AppointmentDetails();
            appointmentDetails.setUserId(appointmentModel.getUserId());
            appointmentDetails.setUserFirstName(user.getFirstName());
            appointmentDetails.setUserLastName(user.getLastName());
            appointmentDetails.setUserEmail(user.getEmail());
            appointmentDetails.setDoctorId(appointmentModel.getDoctorId());
            appointmentDetails.setDoctorFirstName(doctor.getFirstName());
            appointmentDetails.setDoctorLastName(doctor.getLastName());
            appointmentDetails.setDoctorEmail(doctor.getEmail());
            appointmentDetails.setDoctorContactNumber(doctor.getPhoneNumber());
            appointmentDetails.setDoctorAddress(doctor.getAddress());
            appointmentDetails.setPatientFirstName(appointmentModel.getPatientFirstName());
            appointmentDetails.setPatientLastName(appointmentModel.getPatientLastName());
            appointmentDetails.setPatientEmail(appointmentModel.getPatientEmail());
            appointmentDetails.setPatientContactNumber(appointmentModel.getPatientContactNumber());
            appointmentDetails.setPatientVisitReason(appointmentModel.getPatientVisitReason());
            appointmentDetails.setPatientGender(appointmentModel.getPatientGender());
            appointmentDetails.setPatientAge(appointmentModel.getPatientAge());
            appointmentDetails.setSlotDate(appointmentModel.getDay()+"-"+appointmentModel.getMonth()+"-"+appointmentModel.getYear());
            appointmentDetails.setSlotTime(appointmentModel.getSlotTime());
            appointmentDetails.setAmountPaid(appointmentModel.getAmountPaid());
            appointmentDetails.setPaymentID(appointmentModel.getPaymentID());
            appointmentDetails.setOrderID(appointmentModel.getOrderID());
            appointmentDetailRepository.save(appointmentDetails);


            //Removing the booked slot from availabilty and updating it
            availability.getBookingSlotAvailability()
                    .get(appointmentModel.getMonth())
                    .get(appointmentModel.getDay()).remove(appointmentModel.getSlot());
            bookingAvailabilityRepository.save(availability);
            return appointment;
        }
        else
        {
            return null;
        }
    }

    //Get All Appointments
    @Override
    public List<AppointmentDetails> getAllAppointment() {
        List<AppointmentDetails> appointment=appointmentDetailRepository.findAll();
        return appointment;
    }

    //Getting particular appointment
    @Override
    public Appointment getAppointment(Long appointmentId) {
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new NoResourceException());
        return appointment;
    }

    //Updating appointment
    @Override
    public Appointment updateAppointment(Long appointmentId,AppointmentModel appointmentModel) {
        Appointment appointment=appointmentRepository.findById(appointmentId).orElseThrow
                (()->new NoResourceException("Appointment not Found"+appointmentId, HttpStatus.NOT_FOUND));
        appointment.setStatus(appointmentModel.getStatus());
        appointmentRepository.save(appointment);
        AppointmentDetails appointmentDetails=appointmentDetailRepository.findById(appointmentId).orElseThrow
                (()->new NoResourceException("Appointment not Found"+appointmentId, HttpStatus.NOT_FOUND));
        appointmentDetails.setStatus(appointmentModel.getStatus());
        appointmentDetailRepository.save(appointmentDetails);
        return appointment;
    }

    //Deleting appointment
    @Override
    public String deleteAppointment(Long appointmentId) {
        Appointment appointment=appointmentRepository.findById(appointmentId).orElseThrow
                (()->new NoResourceException("Appointment not Found"+appointmentId, HttpStatus.NOT_FOUND));
        if(appointment!=null) {
            appointmentRepository.deleteById(appointmentId);
            appointmentDetailRepository.deleteById(appointmentId);
            return "Deleted";
        }
        return null;
    }
}
