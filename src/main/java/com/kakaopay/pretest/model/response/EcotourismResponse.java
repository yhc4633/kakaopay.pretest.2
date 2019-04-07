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
import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class EcotourismResponse extends AbstractResponse {
    List<SearchResult> resultList = new ArrayList<>();

    public EcotourismResponse(ResponseHeader responseHeader, List<Ecotourism> ecotourismList) {
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
        private String ecotourismCode;
        private String programName;
        private String theme;
        private String region;
        private String programIntro;
        private String programDetail;

        public SearchResult(Ecotourism ecotourism) {
            this.ecotourismCode = ecotourism.getPublicIdentifyCode();
            this.programName = ecotourism.getProgram().getName();
            this.theme = ecotourism.getTheme().getName();
            this.region = ecotourism.getRegion().toString();
            this.programIntro = ecotourism.getProgram().getIntro();
            this.programDetail = ecotourism.getProgram().getDetail();
        }

        public void addTheme(String theme) {
            this.theme = this.theme + SEPARATOR_COMMA + theme;
        }
    }
}
