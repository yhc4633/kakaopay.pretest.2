package com.kakaopay.pretest.model.extend;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.kakaopay.pretest.constants.ResultCode.*;

@Data
public class ProcessResultResponse extends AbstractResponse {
    private List<String> failedList;

    public ProcessResultResponse(ResponseHeader responseHeader, List<String> failedList) {
        super(responseHeader);
        this.failedList = failedList;
        if (failedList == null) {
            this.resultCode = ERROR_NO_DATA;
        } else if (CollectionUtils.isNotEmpty(failedList)) {
            this.resultCode = ERROR_EXIST_FAILED_DATA;
        } else {
            this.resultCode = SUCCESS;
        }
    }
}