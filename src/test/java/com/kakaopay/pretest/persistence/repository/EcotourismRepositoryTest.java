package com.kakaopay.pretest.persistence.repository;

import lombok.AllArgsConstructor;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AllArgsConstructor
public class EcotourismRepositoryTest {
    private final EcotourismRepository ecotourismRepository;

    @After
    public void cleanup() {
        ecotourismRepository.deleteAll();
    }


}