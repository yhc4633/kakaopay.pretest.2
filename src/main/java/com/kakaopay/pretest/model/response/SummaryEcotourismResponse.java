package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        Map<String, List<Ecotourism>> tourKeyExceptThemeMap = ecotourismList.stream().collect(Collectors.groupingBy(ecotourism -> ecotourism.getTourKeyExceptTheme()));

        for (String tourKeyExceptTheme : tourKeyExceptThemeMap.keySet()) {
            SearchResult searchResult = null;

            for (Ecotourism ecotourism : tourKeyExceptThemeMap.get(tourKeyExceptTheme)) {
                if (searchResult == null) {
                    searchResult = new SearchResult(ecotourism);
                } else {
                    searchResult.addTheme(ecotourism.getTheme().getName());
                }
            }

            resultList.add(searchResult);
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
            this.theme = ecotourism.getTheme().getName();
        }

        public void addTheme(String theme) {
            this.theme = this.theme + SEPARATOR_COMMA + theme;
        }
    }
}