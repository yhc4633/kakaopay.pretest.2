package com.kakaopay.pretest.persistence.entity.impl;

import com.kakaopay.pretest.persistence.entity.CommonEntity;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import static com.kakaopay.pretest.constants.ParameterCode.SINGLE_REGION_COUNT;

@Data
@Entity
public class Region implements CommonEntity {
    public Region(String region) {
        for (String subdivideRegion : StringUtils.split(region, " ")) {
            setPropertyByRule(subdivideRegion);
        }
    }

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

    private void setPropertyByRule(String subdivideRegion) {
        if (StringUtils.isEmpty(subdivideRegion)) {
            return;
        }

        if (StringUtils.endsWith(subdivideRegion, "도")) {
            setDoh(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "시")) {
            setSi(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "군")) {
            setGoon(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "구")) {
            setGu(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "면")) {
            setMyun(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "리")) {
            setRi(subdivideRegion);
        } else if (StringUtils.endsWith(subdivideRegion, "읍")) {
            setEub(subdivideRegion);
        } else {
            if (StringUtils.isEmpty(getEtc())) {
                setEtc(subdivideRegion);
            } else {
                setEtc(getEtc() + " " + subdivideRegion);
            }
        }
    }

    @Override
    public String getPublicIdentifyCode() {
        return "reg_" + getRegionCode();
    }

    @Override
    public void setPublicIdentifyCode(String identifyCode) {

    }

    public static String[] createRegionArrWithPrefix(String regions) {
        if (StringUtils.isEmpty(regions)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        String[] regionArr = StringUtils.split(regions, ",");

        if (regionArr.length == SINGLE_REGION_COUNT) {
            return regionArr;
        }

        int firstIndex = 0;
        String multipleRegionPrefix = StringUtils.substringBeforeLast(regionArr[firstIndex], " ");
        for (int i=firstIndex; i<regionArr.length; i++) {
            if (i != firstIndex) {
                regionArr[i] = multipleRegionPrefix + " " + regionArr[i].trim();
            }
        }

        return regionArr;
    }


}