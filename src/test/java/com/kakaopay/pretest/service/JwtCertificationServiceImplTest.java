package com.kakaopay.pretest.service;

import com.kakaopay.pretest.StartApplicationServer;
import com.kakaopay.pretest.persistence.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplicationServer.class)
public class JwtCertificationServiceImplTest {
    @Autowired
    @Qualifier("jwtCertificationService")
    private CertificationService jwtCertificationService;

    @Autowired
    private MemberRepository memberRepository;

    private String id = "testUser";
    private String password = "testPassword1234!";

    @After
    public void removeAllData() {
        memberRepository.deleteAll();
    }

    @Test
    public void 가입_테스트() {
        String token = jwtCertificationService.signUp(id, password);

        assertNotNull(token);
    }

    @Test
    public void 로그인_테스트() {
        String originToken = jwtCertificationService.signUp(id, password);

        String loginToken = jwtCertificationService.signIn(id, password);

        assertNotEquals(originToken, loginToken);
    }

    @Test
    public void 토큰_재발급_테스트() {
        String originToken = jwtCertificationService.signUp(id, password);

        String refreshToken = jwtCertificationService.refresh(originToken);

        assertNotEquals(originToken, refreshToken);
    }
}