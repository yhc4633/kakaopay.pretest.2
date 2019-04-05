package com.kakaopay.pretest.model.extend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;

@Data
public class ProcessResultResponse extends AbstractResponse {
    @JsonProperty("isSuccessful")
    private boolean successful;

    public ProcessResultResponse(ResponseHeader responseHeader, boolean isSuccessful) {
        super(responseHeader);
        this.successful = isSuccessful;
    }
}