package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.persistence.entity.impl.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findRegionByDohAndSiAndGoonAndGuAndMyunAndRiAndEubAndDongAndEtc(String doh, String si, String goon, String gu, String myun, String ri, String eub, String dong, String etc);
    List<Region> findAllByDoh(String doh);
    List<Region> findAllBySi(String si);
    List<Region> findAllByGoon(String goon);
    List<Region> findAllByGu(String gu);
    List<Region> findAllByMyun(String myun);
    List<Region> findAllByRi(String ri);
    List<Region> findAllByEub(String eub);
    List<Region> findAllByDong(String dong);
}