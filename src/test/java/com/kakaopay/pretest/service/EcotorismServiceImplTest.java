package com.kakaopay.pretest.service;

import com.kakaopay.pretest.StartApplicationServer;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.entity.impl.Region;
import com.kakaopay.pretest.persistence.repository.custom.EcotourismRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ProgramRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.RegionRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ThemeRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplicationServer.class)
public class EcotorismServiceImplTest {
    @Autowired
    @Qualifier("ecotourismService")
    private TourService ecotourismService;

    @Autowired
    private EcotourismRepositoryCustom ecotourismRepositoryCustom;

    @Autowired
    private RegionRepositoryCustom regionRepositoryCustom;

    @Autowired
    private ThemeRepositoryCustom themeRepositoryCustom;

    @Autowired
    private ProgramRepositoryCustom programRepositoryCustom;

    @After
    public void removeAllData() {
        ecotourismRepositoryCustom.getEcotourismRepository().deleteAll();
        regionRepositoryCustom.getRegionRepository().deleteAll();
        themeRepositoryCustom.getThemeRepository().deleteAll();
        programRepositoryCustom.getProgramRepository().deleteAll();
    }

    // NOTE : 테스트 파일의 데이터 row 수 변경 시 변경 필요
    private final int TEST_FILE_SIZE = 110;

    @Test
    public void 파일_업로드_테스트() {
        List<Ecotourism> ecotourismList = uploadFile();

        assertThat(ecotourismList.size(), is(TEST_FILE_SIZE));
    }

    private List<Ecotourism> uploadFile() {
        BufferedReader br = null;

        try {
            File file = new File("src/test/resources/file/2017년_국립공원_생태관광_정보.csv");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("testFile", file.getName(), "text/plain", IOUtils.toByteArray(input));

            ecotourismService.uploadFile(multipartFile);

            return ecotourismRepositoryCustom.getEcotourismRepository().findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Test
    public void 지역_코드로_관광_정보_조회_테스트() {
        String[] ecotourismArr = getEcotourismArr();
        ecotourismService.addTour(ecotourismArr);

        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        // 최초 삽입 관광 정보
        Ecotourism ecotourism = ecotourismList.get(ecotourismList.size()-1);

        Region region = ecotourism.getRegionList().get(0);
        List<Ecotourism> ecotourismListByRegion = ecotourismService.getTourListByRegionCode(region.getPublicIdentifyCode());

        // 삽입된 지역 코드로 조회된 관광 정보
        Ecotourism ecotourismByRegion = ecotourismListByRegion.get(ecotourismList.size()-1);

        assertThat(ecotourism.getPublicIdentifyCode(), is(ecotourismByRegion.getPublicIdentifyCode()));
    }

    @Test
    public void 관광_정보_개별_추가_테스트() {
        String[] ecotourismArr = getEcotourismArr();

        ecotourismService.addTour(ecotourismArr);

        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        Ecotourism ecotourism = ecotourismList.get(ecotourismList.size()-1);

        assertThat(ecotourismList.size(), is(1));
        assertThat(ecotourism.getProgram().getName(), is(ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX]));
        assertThat(ecotourism.getProgram().getDetail(), is(ecotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX]));
    }

    private String[] getEcotourismArr() {
        String[] ecotourismArr = {"1", "직장인 체험하기", "직업 체험", "경기도 성남시 판교", "직업 체험 프로그램", "직장인이 되어 직업을 체험합니다"};
        return ecotourismArr;
    }

    @Test
    public void 관광_정보_수정_테스트() {
        String[] ecotourismArr = getEcotourismArr();
        ecotourismService.addTour(ecotourismArr);
        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        Ecotourism originEcotourism = ecotourismList.get(0);

        String[] modifyEcotourismArr = {originEcotourism.getPublicIdentifyCode(), "직업을 겪어보자", "직업 체험", "경기도 성남시 판교,경기도 성남시 야탑", "직업 체험 프로그램2", "직업을 체험합니다"};
        ecotourismService.modifyTour(modifyEcotourismArr);
        ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        Ecotourism modifiedEcotourism = ecotourismList.get(0);

        assertThat(originEcotourism.getPublicIdentifyCode(), is(modifiedEcotourism.getPublicIdentifyCode()));
        assertThat(modifiedEcotourism.getProgram().getName(), is(modifyEcotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX]));
        assertThat(modifiedEcotourism.getProgram().getIntro(), is(modifyEcotourismArr[TOUR_INFO_ARR_PROGRAM_INTRO_INDEX]));
        assertThat(modifiedEcotourism.getProgram().getDetail(), is(modifyEcotourismArr[TOUR_INFO_ARR_PROGRAM_DETAIL_INDEX]));
        assertThat(modifiedEcotourism.getRegionList().size(), is(modifyEcotourismArr[TOUR_INFO_ARR_REGION_INDEX].split(SEPARATOR_COMMA).length));
    }

    @Test
    public void 지역명으로_특정지역의_프로그램명_테마_출력_테스트() {
        String[] ecotourismArr = getEcotourismArr();

        ecotourismService.addTour(ecotourismArr);

        String[] ecotourismArr2 = {"2", "직업을 겪어보자2", "직업 체험", "경기도 성남시 야탑", "직업 체험 프로그램2", "직업을 체험합니다"};

        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionKeyword("판교");

        Ecotourism ecotourism = ecotourismList.get(ecotourismList.size()-1);

        assertThat(ecotourism.getProgram().getName(), is(ecotourismArr[TOUR_INFO_ARR_PROGRAM_NAME_INDEX]));
        assertThat(ecotourism.getThemeList().get(0).getName(), is(ecotourismArr[TOUR_INFO_ARR_THEME_INDEX]));
    }

    @Test
    public void 프로그램_소개_키워드로_서비스지역_개수_출력_테스트() {
        String[] ecotourismArr = getEcotourismArr();
        ecotourismService.addTour(ecotourismArr);
        String[] ecotourismArr2 = {"2", "직업을 겪어보자2", "직업 체험", "경기도 성남시 야탑", "직업 체험 프로그램2", "직업을 체험합니다"};
        ecotourismService.addTour(ecotourismArr2);

        String programIntroKeyword = "직업";

        List<Ecotourism> ecotourismList = ecotourismService.getTourListByProgramIntroKeyword(programIntroKeyword);

        String regions = "";
        int count = 0;
        for (Ecotourism ecotourism : ecotourismList) {
            for (Region region : ecotourism.getRegionList()) {
                regions = regions + SEPARATOR_COMMA + region.toString();
            }

            count++;
        }

        assertThat(regions, is(",경기도 성남시 판교,경기도 성남시 야탑"));
        assertThat(count, is(2));
    }

    @Test
    public void 프로그램_상세_키워드의_출현_빈도_테스트() {
        String[] ecotourismArr = getEcotourismArr();

        ecotourismService.addTour(ecotourismArr);

        String programDetailKeyword = "직업";

        assertThat(ecotourismService.getFrequencyInProgramDetail(programDetailKeyword), is(1));
    }

    @Test
    public void 지역명_관광_키워드로_프로그램_추천_테스트() {
        uploadFile();

        List<Ecotourism> ecotourismList = ecotourismService.getTourListByProgramIntroKeyword("어름치마을");
        Ecotourism targetEcotourism = ecotourismList.get(ecotourismList.size()-1);

        String regionKeyword = "평창";
        String recommendKeyword = "국립공원";

        Ecotourism ecotourism = (Ecotourism) ecotourismService.getTourByRecommend(regionKeyword, recommendKeyword);

        assertThat(ecotourism.getPublicIdentifyCode(), is(targetEcotourism.getPublicIdentifyCode()));
    }
}