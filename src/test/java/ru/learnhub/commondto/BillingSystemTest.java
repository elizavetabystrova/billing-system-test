package ru.learnhub.commondto;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.learnhub.commondto.dto.CallDataRecord;
import ru.learnhub.commondto.dto.CallDataRecordPlus;
import ru.learnhub.commondto.dto.CallType;
import ru.learnhub.commondto.service.BRTMessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Slf4j
class BillingSystemTest {

    @Autowired
    private BRTMessageService messageService;

    @Autowired
    private TestConfig.BRTMessageListener messageListener;

    @BeforeEach
    public void clear() {
        messageListener.getPayload().clear();
    }

    @Test
    void testGetCallDataRecordWithTariff() throws InterruptedException {
        LocalDateTime testDateTime = generateRandomDateTime();
        CallDataRecord callDataRecord1 = new CallDataRecord(CallType.OUTGOING, "75218801855",
                testDateTime, testDateTime.plusSeconds(10));
        CallDataRecord callDataRecord2 = new CallDataRecord(CallType.INCOMING, "75218801855",
                testDateTime, testDateTime.plusMinutes(12));
        List<CallDataRecord> testData = new ArrayList<>(List.of(callDataRecord1, callDataRecord2));

        messageService.sendCallData(testData);

        CallDataRecordPlus callDataRecordPlus1 = new CallDataRecordPlus(callDataRecord1, "11");
        CallDataRecordPlus callDataRecordPlus2 = new CallDataRecordPlus(callDataRecord2, "11");
        List<CallDataRecordPlus> expected = new ArrayList<>(List.of(callDataRecordPlus1,callDataRecordPlus2));
        List<CallDataRecordPlus> actual = messageListener.getPayload().poll(1, TimeUnit.SECONDS);
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCallDataRecordWithDifferentTariff() throws InterruptedException {
        LocalDateTime testDateTime = generateRandomDateTime();
        CallDataRecord callDataRecord1 = new CallDataRecord(CallType.INCOMING, "71916454074",
                testDateTime, testDateTime.plusSeconds(10));
        CallDataRecord callDataRecord2 = new CallDataRecord(CallType.OUTGOING, "71916454074",
                testDateTime, testDateTime.plusMinutes(2));
        CallDataRecord callDataRecord3 = new CallDataRecord(CallType.INCOMING, "71916454074",
                testDateTime, testDateTime.plusHours(2));

        List<CallDataRecord> testData = new ArrayList<>(List.of(callDataRecord1, callDataRecord2, callDataRecord3));
        messageService.sendCallData(testData);

        CallDataRecordPlus callDataRecordPlus1 = new CallDataRecordPlus(callDataRecord1, "03");
        CallDataRecordPlus callDataRecordPlus2 = new CallDataRecordPlus(callDataRecord2, "03");
        CallDataRecordPlus callDataRecordPlus3 = new CallDataRecordPlus(callDataRecord3, "03");
        List<CallDataRecordPlus> expected = new ArrayList<>(List.of(callDataRecordPlus1, callDataRecordPlus2, callDataRecordPlus3));
        List<CallDataRecordPlus> actual = messageListener.getPayload().poll(1, TimeUnit.SECONDS);
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCallDataRecordWithBalanceBelowZero() throws InterruptedException {
        LocalDateTime testDateTime = generateRandomDateTime();
        CallDataRecord callDataRecord = new CallDataRecord(CallType.INCOMING, "71918349678",
                testDateTime, testDateTime.plusMinutes(5));
        List<CallDataRecord> testData = new ArrayList<>(List.of(callDataRecord));
        messageService.sendCallData(testData);


        List<CallDataRecordPlus> actual = messageListener.getPayload().poll(1, TimeUnit.SECONDS);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testGetCallDataRecordWithTariff06() throws InterruptedException {
        LocalDateTime testDateTime = generateRandomDateTime();
        CallDataRecord callDataRecord = new CallDataRecord(CallType.INCOMING, "75213085311",
                testDateTime, testDateTime.plusMinutes(8));
        List<CallDataRecord> testData = new ArrayList<>(List.of(callDataRecord));
        messageService.sendCallData(testData);

        CallDataRecordPlus callDataRecordPlus = new CallDataRecordPlus(callDataRecord, "06");
        List<CallDataRecordPlus> expected = new ArrayList<>(List.of(callDataRecordPlus));
        List<CallDataRecordPlus> actual = messageListener.getPayload().poll(1, TimeUnit.SECONDS);
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    private LocalDateTime generateRandomDateTime() {
        SplittableRandom splittableRandom = new SplittableRandom();
        int randomHours = splittableRandom.nextInt(23);
        int randomMinutes = splittableRandom.nextInt(59);
        int randomSeconds = splittableRandom.nextInt(59);
        return LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(),
                randomHours, randomMinutes, randomSeconds);
    }
}
