package OnlineDoctorAppointmentSystem.Controller;

import OnlineDoctorAppointmentSystem.Entity.DocplusMessage;
import OnlineDoctorAppointmentSystem.Service.DocplusMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/docplus.in")
public class DocplusMessageController {
    @Autowired
    DocplusMessageService docplusMessageService;

    //Saving the message from sender in database
    @PostMapping("/storeMessage")
    public ResponseEntity<String> storeMessage(@RequestBody DocplusMessage docplusMessage)
    {
        String result=docplusMessageService.storeMessage(docplusMessage);
        return new ResponseEntity<>("Stored", HttpStatus.CREATED);
    }

    //Getting the message from database and dispaying to admin
    @GetMapping("/retrieveMessage")
    public List<DocplusMessage> retrieveMessage()
    {
        List<DocplusMessage> messageList=docplusMessageService.getAllMessage();
        return messageList;
    }

}
