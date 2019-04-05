package com.kakaopay.pretest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Data
@AllArgsConstructor
public class ResponseHeader {
    private String transactionId;
    private int resultCode;
    private String resultMessage;

    public ResponseHeader(String transactionId, int resultCode) {
        this.transactionId = transactionId;
        this.resultCode = resultCode;
        this.resultMessage = resultCode >=0 ? SUCCESS : FAIL;
    }
}