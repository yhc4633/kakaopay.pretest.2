package com.kakaopay.pretest.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    FAIL(1, "FAIL"),
    ERROR_WRONG_PARAMETER(5001, "ERROR_WRONG_PARAMETER"),
    ERROR_NO_DATA(10001, "ERROR_NO_DATA"),
    ERROR_EXIST_FAILED_DATA(10002, "ERROR_EXIST_FAILED_DATA");

    @Getter
    private int resultCode;
    @Getter
    private String resultMessage;

    ResponseCode(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public static String getResultMessageByResultCode(int resultCode) {
        for (ResponseCode responseCode : ResponseCode.values()) {
            if (responseCode.resultCode == resultCode) {
                return responseCode.resultMessage;
            }
        }

        return StringUtils.EMPTY;
    }
}