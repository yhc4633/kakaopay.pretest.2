package com.kakaopay.pretest.controller;

import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.model.extend.EcotourismResponse;
import com.kakaopay.pretest.model.extend.ProcessResultResponse;
import com.kakaopay.pretest.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.kakaopay.pretest.constants.ParameterCode.Headers.TRANSACTION_ID;
import static com.kakaopay.pretest.constants.ResultCode.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ecotourism")
public class EcotourismController {
    @Qualifier("ecotourismService")
    private final TourService ecotourismService;

    @PostMapping(value = "/register/file", produces = "text/csv")
    public ProcessResultResponse registerEcotourismFile(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                                        @RequestParam MultipartFile ecotourismFile) {

        boolean isSuccessUploading = ecotourismService.uploadFile(ecotourismFile);

        return new ProcessResultResponse(new ResponseHeader(transactionId, OK), isSuccessUploading);
    }

    @GetMapping(value = "/search/{regionCode}")
    public EcotourismResponse searchEcotourism(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                               @PathVariable(value = "regionCode") String regionCode) {


        return new EcotourismResponse(new ResponseHeader(transactionId, OK));
    }
}