package ru.learnhub.commondto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CallDataRecord implements Serializable {
    private final CallType callType;
    private final String phoneNumber;
    private final LocalDateTime callStartTime;
    private final LocalDateTime callEndTime;
}
