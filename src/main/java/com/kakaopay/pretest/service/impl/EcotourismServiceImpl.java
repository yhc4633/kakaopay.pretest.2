package com.kakaopay.pretest.service.impl;

import com.kakaopay.pretest.persistence.repository.EcotourismRepository;
import com.kakaopay.pretest.service.TourService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

@Slf4j
@Data
@Service("ecotourismService")
@AllArgsConstructor
public class EcotourismServiceImpl implements TourService {
    private final EcotourismRepository ecotourismRepository;

    @Override
    public boolean uploadFile(MultipartFile ecotourismFile) {
        if (ecotourismFile == null) {
            log.error("ecotourism file is null");
            return false;
        }

        try {
            byte[] bytes = ecotourismFile.getBytes();
            ByteArrayInputStream inputFileStream = new ByteArrayInputStream(bytes);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFileStream, "euc-kr"));
            String line = "";
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
            br.close();
            return true;
        } catch (Exception e) {

            return false;
        }
    }
}