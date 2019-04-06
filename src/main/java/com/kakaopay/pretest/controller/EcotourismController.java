package com.kakaopay.pretest.controller;

import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.model.extend.EcotourismResponse;
import com.kakaopay.pretest.model.extend.EcotourismSummaryResponse;
import com.kakaopay.pretest.model.extend.ProcessResultResponse;
import com.kakaopay.pretest.model.extend.RegisterFileResponse;
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
    public RegisterFileResponse registerEcotourismFile(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                       @RequestParam(value = "ecotourismFile") MultipartFile ecotourismFile) {

        List<String> failedList = ecotourismService.uploadFile(ecotourismFile);

        return new RegisterFileResponse(new ResponseHeader(transactionId, SUCCESS), failedList);
    }

    @GetMapping(value = "/region/{regionCode}/search")
    public EcotourismResponse searchEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                               @PathVariable(value = "regionCode") String regionCode) {

        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionCode(regionCode);

        return new EcotourismResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }

    @PostMapping(value = "/tour/register")
    public ProcessResultResponse registerEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                    @RequestParam(value = "programName", defaultValue = "") String programName,
                                                    @RequestParam(value = "theme", defaultValue = "") String theme,
                                                    @RequestParam(value = "region", defaultValue = "") String region,
                                                    @RequestParam(value = "programIntro", defaultValue = "") String programIntro,
                                                    @RequestParam(value = "programDetail", defaultValue = "") String programDetail) {
        String[] tourInfoArr = {programName, theme, region, programIntro, programDetail};

        int resultCode = ecotourismService.addTour(tourInfoArr);

        return new ProcessResultResponse(new ResponseHeader(transactionId, SUCCESS), resultCode);
    }

    /*@PutMapping(value = "/tour/{ecotourismCode}/modify")
    public ProcessResultResponse modifyEcotourism((@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                  ) {

    }*/

    @GetMapping(value = "/tour/summary")
    public EcotourismSummaryResponse searchEcotourismSummary(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                             @RequestParam(value = "regionKeyword", defaultValue = "") String regionKeyword) {
        List<Ecotourism> ecotourismList = ecotourismService.getTourListByRegionKeyword(regionKeyword);

        return new EcotourismSummaryResponse(new ResponseHeader(transactionId, SUCCESS), ecotourismList);
    }
}