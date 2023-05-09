package ru.learnhub.commondto.service;

import org.springframework.stereotype.Service;
import ru.learnhub.commondto.dto.CallDataRecord;
import ru.learnhub.commondto.messaging.BRTMessageSender;

import java.util.List;

@Service
public class BRTMessageService {

    private final BRTMessageSender messageSender;

    public BRTMessageService(BRTMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void sendCallData(List<CallDataRecord> cdrList) {
        messageSender.sendMessage(cdrList);
    }
}
