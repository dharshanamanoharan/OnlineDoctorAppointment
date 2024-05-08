package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.DocplusMessage;
import OnlineDoctorAppointmentSystem.Repository.DocplusMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocplusMessageServiceImpl implements  DocplusMessageService{
    @Autowired
    DocplusMessageRepository docplusMessageRepository;
    @Override
    public String storeMessage(DocplusMessage docplusMessage) {
        DocplusMessage docplusMessage1=new DocplusMessage();
        docplusMessage1.setSenderName(docplusMessage.getSenderName());
        docplusMessage1.setSenderEmail(docplusMessage.getSenderEmail());
        docplusMessage1.setSenderMessage(docplusMessage.getSenderMessage());
        docplusMessage1.setMsgTimeStamp(docplusMessage.getMsgTimeStamp());
        docplusMessageRepository.save(docplusMessage1);
        return "Message Stored!";
    }

    @Override
    public List<DocplusMessage> getAllMessage() {
        List<DocplusMessage> msgList=docplusMessageRepository.findAll();
        return msgList;
    }
}
