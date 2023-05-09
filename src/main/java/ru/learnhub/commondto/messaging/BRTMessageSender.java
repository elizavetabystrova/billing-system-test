package ru.learnhub.commondto.messaging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.learnhub.commondto.dto.CallDataRecord;

import java.util.List;

@Service
@Slf4j
public class BRTMessageSender {
    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    public BRTMessageSender(JmsTemplate jmsTemplate, @Value("${cdr.mq}") String cdrMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void sendMessage(List<CallDataRecord> records) {
        log.info("Send CallDataRecord to BRT-service");
        jmsTemplate.convertAndSend(cdrMq, records);
    }
}
