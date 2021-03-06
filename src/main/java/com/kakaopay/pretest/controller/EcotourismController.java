package com.kakaopay.pretest.controller;

import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.model.request.EcotourismRequest;
import com.kakaopay.pretest.model.response.*;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.service.TourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.Headers.AUTHORIZATION;
import static com.kakaopay.pretest.constants.ParameterCode.Headers.TRANSACTION_ID;
import static com.kakaopay.pretest.constants.ResponseCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ecotourism")
@Api(tags="Ecotourism Controller", consumes = "application/json", produces = "application/json")
public class EcotourismController {
    @Qualifier("ecotourismService")
    private final TourService ecotourismService;

    @PostMapping(value = "/file/register")
    @ApiOperation(value = "registerFile", notes = "데이터 파일을 통해 저장 api")
    public RegisterEcotourismFileResponse registerFile(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                       @RequestHeader(value = AUTHORIZATION) String authorization,
                                                       @RequestParam(value = "ecotourismFile") MultipartFile ecotourismFile) {
        List<String> failedList = ecotourismService.uploadFile(ecotourismFile);

        return new RegisterEcotourismFileResponse(new ResponseHeader(transactionId, SUCCESS), failedList);
    }

    @GetMapping(value = "/tour/search")
    @ApiOperation(value = "search", notes = "테이터 조회 api")
    public EcotourismResponse search(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                     @RequestHeader(value = AUTHORIZATION) String authorization,
                                     @RequestParam(value = "regionCode") String regionCode) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionCode(regionCode);

        return new EcotourismResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }

    @PostMapping(value = "/tour/register")
    @ApiOperation(value = "register", notes = "테이터 추가 api")
    public ProcessResultResponse register(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                          @RequestHeader(value = AUTHORIZATION) String authorization,
                                          @RequestBody EcotourismRequest ecotourismRequest) {
        int resultCode = ecotourismService.addTour(ecotourismRequest.createTourInfoArr());

        return new ProcessResultResponse(new ResponseHeader(transactionId, SUCCESS), resultCode);
    }

    @PutMapping(value = "/tour/modify")
    @ApiOperation(value = "modifyEcotourism", notes = "테이터 수정 api")
    public ProcessResultResponse modifyEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                  @RequestHeader(value = AUTHORIZATION) String authorization,
                                                  @RequestBody EcotourismRequest ecotourismRequest) {
        int resultCode = ecotourismService.modifyTour(ecotourismRequest.createTourInfoArr());

        return new ProcessResultResponse(new ResponseHeader(transactionId, SUCCESS), resultCode);
    }

    @GetMapping(value = "/tour/summary")
    @ApiOperation(value = "searchSummary", notes = "서비스 지역 키워드로 프로그램명, 테마 출력 api")
    public SummaryEcotourismResponse searchSummary(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                   @RequestHeader(value = AUTHORIZATION) String authorization,
                                                   @RequestParam(value = "regionKeyword", defaultValue = "") String regionKeyword) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionKeyword(regionKeyword);

        return new SummaryEcotourismResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }

    @GetMapping(value = "/tour/frequency")
    @ApiOperation(value = "searchRegionFrequency", notes = "프로그램 소개 키워드로 서비스 지역 정보, 개수 출력 api")
    public FrequentEcotourismRegionResponse searchRegionFrequency(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                                  @RequestHeader(value = AUTHORIZATION) String authorization,
                                                                  @RequestParam(value = "programIntroKeyword", defaultValue = "") String programIntroKeyword) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByProgramIntroKeyword(programIntroKeyword);

        return new FrequentEcotourismRegionResponse(new ResponseHeader(transactionId, SUCCESS), programIntroKeyword, ecotourismList);
    }

    @GetMapping(value = "/program/frequency")
    @ApiOperation(value = "searchProgramDetailFrequency", notes = "프로그램 상세 정보 키워드로 키워드의 출현 빈도 수 출력 api")
    public FrequentEcotourismProgramDetailResponse searchProgramDetailFrequency(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                                                @RequestHeader(value = AUTHORIZATION) String authorization,
                                                                                @RequestParam(value = "programDetailKeyword", defaultValue = "") String programDetailKeyword) {
        Integer count = ecotourismService.getFrequencyInProgramDetail(programDetailKeyword);

        return new FrequentEcotourismProgramDetailResponse(new ResponseHeader(transactionId, SUCCESS), programDetailKeyword, count);
    }

    @GetMapping(value = "/tour/recommend")
    @ApiOperation(value = "searchRecommendEcotourism", notes = "지역명, 관광 키워드로 추천 프로그램 코드 출력 api")
    public EcotourismRecommendResponse searchRecommendEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                                 @RequestHeader(value = AUTHORIZATION) String authorization,
                                                                 @RequestParam(value = "regionKeyword", defaultValue = "") String regionKeyword,
                                                                 @RequestParam(value = "recommendKeyword", defaultValue = "") String recommendKeyword) {
        Ecotourism ecotourism = (Ecotourism) ecotourismService.getTourByRecommend(regionKeyword, recommendKeyword);

        return new EcotourismRecommendResponse(new ResponseHeader(transactionId, SUCCESS), ecotourism);
    }
}