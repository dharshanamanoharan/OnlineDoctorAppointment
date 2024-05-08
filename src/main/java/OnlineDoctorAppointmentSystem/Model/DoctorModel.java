package OnlineDoctorAppointmentSystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorModel {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String gender;
    private String specialization;
    private String description;
    private String image;
    private String address;
    private String location;
    private String availability;
    private String phoneNumber;
    private int fees;
}
