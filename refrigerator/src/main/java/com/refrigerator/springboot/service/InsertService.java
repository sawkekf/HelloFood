package com.refrigerator.springboot.service;



import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Ingredient;
import com.refrigerator.springboot.entity.Refrigerator;
import com.refrigerator.springboot.entity.SmallTags;
import com.refrigerator.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class InsertService {

    private final TagRepository tagRepository;
    private final IngRepository ingRepository;
    private final RefRepository refRepository;
    private final IngRepositoryCusImp ingRepositoryCusImp;

    public void updateIng2(ListDetailDto paramdata, Long ref) {
        ingRepositoryCusImp.updateIng3(paramdata);
    }

    public List<SmallTags> gettagList(String name){

        List<SmallTags> tagList =  new ArrayList<>();

        tagList = tagRepository.findsmalltagsDtoList(name);
        System.out.println("dasd");
        return tagList;
    }
    public SmallTags gettag(String id){
       SmallTags smallTags = tagRepository.findbyname(id);
        return smallTags;
    }
    public boolean addIng(Selectdto selectdto, Long ref){
        String str =selectdto.getText().trim();
        System.out.println(str);
        SmallTags tags = tagRepository.findbyname(str);
        Optional<Refrigerator> refrigerator = refRepository.findById(ref);

        System.out.println(str);
        System.out.println(tags);
        System.out.println(refrigerator);

        Ingredient ingredient= Ingredient.create(tags,refrigerator.get(),selectdto.getDatae(),selectdto.getMemo());
        System.out.println(ingredient.toString());
        System.out.println(ingredient.getId());

        ingRepository.save(ingredient);

        return true;
    }
    public List<IngreBigDto> getAllIng(Long id){

         List<IngreBigDto> inglist = ingRepositoryCusImp.allList(id);

         return inglist;
    }
    public IngredientDto getIng(Long id){
        IngredientDto ingredientDto  = ingRepositoryCusImp.getIng(id);

        return ingredientDto;
    }
    public void updateIng(IngredientDto ingredientDto){
        Long count = ingRepositoryCusImp.updateIng(ingredientDto);
        if(count !=1){
            System.out.println("실패");
        }

    }
    public  List<SmallDto> getIngStr(String name){


         List<SmallDto> inglist = ingRepositoryCusImp.getLists(name);


        System.out.println(inglist.size());

         return inglist;
    }
    public void DeleteIng (String str){
        Long count = ingRepositoryCusImp.deleteIng(str);
        if(count != 1){
            System.out.println("삭제 되지않았습니다.");
        }
    }

    public void DeleteIngs(String ids) {
        Long count = ingRepositoryCusImp.deleteIngs(ids);
    }

    public List<SmallTags> getSmList() {
        List<SmallTags> smlist = ingRepositoryCusImp.getSmList();
        return smlist;
    }

    public List<BigTags> getBiList() {
        List<BigTags> bilist = ingRepositoryCusImp.getbiList();
        return bilist;

    }

    public List<SmallDto> selectBybig(String text) {
        List<SmallDto> smlist = ingRepositoryCusImp.selectByBig(text);
        return smlist;
    }

    public IngreBigDto getInfo(Long id, Long ids) {
        IngreBigDto ingredientDto = ingRepositoryCusImp.getIng2(id,ids);
        return ingredientDto;
    }



    public List<IngreBigDto> getIngList(String text, Long id) {
        List<IngreBigDto> inglist = ingRepositoryCusImp.getInglist(text,id);
        return inglist;
    }

    public IngreBigDto getIngStr2(String s) {
        Ingredient ingre = ingRepositoryCusImp.getIngs2(s);
        IngreBigDto ingreBigDto = ingRepositoryCusImp.getIngs(ingre.getId());
        return ingreBigDto;
    }
    public IngreBigDto getIngStr3(String s) {
       IngreBigDto ingreBigDto = ingRepositoryCusImp.getIngs(Long.valueOf(s));
        return ingreBigDto;
    }

}
