package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Theme;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class ThemeRepositoryCustom implements CommonProcess<Theme> {
    private final ThemeRepository themeRepository;

    @Override
    public Theme saveIfNotExist(Theme theme) {
        if (theme == null) {
            return null;
        }

        Theme searchedTheme = themeRepository.findThemeByName(theme.getName());

        if (searchedTheme != null) {
            return searchedTheme;
        }

        return themeRepository.save(theme);
    }
}