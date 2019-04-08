package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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

    @Transient
    @Setter(AccessLevel.NONE)
    private int introWeightPercentage = 30;

    @Transient
    @Setter(AccessLevel.NONE)
    private int detailWeightPercentage = 20;

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

    public boolean isSameProgram(Program program) {
        if (program == null) {
            return false;
        }

        return StringUtils.equals(getName(), program.getName())
                && StringUtils.equals(getIntro(), program.getIntro())
                && StringUtils.equals(getDetail(), program.getDetail());
    }

    public float calculateProgramWeightScore(String recommendKeyword) {
        int matchIntroCount = StringUtils.countMatches(getIntro(), recommendKeyword);
        float introScore = matchIntroCount * getIntroWeightPercentage() / 100;

        int matchDetailCount = StringUtils.countMatches(getDetail(), recommendKeyword);
        float detailScore = matchDetailCount * getIntroWeightPercentage() / 100;

        return introScore + detailScore;
    }
}