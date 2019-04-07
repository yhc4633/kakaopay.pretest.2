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
import java.util.stream.Collectors;

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
        if (isValidEcotourismArr(ecotourismArr) == false) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        List<Region> savedRegionList = new ArrayList<>();
        for (String region : Region.createRegionArrWithPrefix(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX])) {
            savedRegionList.add(regionRepositoryCustom.saveIfNotExist(new Region(region.trim())));
        }

        List<Theme> savedThemeList = new ArrayList<>();
        for (String theme : ecotourismArr[TOUR_INFO_ARR_THEME_INDEX].split(SEPARATOR_COMMA)) {
            if (StringUtils.isNotEmpty(theme.trim())) {
                savedThemeList.add(themeRepositoryCustom.saveIfNotExist(new Theme(theme.trim())));
            }
        }

        String programName = ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX];
        String programIntro = ecotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX];
        String programDetail = ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX];
        Program savedProgram = programRepositoryCustom.saveIfNotExist(new Program(programName, programIntro, programDetail));

        ecotourismRepositoryCustom.saveIfNotExist(new Ecotourism(savedRegionList, savedThemeList, savedProgram));

        return SUCCESS.getResultCode();
    }

    private boolean isValidEcotourismArr(String[] ecotourismArr) {
        if (ArrayUtils.getLength(ecotourismArr) != VALID_TOUR_INFO_ARR_LENGTH
                || StringUtils.isAnyEmpty(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX], ecotourismArr[TOUR_INFO_ARR_THEME_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX])) {
            return false;
        }
        return true;
    }

    @Override
    public Integer modifyTour(String[] ecotourismArr) {
        if (isValidEcotourismArr(ecotourismArr) == false) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        String ecotourismCode = ecotourismArr[TOUR_INFO_ARR_NUMBER_INDEX];
        if (StringUtils.isEmpty(ecotourismCode) || StringUtils.startsWith(ecotourismCode, ECOTOURISM_CODE_PREFIX) == false) {
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

        // 지역 업데이트. 단순 지역 추가 시 insert, 지역이 바뀐 경우 새 지역은 insert
        List<Region> newRegionList = new ArrayList<>();
        List<Region> removeTargetRegionList = new ArrayList<>(ecotourism.getRegionList());
        for (String newRegion : Region.createRegionArrWithPrefix(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX])) {
            newRegionList.add(regionRepositoryCustom.saveIfNotExist(new Region(newRegion.trim())));
            removeTargetRegionList = removeTargetRegionList.stream().filter(region -> StringUtils.equals(region.toString(), newRegion) == false).collect(Collectors.toList());
        }
        ecotourism.setRegionList(newRegionList);

        // 테마 업데이트. 단순 테마 추가 시 insert, 테마가 바뀐 경우 새 테마는 insert
        List<Theme> newThemeList = new ArrayList<>();
        List<Theme> removeTargetThemeList = new ArrayList<>(ecotourism.getThemeList());
        for (String newTheme : ecotourismArr[TOUR_INFO_ARR_THEME_INDEX].split(SEPARATOR_COMMA)) {
            newThemeList.add(themeRepositoryCustom.saveIfNotExist(new Theme(newTheme.trim())));
            removeTargetThemeList = removeTargetThemeList.stream().filter(theme -> StringUtils.equals(theme.getName(), newTheme) == false).collect(Collectors.toList());
        }
        ecotourism.setThemeList(newThemeList);

        // 프로그램 업데이트. 해당 프로그램이 위의 데이터만 해당하면 update, 그 외에도 있으면 insert 후 매핑 변경
        ecotourism.setProgram(renewProgram(ecotourismArr, ecotourism.getProgram()));

        ecotourismRepositoryCustom.getEcotourismRepository().save(ecotourism);

        // 제외된 지역은 다른 데이터의 사용여부 확인 후 잔존 or 제거
        if (removeTargetRegionList.size() > NumberUtils.INTEGER_ZERO && CollectionUtils.size(ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegionList(removeTargetRegionList)) == NumberUtils.INTEGER_ZERO) {
            regionRepositoryCustom.getRegionRepository().delete(removeTargetRegionList);
        }

        // 제외된 테마는 다른 데이터의 사용여부 확인 후 잔존 or 제거
        if (removeTargetThemeList.size() > NumberUtils.INTEGER_ZERO && CollectionUtils.size(ecotourismRepositoryCustom.getEcotourismRepository().findAllByThemeList(removeTargetThemeList)) == NumberUtils.INTEGER_ZERO) {
            themeRepositoryCustom.getThemeRepository().delete(removeTargetThemeList);
        }

        return SUCCESS.getResultCode();
    }

    private Program renewProgram(String[] ecotourismArr, Program program) {
        String programName = ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX];
        String programIntro = ecotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX];
        String programDetail = ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX];
        if (program.isSameProgram(programName, programIntro, programDetail)) {
            return program;
        }

        // 해당 프로그램을 2개 이상의 관광 정보가 사용하는 경우 새로 추가
        if (CollectionUtils.size(ecotourismRepositoryCustom.getEcotourismRepository().findAllByProgram(program)) > NumberUtils.INTEGER_ONE) {
            return programRepositoryCustom.saveIfNotExist(new Program(programName, programIntro, programDetail));
        }

        program.setName(programName);
        program.setIntro(programIntro);
        program.setDetail(programDetail);
        return programRepositoryCustom.getProgramRepository().save(program);
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

        List<Region> regionList = new ArrayList<>();
        regionList.add(region);

        return findEcotourismListByRegionList(regionList);
    }

    private List<Ecotourism> findEcotourismListByRegionList(List<Region> regionList) {
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegionList(regionList);

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
        ecotourismList.addAll(findEcotourismListByRegionList(regionList));

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