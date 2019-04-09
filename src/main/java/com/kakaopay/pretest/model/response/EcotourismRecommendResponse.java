package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import lombok.Data;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class EcotourismRecommendResponse extends AbstractResponse {
    private String ecotourismCode;

    public EcotourismRecommendResponse(ResponseHeader responseHeader, Ecotourism ecotourism) {
        super(responseHeader);

        if (ecotourism != null) {
            this.ecotourismCode = ecotourism.getPublicIdentifyCode();
        }

        this.resultCode = SUCCESS.getResultCode();
    }
}