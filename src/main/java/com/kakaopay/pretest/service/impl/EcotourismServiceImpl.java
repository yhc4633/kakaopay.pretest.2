package com.kakaopay.pretest.service.impl;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Program;
import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.entity.impl.Theme;
import com.kakaopay.pretest.persistence.repository.custom.EcotourismRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ProgramRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.RegionRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ThemeRepositoryCustom;
import com.kakaopay.pretest.service.TourService;
import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.*;
import static com.kakaopay.pretest.constants.ResponseCode.*;

@Slf4j
@Data
@Service("ecotourismService")
@AllArgsConstructor
public class EcotourismServiceImpl implements TourService<Ecotourism> {
    private final EcotourismRepositoryCustom ecotourismRepositoryCustom;
    private final RegionRepositoryCustom regionRepositoryCustom;
    private final ThemeRepositoryCustom themeRepositoryCustom;
    private final ProgramRepositoryCustom programRepositoryCustom;

    /**
     * csv 파일 업로드 메소드
     * 실패 한 데이터의 번호 들 리턴
     *
     * @param ecotourismFile
     * @return
     */
    @Override
    public List<String> uploadFile(MultipartFile ecotourismFile) {
        if (ecotourismFile == null) {
            log.error("ecotourism file is null");
            return null;
        }

        List<String> uploadFailedTourNoList = new ArrayList<>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(ecotourismFile.getInputStream(), "EUC-KR");
            CSVReader csvReader = new CSVReader(inputStreamReader);
            List<String[]> ecotourismList = csvReader.readAll();
            ecotourismList.remove(COLUMN_NAME_INDEX);

            for (String[] ecotourismArr : ecotourismList){
                if (addTour(ecotourismArr) != SUCCESS.getResultCode()) {
                    uploadFailedTourNoList.add(ecotourismArr[TOUR_INFO_ARR_NUMBER_INDEX]);
                }
            }
            return uploadFailedTourNoList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 생태 관광 정보 등록
     * result code 리턴
     *
     * @param ecotourismArr
     * @return
     */
    @Override
    public Integer addTour(String[] ecotourismArr) {
        if (ArrayUtils.getLength(ecotourismArr) != VALID_TOUR_INFO_ARR_LENGTH) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        if (StringUtils.isAnyEmpty(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX], ecotourismArr[TOUR_INFO_ARR_THEME_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX])) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        Program savedProgram = programRepositoryCustom.saveIfNotExist(new Program(ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX]));

        for (String region : Region.createRegionArrWithPrefix(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX])) {
            Region savedRegion = regionRepositoryCustom.saveIfNotExist(new Region(region));

            List<Theme> saverThemeList = new ArrayList<>();
            for (String theme : ecotourismArr[TOUR_INFO_ARR_THEME_INDEX].split(SEPARATOR_COMMA)) {
                saverThemeList.add(themeRepositoryCustom.saveIfNotExist(new Theme(theme)));
            }

            Ecotourism ecotourism = new Ecotourism(savedRegion, saverThemeList, savedProgram);

            ecotourismRepositoryCustom.saveIfNotExist(ecotourism);
        }

        return SUCCESS.getResultCode();
    }

    @Override
    public Integer modifyTour(String ecotourismCode, String[] ecotourismArr) {
        if (StringUtils.isEmpty(ecotourismCode) || StringUtils.startsWith(ecotourismCode, ECOTOURISM_CODE_PREFIX) == false
                || ArrayUtils.getLength(ecotourismArr) != VALID_TOUR_INFO_ARR_LENGTH || StringUtils.isEmpty(StringUtils.join(ecotourismArr))) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        String ecotourismCodeExceptPrefix = StringUtils.replace(ecotourismCode, ECOTOURISM_CODE_PREFIX, "");
        if (NumberUtils.isDigits(ecotourismCodeExceptPrefix) == false) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        Ecotourism ecotourism = ecotourismRepositoryCustom.getEcotourismRepository().findOne(NumberUtils.toLong(ecotourismCodeExceptPrefix));

        if (ecotourism == null) {
            return ERROR_NO_DATA.getResultCode();
        }

        // region, program이 같은 ecotourism은 같은 데이터로 간주
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegionAndProgram(ecotourism.getRegion(), ecotourism.getProgram());

        // 프로그램 업데이트. 해당 프로그램 쓰는 여행 정보가 위의 같은 데이터들 뿐이면 update, 그 외에도 있으면 insert

        // 지역 업데이트. 해당 지역인 여행 정보가 위의 같은 데이터들 뿐이면 update, 그 외에도 있으면 insert

        // 테마 업데이트


        return null;
    }

    @Override
    public List<Ecotourism> getTourListByRegionCode(String regionCode) {
        if (StringUtils.isEmpty(regionCode) || StringUtils.startsWith(regionCode,REGION_CODE_PREFIX) == false) {
            return null;
        }

        String regionCodeExceptPrefix = StringUtils.replace(regionCode, REGION_CODE_PREFIX, "");
        if (NumberUtils.isDigits(regionCodeExceptPrefix) == false) {
            return null;
        }

        Region region = regionRepositoryCustom.getRegionRepository().findOne(NumberUtils.toLong(regionCodeExceptPrefix));

        if (region == null) {
            return Collections.EMPTY_LIST;
        }

        return findEcotourismListByRegion(region);
    }

    private List<Ecotourism> findEcotourismListByRegion(Region region) {
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegion(region);

        return ecotourismList == null ? Collections.EMPTY_LIST : ecotourismList;
    }

    @Override
    public List<Ecotourism> getTourListByRegionKeyword(String regionKeyword) {
        if (regionKeyword == null) {
            return null;
        }

        List<Region> regionList = regionRepositoryCustom.findRegionListByKeyword(regionKeyword);

        if (CollectionUtils.isEmpty(regionList)) {
            return Collections.EMPTY_LIST;
        }

        List<Ecotourism> ecotourismList = new ArrayList<>();

        for (Region region : regionList) {
            ecotourismList.addAll(findEcotourismListByRegion(region));
        }

        return ecotourismList;
    }

    @Override
    public List<Ecotourism> getTourListByProgramIntroKeyword(String programIntroKeyword) {
        if (StringUtils.isEmpty(programIntroKeyword)) {
            return null;
        }

        List<Program> programList = programRepositoryCustom.findProgramListByIntroKeyword(programIntroKeyword);

        if (CollectionUtils.isEmpty(programList)) {
            return Collections.EMPTY_LIST;
        }

        List<Ecotourism> ecotourismList = new ArrayList<>();

        for (Program program : programList) {
            ecotourismList.addAll(findEcotourismListByProgram(program));
        }

        return ecotourismList;
    }

    private List<Ecotourism> findEcotourismListByProgram(Program program) {
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByProgram(program);

        return ecotourismList == null ? Collections.EMPTY_LIST : ecotourismList;
    }

    @Override
    public Integer getFrequencyInProgramDetail(String programDetailKeyword) {
        if (StringUtils.isEmpty(programDetailKeyword)) {
            return null;
        }

        Integer count = 0;

        for (Program program : programRepositoryCustom.getProgramRepository().findAll()) {
            count += StringUtils.countMatches(program.getDetail(), programDetailKeyword);
        }

        return count;
    }
}