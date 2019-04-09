package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class FrequentEcotourismProgramDetailResponse extends AbstractResponse {
    private String programDetailKeyword;
    private Integer count;

    public FrequentEcotourismProgramDetailResponse(ResponseHeader responseHeader, String programDetailKeyword, Integer count) {
        super(responseHeader);
        this.programDetailKeyword = programDetailKeyword;
        if (count == null) {
            this.resultCode = ERROR_WRONG_PARAMETER.getResultCode();
            return;
        }

        this.count = count;

        this.resultCode = SUCCESS.getResultCode();
    }
}