package com.kakaopay.pretest.persistence.repository;

import com.kakaopay.pretest.StartApplicationServer;
import com.kakaopay.pretest.persistence.entity.impl.Program;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplicationServer.class)
public class ProgramRepositoryTest {
    @Autowired
    private ProgramRepository programRepository;

    @After
    public void cleanup() {
        programRepository.deleteAll();
    }

    @Test
    public void test_program_insert() {
        Program program = new Program();
        program.setName("테스트 프로그램 제목");
        program.setIntro("테스트를 위한 프로그램 인트로");
        program.setDetail("테스트를 위한 프로그램 상세 정보");

        programRepository.save(program);

        List<Program> programList = programRepository.findAll();

        assertThat(programList.get(0).getName(), is(program.getName()));
        assertThat(programList.get(0).getIntro(), is(program.getIntro()));
        assertThat(programList.get(0).getDetail(), is(program.getDetail()));
    }
}