package ru.learnhub.commondto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CallDataRecordPlus implements Serializable {

    private final CallDataRecord callDataRecord;
    private final String tariffId;
}
