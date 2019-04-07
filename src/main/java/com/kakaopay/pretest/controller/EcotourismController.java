package com.kakaopay.pretest.controller;

import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.model.request.ModifyEcotourismRequest;
import com.kakaopay.pretest.model.response.*;
import com.kakaopay.pretest.persistence.entity.impl.Ecotourism;
import com.kakaopay.pretest.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.kakaopay.pretest.constants.ParameterCode.Headers.TRANSACTION_ID;
import static com.kakaopay.pretest.constants.ResponseCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ecotourism")
public class EcotourismController {
    @Qualifier("ecotourismService")
    private final TourService ecotourismService;

    @PostMapping(value = "/file/register")
    public RegisterEcotourismFileResponse registerFile(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                       @RequestParam(value = "ecotourismFile") MultipartFile ecotourismFile) {

        List<String> failedList = ecotourismService.uploadFile(ecotourismFile);

        return new RegisterEcotourismFileResponse(new ResponseHeader(transactionId, SUCCESS), failedList);
    }

    @GetMapping(value = "/tour/search", params = {"regionCode"})
    public EcotourismResponse search(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                     @RequestParam(value = "regionCode") String regionCode) {

        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionCode(regionCode);

        return new EcotourismResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }

    @PostMapping(value = "/tour/register")
    public ProcessResultResponse register(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                          @RequestParam(value = "programName", defaultValue = "") String programName,
                                          @RequestParam(value = "theme", defaultValue = "") String theme,
                                          @RequestParam(value = "region", defaultValue = "") String region,
                                          @RequestParam(value = "programIntro", defaultValue = "") String programIntro,
                                          @RequestParam(value = "programDetail", defaultValue = "") String programDetail) {
        String[] tourInfoArr = {programName, theme, region, programIntro, programDetail};

        int resultCode = ecotourismService.addTour(tourInfoArr);

        return new ProcessResultResponse(new ResponseHeader(transactionId, SUCCESS), resultCode);
    }

    @PutMapping(value = "/tour/modify")
    public ProcessResultResponse modifyEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                  @RequestBody ModifyEcotourismRequest modifyEcotourismRequest) {

        String[] tourInfoArr = {modifyEcotourismRequest.getEcotourismCode(), modifyEcotourismRequest.getProgramName(), modifyEcotourismRequest.getTheme()
                , modifyEcotourismRequest.getRegion(), modifyEcotourismRequest.getProgramIntro(), modifyEcotourismRequest.getProgramDetail()};

        int resultCode = ecotourismService.modifyTour(tourInfoArr);

        return new ProcessResultResponse(new ResponseHeader(transactionId, SUCCESS), resultCode);
    }

    @GetMapping(value = "/tour/search", params = {"regionKeyword"})
    public SummaryEcotourismResponse searchSummary(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                   @RequestParam(value = "regionKeyword", defaultValue = "") String regionKeyword) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionKeyword(regionKeyword);

        return new SummaryEcotourismResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }

    @GetMapping(value = "/tour/search", params = {"programIntroKeyword"})
    public FrequentEcotourismRegionResponse searchRegionFrequency(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                                  @RequestParam(value = "programIntroKeyword", defaultValue = "") String programIntroKeyword) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByProgramIntroKeyword(programIntroKeyword);

        return new FrequentEcotourismRegionResponse(new ResponseHeader(transactionId, SUCCESS), programIntroKeyword, ecotourismList);
    }

    @GetMapping(value = "/tour/search", params = {"programDetailKeyword"})
    public FrequentEcotourismProgramDetailResponse searchProgramDetailFrequency(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                                                @RequestParam(value = "programDetailKeyword", defaultValue = "") String programDetailKeyword) {

        Integer count = ecotourismService.getFrequencyInProgramDetail(programDetailKeyword);

        return new FrequentEcotourismProgramDetailResponse(new ResponseHeader(transactionId, SUCCESS), programDetailKeyword, count);
    }
}