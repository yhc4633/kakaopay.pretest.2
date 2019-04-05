package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.persistence.entity.impl.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findThemeByName(String name);
}