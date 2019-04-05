package com.kakaopay.pretest.service.impl;

import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.entity.impl.Theme;
import com.kakaopay.pretest.persistence.repository.custom.EcotourismRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.RegionRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ThemeRepositoryCustom;
import com.kakaopay.pretest.service.TourService;
import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.*;

@Slf4j
@Data
@Service("ecotourismService")
@AllArgsConstructor
public class EcotourismServiceImpl implements TourService {
    private final EcotourismRepositoryCustom ecotourismRepositoryCustom;
    private final RegionRepositoryCustom regionRepositoryCustom;
    private final ThemeRepositoryCustom themeRepositoryCustom;

    @Override
    public boolean uploadFile(MultipartFile ecotourismFile) {
        if (ecotourismFile == null) {
            log.error("ecotourism file is null");
            return false;
        }

        try {
            InputStreamReader is = new InputStreamReader(ecotourismFile.getInputStream(), "EUC-KR");
            CSVReader reader = new CSVReader(is);
            List<String[]> list = reader.readAll();

            for (String[] str : list){
                String data = "";
                for (String s : str){
                    data = data + s + " ";
                }
                log.info(data);
            }
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    @Override
    public boolean addTour(String[] tourInfoArr) {
        if (ArrayUtils.getLength(tourInfoArr) != VALID_TOUR_INFO_ARR_LENGTH) {
            return false;
        }

        for (String tourInfo : tourInfoArr) {
            if (StringUtils.isEmpty(tourInfo)) {
                return false;
            }
        }

        for (String region : Region.createRegionArrWithPrefix(tourInfoArr[TOUR_INFO_ARR_REGION_INDEX])) {
            Region savedRegion = regionRepositoryCustom.saveIfNotExist(new Region(region));

            for (String theme : tourInfoArr[TOUR_INFO_ARR_THEME_INDEX].split(",")) {
                Theme savedTheme = themeRepositoryCustom.saveIfNotExist(new Theme(theme));

                Ecotourism ecotourism = new Ecotourism();
                ecotourism.setRegionCode(savedRegion.getRegionCode());
                ecotourism.setThemeCode(savedTheme.getThemeCode());


            }
        }

        //ecotourismRepository.

        return false;
    }


}