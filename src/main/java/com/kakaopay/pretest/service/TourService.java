package com.kakaopay.pretest.service;

import org.springframework.web.multipart.MultipartFile;

public interface TourService {
    boolean uploadFile(MultipartFile ecotourismFile);
    boolean addTour(String[] tourInfoArr);
}