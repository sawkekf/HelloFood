package com.refrigerator.springboot.controller;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.constant.Role;
import com.refrigerator.springboot.dto.MemberDto;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.entity.Refrigerator;
import com.refrigerator.springboot.repository.MemImgRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.RefRepository;
import com.refrigerator.springboot.service.MemberService;
import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder encoder;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final HttpSession session;
    private final RefRepository refRepository;
    private final MemImgRepository memImgRepository;

    @GetMapping(value = "/member/new")
    public String memberForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        MemberDto dto = (MemberDto) session.getAttribute("user");
        if (dto != null) {
            model.addAttribute("userEmail", dto.getEmail());
            model.addAttribute("userNickname", dto.getNickname());
            model.addAttribute("loginType", dto.getLoginType());
        }

        return "/member/login/createForm";
    }

    @PostMapping(value = "/member/new")
    public String newMember(MemberDto memberFormDto, Model model) {
        try {
            Member member = Member.createMember(memberFormDto, encoder);
            memberService.saveMember(member);
            Refrigerator refrigerator = new Refrigerator(member);
            refRepository.save(refrigerator);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/login/createForm";
        }
        return "/member/login/loginForm";
    }

    @GetMapping(value = "/member/login")
    public String loginPage() {
        return "/member/login/loginForm";
    }

    @GetMapping(value = "/member/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/login/errorForm";
    }

    @GetMapping(value = "/member/findPW")
    public String findPasswordPage() {

        return "/member/find/findPW";
    }

    @GetMapping(value = "/member/changePW")
    public String changePasswordPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sName = (String) request.getSession().getAttribute("name");
        if(sName ==null){

            alert(response,"잘못된 경로입니다.");
        }
        return "/member/find/changePW";
    }
    public static void alert(HttpServletResponse response, String msg) {
        try {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter w = response.getWriter();
            w.write("<script>alert('"+msg+"');</script>");
            w.write("<script>location.href='/member/login'</script>");
            w.flush();
            w.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/member/changePW", method = {RequestMethod.POST})
    @ResponseBody
    public void changePassword(@RequestBody String newPassword, HttpServletRequest request) throws UnsupportedEncodingException {
        String sName = (String) request.getSession().getAttribute("name");
        String sEmail = (String) request.getSession().getAttribute("email");

        Member member = memberRepository.findByEmailAndLoginTypeAndAndName(sEmail, LoginType.NOMAL, sName);

        String decodedPassword = URLDecoder.decode(newPassword, "UTF-8");
        String changeStr = decodedPassword.replace("=", "");
        System.out.println(changeStr);
        member.setPw(encoder.encode(changeStr));
        memberRepository.save(member);
    }

    @GetMapping(value = "/member/findEmail")
    public String findEmailPage() {
        return "/member/find/findEmail";
    }

    @GetMapping(value = "/member/socialCreate")
    public String socialCreatePage(HttpServletRequest request, Model model){
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        String email = dto.getEmail();
        String nickname = dto.getNickname();
        String strNickname = nickname;
        if(nickname.contains(" ")) {
            strNickname = nickname.replaceAll(" ", "");
        }
        model.addAttribute("email", email);
        model.addAttribute("nickname", strNickname);
        return "/member/social/socialForm";
    }

    @PostMapping(value = "/member/socialCreate")
    public String socialCreate(HttpServletRequest request){
        Member member = new Member();
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        String nickname = request.getParameter("nickname");
        member.setEmail(dto.getEmail());
        member.setName(dto.getName());
        member.setNickname(nickname);
        member.setRole(Role.USER);
        member.setLoginType(dto.getLoginType());
        memberRepository.save(member);

        Refrigerator refrigerator = new Refrigerator(member);
        refRepository.save(refrigerator);

        return "/member/login/loginForm";
    }
    @PostMapping(value = "/member/changFirstImg")
    public @ResponseBody ResponseEntity changFirstImg(HttpServletRequest request, Principal principal)  {
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = Member.guestMem();

            if (dto == null) {
                member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
            } else {
                member = memberRepository.findByEmailAndLoginType(dto.getEmail(), dto.getLoginType());
            }
        MemberImg memberImg = memImgRepository.findByMember(member);

        memImgRepository.delete(memberImg);

        return new ResponseEntity(HttpStatus.OK);
    }
}