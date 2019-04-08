package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.StartApplicationServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplicationServer.class)
public class EcotourismRepositoryCustomTest {
    @Autowired
    private EcotourismRepositoryCustom ecotourismRepositoryCustom;


}