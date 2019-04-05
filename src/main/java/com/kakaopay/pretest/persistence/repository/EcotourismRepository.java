package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcotourismRepository extends JpaRepository<Ecotourism, Long> {
    Ecotourism findEcotourismByRegionCodeAndThemeCodeAndAndProgramCode(Long regionCode, Long themeCode, Long programCode);
}