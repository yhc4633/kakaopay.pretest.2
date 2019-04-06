package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "program")
@NoArgsConstructor
public class Program implements CommonEntity {
    public Program(String name, String intro, String detail) {
        this.name = name;
        this.intro = intro;
        this.detail = detail;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programCode;

    @Lob
    @Column
    private String name;

    @Lob
    @Column
    private String intro;

    @Lob
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