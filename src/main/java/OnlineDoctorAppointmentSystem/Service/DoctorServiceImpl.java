package OnlineDoctorAppointmentSystem.Service;
import OnlineDoctorAppointmentSystem.Entity.BookingAvailability;
import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.DoctorModel;
import OnlineDoctorAppointmentSystem.Repository.BookingAvailabilityRepository;
import OnlineDoctorAppointmentSystem.Repository.DoctorRepository;
import OnlineDoctorAppointmentSystem.Security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private BookingAvailabilityRepository bookingAvailabilityRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }
    @Override
    public Doctor addDoctor(DoctorModel doctorModel) {
        Doctor doctor=new Doctor();
        doctor.setFirstName(doctorModel.getFirstName());
        doctor.setLastName(doctorModel.getLastName());
        doctor.setUserName(doctorModel.getUserName());
        doctor.setEmail((doctorModel.getEmail()));
        //doctor.setPassword(passwordEncoder.encode(doctorModel.getPassword()));
        doctor.setGender(doctorModel.getGender());
        doctor.setSpecialization(doctorModel.getSpecialization());
        doctor.setDescription(doctorModel.getDescription());
        doctor.setPhoneNumber(doctorModel.getPhoneNumber());
        doctor.setAddress(doctorModel.getAddress());
        doctor.setLocation(doctorModel.getLocation());
        doctor.setImage(doctorModel.getImage());
        doctor.setFees(doctorModel.getFees());
        doctor.setAvailability(doctorModel.getAvailability());
        doctorRepository.save(doctor);
        BookingAvailability bookedDoctor=new BookingAvailability(doctor);
        bookingAvailabilityRepository.save(bookedDoctor);
        return doctor;
    }

    @Override
    public Doctor getDoctor(Long docId) {
        Doctor doctor=doctorRepository.findById(docId).orElseThrow
                (()->new NoResourceException("Doctor not Found"+docId, HttpStatus.NOT_FOUND));
        return doctor;
    }


    @Override
    public Doctor updateDoctorById(Long docId, DoctorModel doctorModel) {
       Doctor doctor=doctorRepository.findById(docId).orElseThrow
                (()->new NoResourceException("Doctor not Found"+docId, HttpStatus.NOT_FOUND));
        if(doctorModel.getFirstName()!=null)
        {
            doctor.setFirstName(doctorModel.getFirstName());
        }
        if(doctorModel.getLastName()!=null)
        {
            doctor.setLastName(doctorModel.getLastName());
        }
        if(doctorModel.getEmail()!=null)
        {
            doctor.setEmail((doctorModel.getEmail()));
        }
        if(doctorModel.getGender()!=null)
        {
            doctor.setGender(doctorModel.getGender());
        }
        if(doctorModel.getSpecialization()!=null)
        {
            doctor.setSpecialization(doctorModel.getSpecialization());
        }
        if(doctorModel.getDescription()!=null)
        {
            doctor.setDescription(doctorModel.getDescription());
        }
        if(doctorModel.getPhoneNumber()!=null)
        {
            doctor.setPhoneNumber(doctorModel.getPhoneNumber());
        }
        if(doctorModel.getImage()!=null)
        {
            doctor.setImage(doctorModel.getImage());
        }
        if(!(doctorModel.getFees()<=0))
        {
            doctor.setFees(doctorModel.getFees());
        }
        doctorRepository.save(doctor);
        return doctor;
    }

    @Transactional
    @Override
    public String deleteDoctor(Long docId) {
        Doctor doctor=doctorRepository.findById(docId).orElseThrow
                (()->new NoResourceException("Doctor not Found"+docId, HttpStatus.NOT_FOUND));
        if(doctor!=null)
        {
            bookingAvailabilityRepository.deleteById(docId);
            doctorRepository.deleteById(docId);
            return "Deleted";
        }
        else{
            return null;
        }
    }

    /*@Override
    public Doctor findByUserNameOrEmail(String userNameOrEmail) throws UsernameNotFoundException {
       Doctor doctor=doctorRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail)
                .orElseThrow(() -> new NoResourceException("Doctor Not Found! No user exists with this name or emailID",HttpStatus.NOT_FOUND));
        return doctor;
    }*/

    /*@Override
    public JwtAuthResponse docLogin(LoginModel loginModel) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginModel.getUserNameOrEmail(),
                loginModel.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtTokenProvider.generateJwtToken(authentication);
        Optional<Doctor> doctorOptional = doctorRepository.findByUserNameOrEmail(loginModel.getUserNameOrEmail(),
                loginModel.getUserNameOrEmail());

        String role = null;
        if(doctorOptional.isPresent()) {
            Doctor loggedInDoctor = doctorOptional.get();
            Optional<Role> optionalRole = loggedInDoctor.getRoles().stream().findFirst();

            if (optionalRole.isPresent()) {
                Role doctorRole = optionalRole.get();
                role = doctorRole.getName();
            }
        }
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setRole(role);
        jwtAuthResponse.setAccessToken(token);
        return jwtAuthResponse;
    }*/


}
