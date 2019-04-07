package com.kakaopay.pretest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TourService<T> {
    List<String> uploadFile(MultipartFile ecotourismFile);
    Integer addTour(String[] tourInfoArr);
    Integer modifyTour(String[] tourInfoArr);
    List<T> getTourListByRegionCode(String regionCode);
    List<T> getTourListByRegionKeyword(String regionKeyword);
    List<T> getTourListByProgramIntroKeyword(String programIntroKeyword);
    Integer getFrequencyInProgramDetail(String programDetailKeyword);
}