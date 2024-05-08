package OnlineDoctorAppointmentSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailModel {
    private String emailRecipient;
    private String emailMessage;
    private String emailSubject;

}
