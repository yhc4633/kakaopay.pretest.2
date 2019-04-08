package com.kakaopay.pretest.interceptor;

import com.kakaopay.pretest.service.CertificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.kakaopay.pretest.constants.ParameterCode.Headers.AUTHORIZATION;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    @Qualifier("jwtCertificationService")
    private CertificationService jwtCertificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNotBlank(token) && jwtCertificationService.getClaims(token) != null) {
            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}