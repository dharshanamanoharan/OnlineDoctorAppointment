package OnlineDoctorAppointmentSystem.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="doctors")

public class Doctor {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email",nullable = false,unique = true)
    private String email;
    @Column(name="User Name",unique = true)
    private String userName;
    @Column(name="Password",length = 60)
    private String password;
    @Column(name="gender")
    private String gender;
    @Column(name="specialization")
    private String specialization;
    @Column(name="description")
    private String description;
    @Column(name="address")
    private String address;
    @Column(name="location")
    private String location;
    @Column(name="phone_no")
    private String phoneNumber;
    @Column(name="doc_avatar")
    private String image;
    @Column(name="availability")
    private String availability;
    @Column(name="fees")
    private int fees;
    /*private Set<Role> roles;*/
}


