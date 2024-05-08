package OnlineDoctorAppointmentSystem.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotModel {
    private Long doctorId;
    private int month;
    private int day;
}
