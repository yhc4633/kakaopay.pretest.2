package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.persistence.entity.impl.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findRegionByDohAndSiAndGoonAndGuAndMyunAndRiAndEubAndDongAndEtc(String doh, String si, String goon, String gu, String myun, String ri, String eub, String dong, String etc);
}