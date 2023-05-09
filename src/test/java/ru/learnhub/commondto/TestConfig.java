package ru.learnhub.commondto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import ru.learnhub.commondto.dto.CallDataRecordPlus;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ContextConfiguration
@Slf4j
public abstract class TestConfig {
    @Component
    protected static class BRTMessageListener {

        private final BlockingQueue<List<CallDataRecordPlus>> payload = new ArrayBlockingQueue<>(10);

        @JmsListener(destination = "${cdrplus.mq}")
        public void receiveData(@Payload List<CallDataRecordPlus> cdrList) {
            log.info("Receive CallDataRecords from BRT service");
            payload.add(cdrList);
        }

        public BlockingQueue<List<CallDataRecordPlus>> getPayload() {
            return payload;
        }
    }
}
