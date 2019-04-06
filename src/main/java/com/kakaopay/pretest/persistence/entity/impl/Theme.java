package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "theme")
@NoArgsConstructor
public class Theme implements CommonEntity {
    public Theme(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeCode;

    @Column
    private String name;

    @Override
    public String getPublicIdentifyCode() {
        return "thm_" + getThemeCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }
}