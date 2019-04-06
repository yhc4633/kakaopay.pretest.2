package com.kakaopay.pretest.model.extend;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class RegisterFileResponse extends AbstractResponse {
    private List<String> failedList;

    public RegisterFileResponse(ResponseHeader responseHeader, List<String> failedList) {
        super(responseHeader);
        this.failedList = failedList;
        if (failedList == null) {
            this.resultCode = ERROR_NO_DATA.getResultCode();
        } else if (CollectionUtils.isNotEmpty(failedList)) {
            this.resultCode = ERROR_EXIST_FAILED_DATA.getResultCode();
        } else {
            this.resultCode = SUCCESS.getResultCode();
        }
    }
}