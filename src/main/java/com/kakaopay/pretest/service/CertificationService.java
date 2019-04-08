package com.kakaopay.pretest.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface CertificationService {
    String signUp(String id, String password);
    String signIn(String id, String password);
    String refresh(String token);
    Jws<Claims> getClaims(String token);
}
