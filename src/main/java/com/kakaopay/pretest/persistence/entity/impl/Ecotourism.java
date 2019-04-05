package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ecotourism")
public class Ecotourism implements CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ecotourismCode;

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name ="region_code")
    private Long regionCode;

    @ManyToOne(targetEntity = Theme.class)
    @JoinColumn(name ="theme_code")
    private Long themeCode;

    @ManyToOne(targetEntity = Program.class)
    @JoinColumn(name ="program_code")
    private Long programCode;

    @Override
    public String getPublicIdentifyCode() {
        return "ectr_" + getEcotourismCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }
}