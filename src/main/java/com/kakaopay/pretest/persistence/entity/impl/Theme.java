package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Data
@Entity
@Table(name = "theme")
@NoArgsConstructor
public class Theme implements CommonEntity {
    public Theme(String name) {
        this.name = name;
    }

    @Transient
    @Setter(AccessLevel.NONE)
    private float nameWeightPercentage = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_code")
    private Long themeCode;

    @Column
    private String name;

    @Override
    public String getPublicIdentifyCode() {
        return THEME_CODE_PREFIX + getThemeCode();
    }

    public float calculateThemeWeightScore(String recommendKeyword) {
        int matchNameCount = StringUtils.countMatches(getName(), recommendKeyword);

        return ((float) matchNameCount) * getNameWeightPercentage() / PERCENTAGE_STANDARD;
    }
}