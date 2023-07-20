package com.refrigerator.springboot.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.service.MailService;
import com.refrigerator.springboot.service.PhoneNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final MailService mailService;
    private final PhoneNumberService phoneService;
    private final MemberRepository repository;
    private final HttpSession session;
    private final MemberRepository memberRepository;

    @RequestMapping(value = "/member/idChk", method = {RequestMethod.POST})
    public @ResponseBody void memberChk(@RequestBody String email, HttpServletResponse res, HttpServletRequest req, Model model) throws IOException {
        boolean usableEmail = false;

        String[] split = email.split("%40");
        String str = split[1].replace("=", "");
        String changeStr = split[0] + "@" + str;
        try {
            String chkID = memberRepository.findByEmailAndLoginType(changeStr, LoginType.NOMAL).getEmail();
            if (chkID.equals(changeStr)) {
                usableEmail = false;
            }
        } catch (Exception e) {
            usableEmail = true;
        }

        //Json 방식으로 데이터를 만들고, [ usableEmail : value ] 로 만들어서 값 전달 후 ajax 스크립트가 동작하면서 [ usableEmail : value ] 를 인식하고 value 값에 따라서 동작
        //json 객체생성
        JSONObject jso = new JSONObject();
        //boolean 값 넣기
        jso.put("usableEmail", usableEmail);

        // 콘텐트 타입 utf-8로 지정(json은 맵 형태처럼 key, value 값을 받는대 object를 받을수 없어 직접 타입 설정)
        res.setContentType("text/html;charset=utf-8");

        //값 출력
        PrintWriter out = res.getWriter();
        //json 형태로 전달
        out.print(jso.toString());

    }

    @RequestMapping(value = "/member/nickChk", method = {RequestMethod.POST})
    public @ResponseBody void nickChk(@RequestBody String nickname, HttpServletResponse res, HttpServletRequest req, Model model) throws IOException {
        boolean result = false;
        String decodeNickname = URLDecoder.decode(nickname, "UTF-8");
        String replaceStr = decodeNickname.replace("=", "");
        try {
            String chkNick = memberRepository.findByNickname(replaceStr).getNickname();
            if (chkNick.equals(replaceStr)) {
                result = false;
            }
        } catch (Exception e) {
            System.out.println(replaceStr);
            result = true;
        }

        JSONObject jso = new JSONObject();

        jso.put("result", result);

        res.setContentType("text/html;charset=utf-8");

        PrintWriter out = res.getWriter();
        out.print(jso.toString());

    }

    @GetMapping(value = "/member/emailChk")
    public String moveEmailChk() {
        return "/auth/emailCheck";
    }

    @RequestMapping(value = "/member/mailChk", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<String> mailConfirm(@RequestParam("email") String email, @RequestParam("name") String name) throws Exception {
        Member member = repository.findByEmailAndLoginTypeAndAndName(email, LoginType.NOMAL, name);
        if (member != null) {
            String code = mailService.sendMail(email);
            session.setAttribute("email", email);
            session.setAttribute("name", name);
            return ResponseEntity.ok(code);
        } else {
            System.out.println("not found matched member");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/member/phoneNumChk")
    public String movePhoneChk() {
        return "/auth/phoneCheck";
    }

    @RequestMapping(value = "/member/phoneChk", method = {RequestMethod.POST})
    @ResponseBody
    public String phoneConfirm(@RequestBody String phoneNum) throws Exception {
        String replaceStr = phoneNum.replace("=", "");
        String code = phoneService.sendOne(replaceStr);
        System.out.println("controller : " + code);
        return code;
    }

    @RequestMapping(value = "/member/findEmailCheck", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<String> findEmailAuth(@RequestParam("name") String name, @RequestParam("phoneNum") String phoneNum, Model model) throws Exception {
        List<Member> member = repository.findByNameAndPhoneNum(name, phoneNum);
        String replaceStr = phoneNum.replace("=", "");
        String code = phoneService.sendOne(replaceStr);

        if (member.size() != 0) {
            List<String> emails = new ArrayList<>();
            for (Member members : member) {
                String email = members.getEmail();

                emails.add(email);
                System.out.println("for : " + emails);
            }
            System.out.println(emails);
            return ResponseEntity.ok(code + "/" + emails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
