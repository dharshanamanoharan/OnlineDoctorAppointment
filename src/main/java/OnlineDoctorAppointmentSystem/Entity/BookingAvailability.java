package OnlineDoctorAppointmentSystem.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.HashSet;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class BookingAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "longblob")
    HashMap<Integer,HashMap<Integer,HashSet<String>>> bookingSlotAvailability;


    public BookingAvailability(Doctor doctorId)
    {
        this.doctorId=doctorId;
        HashMap<Integer, HashMap<Integer, HashSet<String>>> bookingSlots = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            HashMap<Integer, HashSet<String>> dateTimeSlot= new HashMap<>();
            for (int day = 1; day <= 31; day++) {
                HashSet<String> slots = new HashSet<>();
                slots.add("slot1");
                slots.add("slot2");
                slots.add("slot3");
                slots.add("slot4");
                slots.add("slot5");
                slots.add("slot6");
                slots.add("slot7");
                slots.add("slot8");
                slots.add("slot9");
                slots.add("slot10");
                slots.add("slot11");
                slots.add("slot12");
                dateTimeSlot.put(day, slots);
            }
            bookingSlots.put(month, dateTimeSlot);
        }
        this.bookingSlotAvailability=bookingSlots;
    }

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "Doctor ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DOCTOR_ID"))
    private Doctor doctorId;
}


