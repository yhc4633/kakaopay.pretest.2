package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Data
@Entity
@Table(name = "ecotourism")
@NoArgsConstructor
public class Ecotourism implements CommonEntity {
    public Ecotourism(Region region, List<Theme> theme, Program program) {
        this.region = region;
        this.themeList = theme;
        this.program = program;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ecotourismCode;

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name ="region_code")
    private Region region;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
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
    public void setPublicIdentifyCode(String identifyCode) {

    }

    public String getTourKeyExceptTheme() {
        return getRegion().getPublicIdentifyCode() + SEPARATOR_BAR + getProgram().getPublicIdentifyCode();
    }
}