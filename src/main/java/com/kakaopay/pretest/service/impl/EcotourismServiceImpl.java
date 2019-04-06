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
     * 실패 한 데이터의 번호 들을 리턴
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
                if (addTour(ecotourismArr) == false) {
                    uploadFailedTourNoList.add(ecotourismArr[TOUR_INFO_ARR_NUMBER_INDEX]);
                }
            }
            return uploadFailedTourNoList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean addTour(String[] ecotourismArr) {
        if (ArrayUtils.getLength(ecotourismArr) != VALID_TOUR_INFO_ARR_LENGTH) {
            return false;
        }

        if (StringUtils.isAnyEmpty(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX], ecotourismArr[TOUR_INFO_ARR_THEME_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX])) {
            return false;
        }

        Program savedProgram = programRepositoryCustom.saveIfNotExist(new Program(ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX], ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX]));

        for (String region : Region.createRegionArrWithPrefix(ecotourismArr[TOUR_INFO_ARR_REGION_INDEX])) {
            Region savedRegion = regionRepositoryCustom.saveIfNotExist(new Region(region));

            for (String theme : ecotourismArr[TOUR_INFO_ARR_THEME_INDEX].split(",")) {
                Theme savedTheme = themeRepositoryCustom.saveIfNotExist(new Theme(theme));

                Ecotourism ecotourism = new Ecotourism(savedRegion, savedTheme, savedProgram);

                ecotourismRepositoryCustom.saveIfNotExist(ecotourism);
            }
        }

        return true;
    }

    @Override
    public List<Ecotourism> getTourList(String regionCode) {
        if (StringUtils.isEmpty(regionCode) || StringUtils.startsWith(regionCode,"reg_") == false) {
            return null;
        }

        String regionCodeExceptPrefix = StringUtils.replace(regionCode, "reg_", "");
        if (NumberUtils.isDigits(regionCodeExceptPrefix) == false) {
            return null;
        }

        Region region = regionRepositoryCustom.getRegionRepository().findOne(NumberUtils.toLong(regionCodeExceptPrefix));

        if (region == null) {
            return Collections.EMPTY_LIST;
        }

        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAllByRegion(region);

        return ecotourismList == null ? Collections.EMPTY_LIST : ecotourismList;
    }


}