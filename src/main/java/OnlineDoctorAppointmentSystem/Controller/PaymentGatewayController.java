package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.Doctor;
import OnlineDoctorAppointmentSystem.Entity.User;
import OnlineDoctorAppointmentSystem.Exception.NoResourceException;
import OnlineDoctorAppointmentSystem.Model.SendEmailModel;
import OnlineDoctorAppointmentSystem.Repository.DoctorRepository;
import OnlineDoctorAppointmentSystem.Repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/docplus.in")
public class PaymentGatewayController {
    @Value("${rzp_key_id}")
    private String keyId;

    @Value("${rzp_key_secret}")
    private String secret;

    @GetMapping("/payment/{amount}")
    public String Payment(@PathVariable("amount") Double amount) throws RazorpayException {

        RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_11");
        Order order = razorpayClient.orders.create(orderRequest);
        String orderId = order.get("id");

        return orderId;
    }

}
