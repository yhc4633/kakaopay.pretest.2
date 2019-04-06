package com.kakaopay.pretest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TourService<T> {
    List<String> uploadFile(MultipartFile ecotourismFile);
    int addTour(String[] tourInfoArr);
    List<T> getTourListByRegionCode(String regionCode);
    List<T> getTourListByRegionKeyword(String regionKeyword);
}