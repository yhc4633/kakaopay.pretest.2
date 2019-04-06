package com.kakaopay.pretest.model;

import com.kakaopay.pretest.constants.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
@AllArgsConstructor
public class ResponseHeader {
    private String transactionId;
    private int resultCode;
    private String resultMessage;

    public ResponseHeader(String transactionId, ResponseCode responseCode) {
        this(transactionId, responseCode.getResultCode());
    }

    public ResponseHeader(String transactionId, int resultCode) {
        this.transactionId = transactionId;
        this.resultCode = resultCode;
        this.resultMessage = getResultMessageByResultCode(resultCode);
    }
}