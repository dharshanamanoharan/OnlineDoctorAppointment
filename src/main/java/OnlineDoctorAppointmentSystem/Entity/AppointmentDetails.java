package OnlineDoctorAppointmentSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    //Details of the User who booked
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    //Details of the Doctor
    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorEmail;
    private String doctorContactNumber;
    private String doctorAddress;
    //Patient Details entered in the appointment form
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private String patientContactNumber;
    private String patientVisitReason;
    private String patientGender;
    private String patientAge;
    private String slotDate;
    private String slotTime;
    private String status ="pending for approval";
    private String paymentID;
    private String amountPaid;
    private String orderID;
}
