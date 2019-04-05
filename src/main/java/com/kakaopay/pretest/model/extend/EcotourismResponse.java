package com.kakaopay.pretest.model.extend;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;

@Data
public class EcotourismResponse extends AbstractResponse {


    public EcotourismResponse(ResponseHeader responseHeader) {
        super(responseHeader);
    }
}
