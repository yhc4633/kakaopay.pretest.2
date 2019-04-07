package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Program;
import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.entity.impl.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcotourismRepository extends JpaRepository<Ecotourism, Long> {
    Ecotourism findEcotourismByRegionListAndThemeListAndProgram(List<Region> regionList, List<Theme> theme, Program program);
    List<Ecotourism> findAllByRegionList(List<Region> regionList);
    List<Ecotourism> findAllByProgram(Program program);
    List<Ecotourism> findAllByThemeList(List<Theme> themeList);
}