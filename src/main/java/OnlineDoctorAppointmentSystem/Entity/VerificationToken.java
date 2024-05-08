package OnlineDoctorAppointmentSystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String email;
    private Date expirationTime;
    //Expiration duration as 10 minutes
    private static final int expirationDuration=10;
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "User ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;
    public VerificationToken(User user,String token)
    {
        super();
        this.token=token;
        this.user=user;
        this.email= user.getEmail();
        this.expirationTime=calculateExpirationDate(expirationDuration);
    }
    public VerificationToken(String token)
    {
        super();
        this.token=token;
        this.expirationTime=calculateExpirationDate(expirationDuration);
    }

    private Date calculateExpirationDate(int expirationTime){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
