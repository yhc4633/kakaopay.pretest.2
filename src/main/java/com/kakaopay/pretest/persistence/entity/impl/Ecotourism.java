package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Data
@Entity
@Table(name = "ecotourism")
@NoArgsConstructor
public class Ecotourism implements CommonEntity, Comparable<Ecotourism> {
    public Ecotourism(List<Region> region, List<Theme> theme, Program program) {
        this.regionList = region;
        this.themeList = theme;
        this.program = program;
    }

    @Transient
    @Setter(AccessLevel.NONE)
    private float themeAndProgramWeightScore = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ecotourismCode;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "ecotourism_region", joinColumns = @JoinColumn(name ="ecotourism_code"), inverseJoinColumns = @JoinColumn(name ="region_code"))
    private List<Region> regionList;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "ecotourism_theme", joinColumns = @JoinColumn(name ="ecotourism_code"), inverseJoinColumns = @JoinColumn(name ="theme_code"))
    private List<Theme> themeList;

    @ManyToOne(targetEntity = Program.class)
    @JoinColumn(name ="program_code")
    private Program program;

    @Override
    public String getPublicIdentifyCode() {
        return ECOTOURISM_CODE_PREFIX + getEcotourismCode();
    }

    @Override
    public int compareTo(Ecotourism ecotourism) {
        if (getThemeAndProgramWeightScore() < ecotourism.getThemeAndProgramWeightScore()) {
            return 1;
        } else if (getThemeAndProgramWeightScore() > ecotourism.getThemeAndProgramWeightScore()) {
            return -1;
        }
        return 0;
    }

    public void calculateThemeAndProgramWeightScore(String recommendKeyword) {
        float themeScore = 0;

        for (Theme theme : getThemeList()) {
            themeScore += theme.calculateThemeWeightScore(recommendKeyword);
        }

        this.themeAndProgramWeightScore = themeScore + getProgram().calculateProgramWeightScore(recommendKeyword);
    }
}