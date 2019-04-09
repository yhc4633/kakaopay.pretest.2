package com.kakaopay.pretest.service.impl;

import com.kakaopay.pretest.persistence.entity.impl.Member;
import com.kakaopay.pretest.persistence.repository.MemberRepository;
import com.kakaopay.pretest.service.CertificationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Data
@Service("jwtCertificationService")
@AllArgsConstructor
public class JwtCertificationServiceImpl implements CertificationService {
    private final MemberRepository memberRepository;

    @Override
    public String signUp(String id, String password) {
        if (StringUtils.isAnyEmpty(id, password)) {
            return null;
        }

        Member member = memberRepository.findOne(id);

        if (member != null) {
            return null;
        }

        String encodePassword = encodePassword(password);

        memberRepository.save(new Member(id, encodePassword));

        return generateToken(id);
    }

    private String encodePassword(String password){
        String jwtString = Jwts.builder()
                .setSubject(password)
                .signWith(SignatureAlgorithm.HS512, "passwordSecretKey")
                .compact();
        return jwtString;
    }

    private String generateToken(String id){
        if (StringUtils.isBlank(id)) {
            return null;
        }

        String jwtString = Jwts.builder()
                .setSubject("id/" + id)
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("generateDate", System.currentTimeMillis())
                .setExpiration(new Date(System.currentTimeMillis() + (3600000 * 24)))
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS512, "tokenSecretKey")
                .compact();
        return jwtString;
    }

    @Override
    public String signIn(String id, String password) {
        if (StringUtils.isAnyEmpty(id, password)) {
            return null;
        }

        Member member = memberRepository.findOne(id);

        if (member == null) {
            return null;
        }

        String encodePassword = encodePassword(password);

        if (StringUtils.equals(member.getPassword(), encodePassword) == false) {
            return null;
        }

        return generateToken(id);
    }

    @Override
    public String refresh(String token) {
        return refreshToken(token);
    }

    private String refreshToken(String token) {
        Jws<Claims> claims = getClaims(token);

        if (claims == null) {
            return null;
        }

        String id = (String)claims.getBody().get("id");
        return generateToken(id);
    }

    @Override
    public Jws<Claims> getClaims(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey("tokenSecretKey")
                    .parseClaimsJws(token);

            return claims;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}