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
import java.util.PriorityQueue;
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

        List<Region> savedRegionList = saveRegionsIfNotExist(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX]);

        List<Theme> savedThemeList = saveThemesIfNotExist(ecotourismArr[TOUR_INFO_ARR_THEME_INDEX]);

        Program savedProgram = saveProgramIfNotExist(ecotourismArr);

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

    private List<Region> saveRegionsIfNotExist(String regions) {
        List<Region> savedRegionList = new ArrayList<>();
        for (String region : Region.createRegionArrWithPrefix(regions)) {
            savedRegionList.add(regionRepositoryCustom.saveIfNotExist(new Region(region.trim())));
        }
        return savedRegionList;
    }

    private List<Theme> saveThemesIfNotExist(String themes) {
        List<Theme> savedThemeList = new ArrayList<>();
        for (String theme : themes.split(SEPARATOR_COMMA)) {
            if (StringUtils.isNotEmpty(theme.trim())) {
                savedThemeList.add(themeRepositoryCustom.saveIfNotExist(new Theme(theme.trim())));
            }
        }
        return savedThemeList;
    }

    private Program saveProgramIfNotExist(String[] ecotourismArr) {
        String programName = ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX];
        String programIntro = ecotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX];
        String programDetail = ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX];
        return programRepositoryCustom.saveIfNotExist(new Program(programName, programIntro, programDetail));
    }

    @Override
    public Integer modifyTour(String[] ecotourismArr) {
        if (isValidEcotourismArr(ecotourismArr) == false) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        String ecotourismCode = ecotourismArr[TOUR_INFO_ARR_NUMBER_INDEX];
        if (isValidCode(ecotourismCode, ECOTOURISM_CODE_PREFIX) == false) {
            return ERROR_WRONG_PARAMETER.getResultCode();
        }

        String ecotourismCodeExceptPrefix = StringUtils.replace(ecotourismCode, ECOTOURISM_CODE_PREFIX, "");

        Ecotourism ecotourism = ecotourismRepositoryCustom.getEcotourismRepository().findOne(NumberUtils.toLong(ecotourismCodeExceptPrefix));

        if (ecotourism == null) {
            return ERROR_NO_DATA.getResultCode();
        }

        // 지역, 테마, 프로그램 업데이트. 단순 추가, 바뀐 경우 insert
        List<Region> originRegionList = ecotourism.getRegionList();
        List<Region> newRegionList = saveRegionsIfNotExist(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX]);
        ecotourism.setRegionList(newRegionList);

        List<Theme> originThemeList = ecotourism.getThemeList();
        List<Theme> newThemeList = saveThemesIfNotExist(ecotourismArr[TOUR_INFO_ARR_THEME_INDEX]);
        ecotourism.setThemeList(newThemeList);

        Program removeTargetProgram = ecotourism.getProgram();
        Program newProgram = saveProgramIfNotExist(ecotourismArr);
        ecotourism.setProgram(newProgram);

        ecotourismRepositoryCustom.getEcotourismRepository().saveAndFlush(ecotourism);

        // 제외된 지역, 테마, 프로그램은 다른 데이터의 사용여부 확인 후 잔존 or 제거
        List<Region> removeTargetRegionList = getRemoveTargetRegionList(originRegionList, newRegionList);
        regionRepositoryCustom.deleteRegionListIfNotUsing(removeTargetRegionList, ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegionListIn(removeTargetRegionList));

        List<Theme> removeTargetThemeList = getRemoveTargetThemeList(originThemeList, newThemeList);
        themeRepositoryCustom.deleteThemeListIfNotUsing(removeTargetThemeList, ecotourismRepositoryCustom.getEcotourismRepository().findAllByThemeListIn(removeTargetThemeList));

        if (removeTargetProgram.isSameProgram(newProgram) == false) {
            programRepositoryCustom.deleteProgramIfNotUsing(removeTargetProgram, ecotourismRepositoryCustom.getEcotourismRepository().findAllByProgram(removeTargetProgram));
        }

        return SUCCESS.getResultCode();
    }

    private boolean isValidCode(String code, String codePrefix) {
        return StringUtils.isNotBlank(code) && StringUtils.startsWith(code, codePrefix)
                && NumberUtils.isDigits(StringUtils.replace(code, codePrefix, ""));
    }

    private List<Region> getRemoveTargetRegionList(List<Region> originRegionList, List<Region> newRegionList) {
        List<Region> removeTargetRegionList = new ArrayList<>(originRegionList);
        for (Region newRegion : newRegionList) {
            removeTargetRegionList = removeTargetRegionList.stream().filter(originRegion -> StringUtils.equals(originRegion.toString(), newRegion.toString()) == false).collect(Collectors.toList());
        }
        return removeTargetRegionList;
    }

    private List<Theme> getRemoveTargetThemeList(List<Theme> originThemeList, List<Theme> newThemeList) {
        List<Theme> removeTargetThemeList = new ArrayList<>(originThemeList);
        for (Theme newTheme : newThemeList) {
            removeTargetThemeList = removeTargetThemeList.stream().filter(originTheme -> StringUtils.equals(originTheme.getName(), newTheme.getName()) == false).collect(Collectors.toList());
        }
        return removeTargetThemeList;
    }

    @Override
    public List<Ecotourism> getTourListByRegionCode(String regionCode) {
        if (isValidCode(regionCode, REGION_CODE_PREFIX) == false) {
            return null;
        }

        String regionCodeExceptPrefix = StringUtils.replace(regionCode, REGION_CODE_PREFIX, "");

        Region region = regionRepositoryCustom.getRegionRepository().findOne(NumberUtils.toLong(regionCodeExceptPrefix));

        if (region == null) {
            return Collections.EMPTY_LIST;
        }

        List<Region> regionList = new ArrayList<>();
        regionList.add(region);

        return findEcotourismListByRegionList(regionList);
    }

    private List<Ecotourism> findEcotourismListByRegionList(List<Region> regionList) {
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegionListIn(regionList);

        return ecotourismList == null ? Collections.EMPTY_LIST : ecotourismList;
    }

    @Override
    public List<Ecotourism> getTourListByRegionKeyword(String regionKeyword) {
        if (StringUtils.isBlank(regionKeyword)) {
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
        if (StringUtils.isBlank(programDetailKeyword)) {
            return null;
        }

        Integer count = 0;

        for (Program program : programRepositoryCustom.getProgramRepository().findAll()) {
            count += StringUtils.countMatches(program.getDetail(), programDetailKeyword);
        }

        return count;
    }

    @Override
    public Ecotourism getTourByRecommend(String regionKeyword, String recommendKeyword) {
        if (StringUtils.isBlank(recommendKeyword)) {
            return null;
        }

        List<Ecotourism> ecotourismList = getTourListByRegionKeyword(regionKeyword);

        if (CollectionUtils.isEmpty(ecotourismList)) {
            return null;
        }

        ecotourismList.forEach(ecotourism -> ecotourism.calculateThemeAndProgramWeightScore(recommendKeyword));

        return new PriorityQueue<>(ecotourismList).poll();
    }
}