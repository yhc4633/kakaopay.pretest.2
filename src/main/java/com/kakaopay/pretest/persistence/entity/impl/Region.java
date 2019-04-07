package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Data
@Entity
@NoArgsConstructor
public class Region implements CommonEntity {
    public Region(String region) {
        for (String subdivideRegion : StringUtils.split(region, SEPARATOR_SPACE)) {
            setPropertyByRule(subdivideRegion);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_code")
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
    private String dong;

    @Column
    private String etc;

    private void setPropertyByRule(String subdivideRegion) {
        if (StringUtils.isEmpty(subdivideRegion)) {
            return;
        }

        if (StringUtils.endsWith(subdivideRegion, "등")) {
            subdivideRegion = StringUtils.substringBeforeLast(subdivideRegion, "등").trim();
        }

        if (StringUtils.endsWith(subdivideRegion, "도") && StringUtils.isEmpty(getDoh())) {
            setDoh(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "시") && StringUtils.isEmpty(getSi())) {
            setSi(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "군") && StringUtils.isEmpty(getGoon())) {
            setGoon(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "구") && StringUtils.endsWith(subdivideRegion, "지구") == false && StringUtils.isEmpty(getGu())) {
            setGu(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "면") && StringUtils.isEmpty(getMyun())) {
            setMyun(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "리") && StringUtils.isEmpty(getRi())) {
            setRi(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "읍") && StringUtils.isEmpty(getEub())) {
            setEub(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "동") && StringUtils.isEmpty(getDong())) {
            setDong(subdivideRegion);
        } else {
            if (StringUtils.isEmpty(getEtc())) {
                setEtc(subdivideRegion);
            } else {
                setEtc(getEtc() + SEPARATOR_SPACE + subdivideRegion);
            }
        }
    }

    @Override
    public String getPublicIdentifyCode() {
        return "reg_" + getRegionCode();
    }

    public static String[] createRegionArrWithPrefix(String regions) {
        if (StringUtils.isEmpty(regions)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        regions = StringUtils.replace(regions, SEPARATOR_WAVE, SEPARATOR_COMMA);

        String[] regionArr = StringUtils.split(regions, SEPARATOR_COMMA);

        if (regionArr.length == SINGLE_REGION_COUNT) {
            return regionArr;
        }

        int firstIndex = 0;
        String multipleRegionPrefix = StringUtils.substringBeforeLast(regionArr[firstIndex].trim(), SEPARATOR_SPACE).trim();
        for (int i=firstIndex; i<regionArr.length; i++) {
            if (i != firstIndex) {
                regionArr[i] = multipleRegionPrefix + SEPARATOR_SPACE + regionArr[i].trim();
            }
        }

        return regionArr;
    }

    @Override
    public String toString() {
        return Stream.of(getDoh(), getSi(), getGoon(), getGu(), getMyun(), getRi(), getEub(), getDong(), getEtc())
                .filter(string -> StringUtils.isNotEmpty(string))
                .collect(Collectors.joining(SEPARATOR_SPACE));
    }
}