package com.kakaopay.pretest.model.extend;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

        for (Ecotourism ecotourism : ecotourismList) {
            resultList.add(new SearchResult(ecotourism));
        }

        this.resultCode = SUCCESS.getResultCode();
    }

    @Data
    @NoArgsConstructor
    private class SearchResult {
        private String programName;
        private String theme;
        private String region;
        private String programIntro;
        private String programDetail;

        public SearchResult(Ecotourism ecotourism) {
            this.programName = ecotourism.getProgram().getName();
            this.theme = ecotourism.getTheme().getName();
            this.region = ecotourism.getRegion().toString();
            this.programIntro = ecotourism.getProgram().getIntro();
            this.programDetail = ecotourism.getProgram().getDetail();
        }
    }
}
