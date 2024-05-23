package OnlineDoctorAppointmentSystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@Entity
@Table (name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="First Name")
    private String firstName;
    @Column(name="Last Name")
    private String lastName;
    @Column(name="User Name",unique = true)
    private String userName;
    @Column(name="Email ID",unique = true)
    private String email;
    @Column(name="Password",length = 60)
    private String password;
    @Column(name="enabled")
    private boolean enabled=false;
    @Column
    String role="ROLE_USER";
    @Column
    String user_avatar;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] displayPicture;

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))*/
    @Lob
    @Column(columnDefinition = "longblob")
    private Set<String> roles;
}
