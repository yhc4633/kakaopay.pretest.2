package com.kakaopay.pretest.model;

import lombok.Data;

@Data
public abstract class AbstractResponse {
    protected final ResponseHeader responseHeader;
    protected int resultCode;
}