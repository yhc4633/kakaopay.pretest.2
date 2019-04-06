package com.kakaopay.pretest.model.extend;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kakaopay.pretest.constants.ResponseCode.*;

@Data
public class FrequentEcotourismRegionResponse extends AbstractResponse {
    private String programIntroKeyword;
    private List<SearchResult> resultList = new ArrayList<>();

    public FrequentEcotourismRegionResponse(ResponseHeader responseHeader, String programIntroKeyword, List<Ecotourism> ecotourismList) {
        super(responseHeader);
        this.programIntroKeyword = programIntroKeyword;
        if (ecotourismList == null) {
            this.resultCode = ERROR_WRONG_PARAMETER.getResultCode();
            return;
        }

        // TODO : 테마만 다른 경우 같은 관광 정보로 간주
        Map<String, List<Ecotourism>> regionCountedMap = ecotourismList.stream().collect(Collectors.groupingBy(ecotourism -> ecotourism.getRegion().getPublicIdentifyCode()));

        for (String regionCountedMapKey : regionCountedMap.keySet()) {
            resultList.add(new SearchResult(regionCountedMapKey, regionCountedMap.get(regionCountedMapKey).size()));
        }
    }

    @Data
    @NoArgsConstructor
    private class SearchResult {
        private String region;
        private int count;

        public SearchResult(String region, int count) {
            this.region = region;
            this.count = count;
        }
    }
}