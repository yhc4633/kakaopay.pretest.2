package com.kakaopay.pretest.service;

import com.kakaopay.pretest.StartApplicationServer;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.persistence.repository.EcotourismRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
    private EcotourismRepository ecotourismRepository;

    // NOTE : 테스트 파일의 데이터 row 수 변경 시 변경 필요
    private final int TEST_FILE_SIZE = 110;

    @Before
    public void initialize() {
        BufferedReader br = null;

        try {
            File file = new File("src/test/resources/file/2017년_국립공원_생태관광_정보.csv");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("testFile", file.getName(), "text/plain", IOUtils.toByteArray(input));

            ecotourismService.uploadFile(multipartFile);
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
    public void test_upload_file() {
        List<Ecotourism> ecotourismList = ecotourismRepository.findAll();

        assertThat(ecotourismList.size(), is(TEST_FILE_SIZE));
    }

    @Test
    public void test_add_tour() {
        String[] ecotourismArr = {"1", "직업을 겪어보자", "직업 체험", "경기도 성남시 판교", "직업 체험 프로그램", "직업을 체험합니다"};

        ecotourismService.addTour(ecotourismArr);

        List<Ecotourism> ecotourismList = ecotourismRepository.findAll();

        Ecotourism ecotourism = ecotourismList.get(ecotourismList.size()-1);

        assertThat(ecotourismList.size(), is(TEST_FILE_SIZE + 1));
        assertThat(ecotourism.getProgram().getName(), is("직업을 겪어보자"));
    }

    @Test
    public void test_modify_tour() {
        List<Ecotourism> ecotourismList = ecotourismRepository.findAll();

        Ecotourism originEcotourism = ecotourismList.get(0);





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