package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Theme;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteThemeListIfNotUsing(List<Theme> removeTargetThemeList, List<Ecotourism> ecotourismList) {
        if (CollectionUtils.size(removeTargetThemeList) > NumberUtils.INTEGER_ZERO && CollectionUtils.size(ecotourismList) == NumberUtils.INTEGER_ZERO) {
            getThemeRepository().delete(removeTargetThemeList);
        }
    }
}