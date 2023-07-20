package com.refrigerator.springboot.config;


import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.dto.MemberDto;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.SocialRepository;
import com.refrigerator.springboot.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private SocialService socialService;
    @Autowired
    private SocialRepository socialRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/member/login")

                .usernameParameter("email")
                .failureUrl("/member/error")
                .successHandler(new CustomAuthenticationSuccessHandler2())
                .and()
                .oauth2Login()
                .successHandler(new CustomAuthenticationSuccessHandler())
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/");

        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .mvcMatchers("/", "/member/**","/recipeBoard/","/recipeBoard/recipeView","/cookBoard/","/cookBoard/CookBoardDetail", "/auth/**","/main/**","/random").permitAll()
                .mvcMatchers("/css/**", "/js/**","/img/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            MemberDto dto = (MemberDto) session.getAttribute("user");
            Optional<Member> member = socialRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());

            if (member.isPresent()) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect("/member/socialCreate");
            }
        }
    }
    public class CustomAuthenticationSuccessHandler2 implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
           response.sendRedirect("/");

        }
    }
}