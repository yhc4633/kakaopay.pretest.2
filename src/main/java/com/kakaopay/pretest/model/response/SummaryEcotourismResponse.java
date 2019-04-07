package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Theme;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kakaopay.pretest.constants.ParameterCode.SEPARATOR_COMMA;
import static com.kakaopay.pretest.constants.ResponseCode.ERROR_WRONG_PARAMETER;
import static com.kakaopay.pretest.constants.ResponseCode.SUCCESS;

@Data
public class SummaryEcotourismResponse extends AbstractResponse {
    List<SearchResult> resultList = new ArrayList<>();

    public SummaryEcotourismResponse(ResponseHeader responseHeader, List<Ecotourism> ecotourismList) {
        super(responseHeader);

        if (ecotourismList == null) {
            this.resultCode = ERROR_WRONG_PARAMETER.getResultCode();
            return;
        }

        for (Ecotourism ecotourism : ecotourismList) {
            resultList.add(new SearchResult(ecotourism));
        }

        this.resultCode = SUCCESS.getResultCode();
    }

    @Data
    @NoArgsConstructor
    private class SearchResult {
        private String region;
        private String programName;
        private String theme;

        public SearchResult(Ecotourism ecotourism) {
            this.region = ecotourism.getRegion().getPublicIdentifyCode();
            this.programName = ecotourism.getProgram().getName();
            this.theme = ecotourism.getThemeList().stream().map(Theme::getName).collect(Collectors.joining(SEPARATOR_COMMA));
        }
    }
}