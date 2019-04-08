package com.kakaopay.pretest.service;

import com.kakaopay.pretest.StartApplicationServer;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.repository.EcotourismRepository;
import com.kakaopay.pretest.persistence.repository.custom.EcotourismRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ProgramRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.RegionRepositoryCustom;
import com.kakaopay.pretest.persistence.repository.custom.ThemeRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
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
    public void test_upload_file() {
        BufferedReader br = null;

        try {
            File file = new File("src/test/resources/file/2017년_국립공원_생태관광_정보.csv");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("testFile", file.getName(), "text/plain", IOUtils.toByteArray(input));

            ecotourismService.uploadFile(multipartFile);

            List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

            assertThat(ecotourismList.size(), is(TEST_FILE_SIZE));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
    public void test_add_tour() {
        String[] ecotourismArr = getEcotourismArr();

        ecotourismService.addTour(ecotourismArr);

        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        Ecotourism ecotourism = ecotourismList.get(ecotourismList.size()-1);

        assertThat(ecotourismList.size(), is(1));
        assertThat(ecotourism.getProgram().getName(), is("직업을 겪어보자"));
    }

    private String[] getEcotourismArr() {
        String[] ecotourismArr = {"1", "직업을 겪어보자", "직업 체험", "경기도 성남시 판교", "직업 체험 프로그램", "직업을 체험합니다"};
        return ecotourismArr;
    }

    @Test
    public void test_modify_tour() {
        String[] ecotourismArr = getEcotourismArr();

        ecotourismService.addTour(ecotourismArr);

        List<Ecotourism> ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();


        Ecotourism originEcotourism = ecotourismList.get(0);

        String[] modifyEcotourismArr = {originEcotourism.getPublicIdentifyCode(), "직업을 겪어보자2", "직업 체험2", "경기도 성남시 판교2", "직업 체험 프로그램2", "직업을 체험합니다2"};


        ecotourismService.modifyTour(modifyEcotourismArr);

        ecotourismList = ecotourismRepositoryCustom.getEcotourismRepository().findAll();

        Ecotourism modifiedEcotourism = ecotourismList.get(0);

        assertThat(originEcotourism.getPublicIdentifyCode(), is(modifiedEcotourism.getPublicIdentifyCode()));
    }

    @Test
    public void test_get_tour_list_by_region_code() {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionCode("reg_32");

        // NOTE : 테스트 파일 변경 시 변경 필요
        assertThat(ecotourismList.get(0).getPublicIdentifyCode(), is("ectr_49"));
    }

    @Test
    public void test_get_tour_list_by_region_keyword() {

    }

    @Test
    public void test_get_tour_list_by_program_intro_keyword() {

    }

    @Test
    public void test_get_frequency_in_program_detail() {

    }

    @Test
    public void test_get_tour_by_recommend() {

    }
}