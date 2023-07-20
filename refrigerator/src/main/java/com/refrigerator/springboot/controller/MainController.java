package com.refrigerator.springboot.controller;

import com.refrigerator.springboot.dto.BardDTO;
import com.refrigerator.springboot.dto.MemberDto;
import com.refrigerator.springboot.service.RandomService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MainController {
	
    private final RandomService randomService;
    
    @PostMapping(value = "/random")
    @ResponseBody
    public BardDTO randomRecipe(){
        BardDTO bardDTO = randomService.recipeCreate();

        return bardDTO;
    } 
}