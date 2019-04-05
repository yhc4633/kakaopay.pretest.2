package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Region implements CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionCode;

    @Column
    private String doh;

    @Column
    private String si;

    @Column
    private String goon;

    @Column
    private String gu;

    @Column
    private String myun;

    @Column
    private String ri;

    @Column
    private String eub;

    @Column
    private String etc;

    @Override
    public String getPublicIdentifyCode() {
        return "reg_" + getRegionCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }
}