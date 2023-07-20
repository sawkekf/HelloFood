package com.refrigerator.springboot.controller;

import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.Refrigerator;
import com.refrigerator.springboot.entity.SmallTags;
import com.refrigerator.springboot.repository.BigtagRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.RefRepository;
import com.refrigerator.springboot.repository.TagRepository;
import com.refrigerator.springboot.service.InsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Valid
public class RefController{
    private final InsertService insertService;
    private final MemberRepository memberRepository;
    private final RefRepository refRepository;
    private final HttpSession session;
    private final TagRepository tagRepository;
    private final BigtagRepository bigTagsRepository;

    @GetMapping(value = "/")
    public String mainpage(Model model, HttpServletRequest request, Principal principal) throws UnsupportedEncodingException {
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = Member.guestMem();
        if(dto != null){
            String email = dto.getEmail();
            member = memberRepository.findmember(email,dto.getLoginType());
            System.out.println(member.getRole());
            model.addAttribute("member", member);
        }
        else if(principal != null){
            String email = principal.getName();
            MemberDto dto1 = new MemberDto();
            member = memberRepository.findmember(email,LoginType.NOMAL);
            dto1.setLoginType(member.getLoginType());
            dto1.setEmail(email);
            dto1.setName(member.getEmail());
            dto1.setRole(member.getRole());
            dto1.setNickname(member.getNickname());
            dto1.setPassword(member.getPw());
            session.setAttribute("user", dto1);
            System.out.println(member.toString());
            model.addAttribute("member", member);
        }
        else{
            member = Member.guestMem();
            System.out.println(member.getRole());
            model.addAttribute("member", member);
        }
        IngredientDto ingredientDto = new IngredientDto();
        List<SmallTags> smList = insertService.getSmList();
        List<BigTags> biList = insertService.getBiList();

        model.addAttribute("member", member);
        model.addAttribute("ingredientDto",ingredientDto);
        model.addAttribute("smList",smList);
        model.addAttribute("biList",biList);

        return "main";
    }
    @GetMapping(value = "/insert")
    public String insert(TagsDto tags, Model model){

        List<SmallTags> tagsDtoList = new ArrayList<>();
        String name = "";
        tagsDtoList = insertService.gettagList(name);
        model.addAttribute("Taglists",tagsDtoList);
        for(SmallTags tag : tagsDtoList){
            System.out.println(tag.getTag_small_name());
        }

        return "insert";
    }
    @GetMapping(value = "/main/ingList")
    public @ResponseBody ResponseEntity myInglist(HttpServletRequest request,Principal principal){
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");

        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());

        Long id = refrigerator.getId();

        List<IngreBigDto> inglist = insertService.getAllIng(id);
        System.out.println(inglist.size());


        return  new ResponseEntity<List<IngreBigDto>>(inglist, HttpStatus.OK);
    }


    @PostMapping(value = "/main/ingredient")
    public @ResponseBody  ResponseEntity ingDetail(@RequestBody Long ingId, Model model){


        IngredientDto ingredientDto = insertService.getIng(ingId);



        model.addAttribute("ingredientDto",ingredientDto);

        return  new ResponseEntity<IngredientDto>(ingredientDto,HttpStatus.OK);
    }
    @PostMapping(value = "/ingredient/detail")
    public @ResponseBody  ResponseEntity ingDetailSave(@RequestBody IngredientDto ingredientDto){

        System.out.println(ingredientDto.getTexts());

        insertService.updateIng(ingredientDto);

        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping(value = "/ingredient/create")
    public @ResponseBody  ResponseEntity insert(@RequestBody String tagName){

        List<SmallDto> inglist = insertService.getIngStr(tagName);

        return new ResponseEntity<List<SmallDto>>(inglist,HttpStatus.OK);
    }

    @PostMapping(value = "/ingredient/delete")
    public @ResponseBody  ResponseEntity DeleteIng(@RequestBody String deleteId,HttpServletRequest request ,Model model,Principal principal){


        System.out.println(deleteId);
        insertService.DeleteIng(deleteId);
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();

        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());


        return new ResponseEntity<List<IngreBigDto>>(inglist,HttpStatus.OK);
    }
    @PostMapping(value = "/ingredient/deletes")
    public @ResponseBody  ResponseEntity DeleteIngs(@RequestBody List<String> deleteId,HttpServletRequest request ,Model model,Principal principal){

        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();
        System.out.println(deleteId);
        for(String str: deleteId){
            insertService.DeleteIngs(str);
        }
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
        }
        else{
          member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());


        return new ResponseEntity<List<IngreBigDto>>(inglist,HttpStatus.OK);
    }
    @PostMapping(value = "/ingredient/mySel")
    public @ResponseBody  ResponseEntity mySel(@RequestBody String texts){

        System.out.println(texts);

        List<SmallDto> lists =  insertService.selectBybig(texts);


        return new ResponseEntity<List<SmallDto>>(lists,HttpStatus.OK);
    }
    @PostMapping(value = "/main/selectIng")
    public @ResponseBody  ResponseEntity SelectIngs(@RequestBody String text,HttpServletRequest request,Principal principal){
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }

        System.out.println();
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());


        Long id = refrigerator.getId();

        System.out.println(text);

        List<IngreBigDto> inglist = insertService.getIngList(text,id);
        System.out.println(inglist.size());

        return new ResponseEntity<List<IngreBigDto>>(inglist,HttpStatus.OK);
    }
    @PostMapping(value = "/select/save")
    public @ResponseBody  ResponseEntity SelectSave(@RequestBody List<Selectdto> paramdata,HttpServletRequest request,Principal principal){

        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");

        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        System.out.println();
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        Long id = refrigerator.getId();

        System.out.println(paramdata);
        for(Selectdto s: paramdata){
            insertService.addIng(s,id);
        }
        List<IngreBigDto> inglist =insertService.getAllIng(id);
        System.out.println(inglist.size());

        return new ResponseEntity<List<IngreBigDto>>(inglist,HttpStatus.OK);
    }
    @PostMapping(value = "/list/detail")
    public @ResponseBody  ResponseEntity listDetail(@RequestBody List<String> paramdata,HttpServletRequest request,Principal principal){

        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        Long id = refrigerator.getId();
        System.out.println(paramdata);
        List<IngreBigDto> inslit = new ArrayList<>();
        for(String s: paramdata){
            inslit.add(insertService.getInfo(Long.parseLong(s),id));

        }



        return new ResponseEntity<List<IngreBigDto>>(inslit,HttpStatus.OK);
    }
    @PostMapping(value = "/list/detailSave")
    public @ResponseBody  ResponseEntity listDetailSave(@RequestBody ListDetailDto paramdata,HttpServletRequest request,Principal principal){
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        Long id = refrigerator.getId();
        System.out.println(paramdata);

        insertService.updateIng2(paramdata,id);



        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping(value = "/createTag")
    public @ResponseBody  ResponseEntity TagSave(@RequestBody TagDto paramdata,HttpServletRequest request,Principal principal){

        System.out.println(paramdata.toString());
        BigTags bigTags = bigTagsRepository.findbyname(paramdata.getBigTag().trim());
        SmallTags smalltags = new SmallTags(paramdata.getName(),bigTags);

       tagRepository.save(smalltags);



        return new ResponseEntity(HttpStatus.OK);
    }
}
