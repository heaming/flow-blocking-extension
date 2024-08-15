package com.flow.blockingextension.controller;

import com.flow.blockingextension.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

    // user 로그인 기능 제공 시 활용
    // private final TokenService tokenService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session) {

        var cookies = request.getCookies();
        var key = "blocking-extension-token";
        var token = "";

        if(cookies != null) {
            var cookie = Arrays.stream(cookies)
                    .filter(i -> i.getName().equalsIgnoreCase(key))
                    .findFirst();
            token = cookie.orElse(new Cookie(key, session.getId())).getValue();
        } else {
            token = session.getId();
        }

        // 영구 저장을 위한 갱신
        Cookie cookie = new Cookie(key, token);
        cookie.setMaxAge(60*60*24*365);
        response.addCookie(cookie);

        // tokenService.addToken(token);

        return "index.html";
    }

}
