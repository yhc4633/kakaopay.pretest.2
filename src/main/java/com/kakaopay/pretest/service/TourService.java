package com.kakaopay.pretest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TourService<T> {
    List<String> uploadFile(MultipartFile ecotourismFile);
    boolean addTour(String[] tourInfoArr);
    List<T> getTourList(String regionCode);
}