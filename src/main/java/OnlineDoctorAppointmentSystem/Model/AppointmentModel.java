package OnlineDoctorAppointmentSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentModel {
    private Long userId;
    private Long appointmentId;
    private Long doctorId;
    private String slot;
    private String status="pending for approval";
    private int day;
    private int month;
    private int year;
    private String slotTime;
    private String slotDate;
    //Complete Appointment Detail
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private String patientContactNumber;
    private String patientVisitReason;
    private String patientGender;
    private String patientAge;
    private String paymentID;
    private String amountPaid;
    private String orderID;
}

