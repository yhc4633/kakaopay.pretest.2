package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.RegionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class RegionRepositoryCustom implements CommonProcess<Region> {
    private final RegionRepository regionRepository;

    @Override
    public Region saveIfNotExist(Region region) {
        Region searchedRegion = regionRepository.findRegionByDohAndSiAndGoonAndGuAndMyunAndRiAnAndEubAndEtc(
                region.getDoh(), region.getSi(), region.getGoon(), region.getGu()
                , region.getMyun(), region.getRi(), region.getEub(), region.getEtc());

        if (searchedRegion != null) {
            return searchedRegion;
        }

        return regionRepository.save(region);
    }
}