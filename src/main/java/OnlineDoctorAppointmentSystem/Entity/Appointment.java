package OnlineDoctorAppointmentSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private Long userId;
    private Long doctorId;
    private String slot;
    private String status="pending for approval";
    private String paymentID;
    private String amountPaid;
    private String orderID;
    private int day;
    private int month;
    private int year;
    private String slotTime;
    private String slotDate;
    public Appointment(Long doctorId,Long userId,String slot,int day,int month)
    {
        this.doctorId=doctorId;
        this.userId=userId;
        this.slot=slot;
        this.day=day;
        this.month=month;
    }

}
