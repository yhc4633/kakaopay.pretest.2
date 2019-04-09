package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;

@Data
public class ProcessResultResponse extends AbstractResponse {
    public ProcessResultResponse(ResponseHeader responseHeader, int resultCode) {
        super(responseHeader);
        this.resultCode = resultCode;
    }
}