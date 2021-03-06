package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.EcotourismRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class EcotourismRepositoryCustom implements CommonProcess<Ecotourism> {
    private final EcotourismRepository ecotourismRepository;

    @Override
    public Ecotourism saveIfNotExist(Ecotourism ecotourism) {
        if (ecotourism == null) {
            return null;
        }

        Ecotourism searchedEcotourism = ecotourismRepository.findEcotourismByRegionListInAndThemeListInAndProgram(ecotourism.getRegionList(), ecotourism.getThemeList(), ecotourism.getProgram());

        if (searchedEcotourism != null) {
            return searchedEcotourism;
        }

        return ecotourismRepository.saveAndFlush(ecotourism);
    }
}