package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.RegionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@AllArgsConstructor
public class RegionRepositoryCustom implements CommonProcess<Region> {
    private final RegionRepository regionRepository;

    @Override
    public Region saveIfNotExist(Region region) {
        if (region == null) {
            return null;
        }

        Region searchedRegion = regionRepository.findRegionByDohAndSiAndGoonAndGuAndMyunAndRiAndEubAndDongAndEtc(
                region.getDoh(), region.getSi(), region.getGoon(), region.getGu()
                , region.getMyun(), region.getRi(), region.getEub(), region.getDong(), region.getEtc());

        if (searchedRegion != null) {
            return searchedRegion;
        }

        return regionRepository.save(region);
    }

    public List<Region> findRegionListByKeyword(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }

        final String searchKeyword = StringUtils.endsWith(keyword, "등") ? StringUtils.substringBeforeLast(keyword, "등") : keyword;

        List<Region> searchedRegionList = null;
        if (StringUtils.endsWith(searchKeyword, "도")) {
            searchedRegionList = regionRepository.findAllByDoh(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "시")) {
            searchedRegionList = regionRepository.findAllBySi(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "군")) {
            searchedRegionList = regionRepository.findAllByGoon(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "구") && StringUtils.endsWith(searchKeyword, "지구") == false) {
            searchedRegionList = regionRepository.findAllByGu(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "면")) {
            searchedRegionList = regionRepository.findAllByMyun(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "리")) {
            searchedRegionList = regionRepository.findAllByRi(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "읍")) {
            searchedRegionList = regionRepository.findAllByEub(searchKeyword);
        } else if (StringUtils.endsWith(searchKeyword, "동")) {
            searchedRegionList = regionRepository.findAllByDong(searchKeyword);
        }

        if (CollectionUtils.isNotEmpty(searchedRegionList)) {
            return searchedRegionList;
        }

        return regionRepository.findAll().stream().filter(region -> StringUtils.contains(region.getEtc(), searchKeyword)).collect(Collectors.toList());
    }
}