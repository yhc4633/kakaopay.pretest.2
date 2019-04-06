package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ecotourism")
@NoArgsConstructor
public class Ecotourism implements CommonEntity {
    public Ecotourism(Region region, Theme theme, Program program) {
        this.region = region;
        this.theme = theme;
        this.program = program;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ecotourismCode;

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name ="region_code")
    private Region region;

    @ManyToOne(targetEntity = Theme.class)
    @JoinColumn(name ="theme_code")
    private Theme theme;

    @ManyToOne(targetEntity = Program.class)
    @JoinColumn(name ="program_code")
    private Program program;

    @Override
    public String getPublicIdentifyCode() {
        return "ectr_" + getEcotourismCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }
}