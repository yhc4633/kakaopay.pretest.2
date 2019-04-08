package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class SignResponse extends AbstractResponse {
    private String token;

    public SignResponse(ResponseHeader responseHeader, String token) {
        super(responseHeader);
        this.token = token;

        if (token == null) {
            this.resultCode = ERROR_WRONG_PARAMETER.getResultCode();
        } else {
            this.resultCode = SUCCESS.getResultCode();
        }
    }
}