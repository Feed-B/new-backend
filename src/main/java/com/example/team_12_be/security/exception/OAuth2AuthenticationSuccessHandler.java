package com.example.team_12_be.security.exception;

import com.example.team_12_be.security.CustomUserDetails;
import com.example.team_12_be.security.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final Environment environment;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtProvider.createToken(userDetails.getUserEmail());
        log.info("===================== onAuthenticationSuccess =================");

        String host = request.getHeader("host");
        String redirectUrl;

        log.info("failure host = " + host);

        if (host != null && host.contains("localhost")) {
            redirectUrl = environment.getProperty("redirect.base-url-local");
        } else {
            redirectUrl = environment.getProperty("redirect.base-url-prod");
        }

        redirectUrl += "?token=" + token + "&type=login";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
