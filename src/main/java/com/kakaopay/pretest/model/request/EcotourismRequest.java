package com.kakaopay.pretest.model.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class EcotourismRequest {
    private String ecotourismCode;
    private String programName;
    private String theme;
    private String region;
    private String programIntro;
    private String programDetail;

    public String[] createTourInfoArr() {
        String[] tourInfoArr = {StringUtils.defaultString(getEcotourismCode()), StringUtils.defaultString(getProgramName())
                , StringUtils.defaultString(getTheme()), StringUtils.defaultString(getRegion())
                , StringUtils.defaultString(getProgramIntro()), StringUtils.defaultString(getProgramDetail())};

        return tourInfoArr;
    }
}