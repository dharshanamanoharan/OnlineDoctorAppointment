package OnlineDoctorAppointmentSystem.Service;

import OnlineDoctorAppointmentSystem.Entity.DocplusMessage;

import java.util.List;

public interface DocplusMessageService {
    String storeMessage(DocplusMessage docplusMessage);

    List<DocplusMessage> getAllMessage();
}
