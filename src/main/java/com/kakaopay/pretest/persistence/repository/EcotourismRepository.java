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
    Ecotourism findEcotourismByRegionAndThemeListAndProgram(Region region, List<Theme> theme, Program program);
    List<Ecotourism> findAllByRegion(Region region);
    List<Ecotourism> findAllByProgram(Program program);
    List<Ecotourism> findAllByRegionAndProgram(Region region, Program program);
}