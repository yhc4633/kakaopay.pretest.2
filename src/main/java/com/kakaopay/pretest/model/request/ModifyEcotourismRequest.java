package com.kakaopay.pretest.model.request;

import lombok.Data;

@Data
public class ModifyEcotourismRequest {
    private String ecotourismCode;
    private String programName;
    private String theme;
    private String region;
    private String programIntro;
    private String programDetail;
}