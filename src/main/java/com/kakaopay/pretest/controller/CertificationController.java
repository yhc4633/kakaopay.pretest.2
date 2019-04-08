package com.kakaopay.pretest.controller;

import com.kakaopay.pretest.model.ResponseHeader;
import com.kakaopay.pretest.model.response.SignResponse;
import com.kakaopay.pretest.service.CertificationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import static com.kakaopay.pretest.constants.ParameterCode.Headers.*;
import static com.kakaopay.pretest.constants.ResponseCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/certify")
@Api(tags="Certification Controller", consumes = "application/json", produces = "application/json")
public class CertificationController {
    @Qualifier("jwtCertificationService")
    private final CertificationService jwtCertificationService;

    @PostMapping(value = "/signup")
    public SignResponse signUp(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                               @RequestParam(value = "id") String id,
                               @RequestParam(value = "password") String password) {
        String token = jwtCertificationService.signUp(id, password);

        return new SignResponse(new ResponseHeader(transactionId, SUCCESS), token);
    }

    @PostMapping(value = "/signin")
    public SignResponse signIn(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                               @RequestParam(value = "id") String id,
                               @RequestParam(value = "password") String password) {
        String token = jwtCertificationService.signIn(id, password);

        return new SignResponse(new ResponseHeader(transactionId, SUCCESS), token);
    }

    @PutMapping(value = "/refresh")
    public SignResponse refresh(@RequestHeader(value = TRANSACTION_ID, required = false, defaultValue = "0") String transactionId,
                                @RequestHeader(value = AUTHORIZATION) String authorization,
                                @RequestHeader(value = TOKEN) String token) {
        String refreshToken = StringUtils.equals(authorization, "Bearer Token") ? jwtCertificationService.refresh(token) : null;

        return new SignResponse(new ResponseHeader(transactionId, SUCCESS), refreshToken);
    }
}