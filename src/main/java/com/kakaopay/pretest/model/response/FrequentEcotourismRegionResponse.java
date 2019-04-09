package com.kakaopay.pretest.model.response;

import com.kakaopay.pretest.model.AbstractResponse;
import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Region;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.function.Function;
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

        List<Region> regionList = ecotourismList.stream().flatMap(ecotourism -> ecotourism.getRegionList().stream()).collect(Collectors.toList());

        Map<String, Long> regionCountMap = regionList.stream().collect(Collectors.groupingBy(Region::toString, Collectors.counting()));

        for (String regionCountMapKey : regionCountMap.keySet()) {
            resultList.add(new SearchResult(regionCountMapKey, Math.toIntExact(regionCountMap.get(regionCountMapKey))));
        }

        this.resultCode = SUCCESS.getResultCode();
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