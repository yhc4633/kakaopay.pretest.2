package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "program")
public class Program implements CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programCode;

    @Column
    private String name;

    @Column
    private String intro;

    @Column
    private String detail;

    @Override
    public String getPublicIdentifyCode() {
        return "prgm_" + getProgramCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }
}