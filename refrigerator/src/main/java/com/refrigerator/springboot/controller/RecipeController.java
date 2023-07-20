package com.refrigerator.springboot.controller;

import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.*;
import com.refrigerator.springboot.repository.*;
import com.refrigerator.springboot.service.InsertService;
import com.refrigerator.springboot.service.RecipeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;


import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequiredArgsConstructor
@RequestMapping("/refrigeratorRecipe")
public class RecipeController {
    private final RecipeApiService recipeApiService;
    private final InsertService insertService;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeContentRepository recipeContentRepository;
    private final RecipeIngedientRepository recipeIngedientRepository;
    private final IngRepositoryCusImp ingRepositoryCusImp;
    private final RefRepository refRepository;

    @GetMapping(value ="/")
    public String recipe(HttpServletRequest request ,Model model,Principal principal){
        Member member = Member.guestMem();
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");

        if(dto==null && principal.getName()==null){
            return "redirect:/loginCheck";
        }
        else {
            if(dto == null){
                member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
            }
            else{
                member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
            }



            Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
            List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());
            LocalDate date = LocalDate.now();


            List<BigTags> biList = insertService.getBiList();
            List<RecipeApiDTO3> recipeApiDTO3s= new ArrayList<>();
            List<RecipeApiDTO> recipeLists = recipeApiService.getRecipeData();
            List<RecipeBoard> recipeBoards = recipeBoardRepository.findAllByDelCheckIsN();
            for(int i =0;i<recipeLists.size();i++){
                RecipeApiDTO3 dto3 = new RecipeApiDTO3(recipeLists.get(i));
                String[] count = recipeLists.get(i).getRcp_part().split(",");
                dto3.setCount_str(0+"/"+count.length);
                recipeApiDTO3s.add(dto3);
            }

            for(RecipeBoard recipeBoard : recipeBoards) {
                RecipeApiDTO3 recipeApiDTO3 = new RecipeApiDTO3();
                String content = "";
                String ingredient = "";
                int num = 1;
                List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
                List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
                for(RecipeContent recipeContent : recipeContents) {
                    content += num+". "+recipeContent.getContent()+"+";
                    num++;
                }
                for(RecipeIngredient recipeIngredient : recipeIngredients) {
                    ingredient += recipeIngredient.getIngrediant()+"("+recipeIngredient.getIngrediantVol()+"),";
                }
                recipeApiDTO3.setId(recipeBoard.getWritingid());
                recipeApiDTO3.setRep_nm(recipeBoard.getTitle());
                recipeApiDTO3.setInfo_eng(Double.toString(recipeBoard.getKcal()));
                recipeApiDTO3.setManual(content);
                recipeApiDTO3.setRcp_part(ingredient);
                String[] count = ingredient.split(",");
                recipeApiDTO3.setCount_str(0+"/"+count.length);
                recipeApiDTO3s.add(recipeApiDTO3);
            }
            List<IngreBigDto> inglist2 = new ArrayList<>();
            for(IngreBigDto dto4 : inglist){
                if(dto4.getIng_deadline()==null){
                    inglist2.add(dto4);
                }
                else if(ChronoUnit.DAYS.between(date, dto4.getIng_deadline())>=0){
                    inglist2.add(dto4);
                }
                else{

                }
            }
            List<RecipeApiDTO3> recipeApiDTO4 = new ArrayList<>();
            for(RecipeApiDTO3 dto3 :recipeApiDTO3s){
                for(IngreBigDto indto1 :inglist2){
                    if(dto3.getRcp_part().indexOf(indto1.getSmallTags())!=-1){
                        recipeApiDTO4.add(dto3);

                    }
                }
            }





            System.out.println(recipeLists.size());
            model.addAttribute("member", member);
            model.addAttribute("recipeLists",recipeApiDTO4);
            model.addAttribute("IngList",inglist);
            model.addAttribute("biList",biList);


            return "refrigeratorRecipe/recipe";
        }
    }


    @PostMapping(value = "/ing") //재료를 선택한 경우 선택한 재료가 포함 되어 있는 레시피를 불러온다.
    public @ResponseBody ResponseEntity ingDetaisl(@RequestBody List<String> ids, Model model){
        List<RecipeApiDTO3> recipeLists = recipeApiService.getRecipeDatas(ids);
        List<RecipeApiDTO3> recipeLists2= new ArrayList<>();
        List<String> slists2 = new ArrayList<>();
        for(String s: ids) {
            String s1 = ingRepositoryCusImp.getIngs2(s).getSmallTag().getTag_small_name(); //해당하는 아이디를 가진 재료를 가져온다.
            slists2.add(s1);
        }
        HashSet<String>Hlist = new HashSet<>(slists2); //중복 제거
        List<String>sslist = new ArrayList<>(Hlist);
        for(int i =0;i<recipeLists.size();i++){
            int count=0;
            for(String s1: sslist) {
                if (recipeLists.get(i).getRcp_part().indexOf(s1) != -1) { //포함되어있을 경우 카운트 증가
                    count++;
                }
            }
            String[] slist = recipeLists.get(i).getRcp_part().split(","); // 쉼표를 기준으로 자른 뒤 총 재료의 개수를 구한다.
            recipeLists.get(i).setCount_str(count+"/"+ slist.length);
            recipeLists.get(i).setCount(count);
        }
        for(RecipeApiDTO3 dto3 :recipeLists){
            if(dto3.getCount()!=0){ //count가 0인 경우들을 제외
                recipeLists2.add(dto3);
            }
        }

        return  new ResponseEntity<List<RecipeApiDTO3>>(recipeLists2,HttpStatus.OK);
    }
    @PostMapping(value = "/ing/selects")
    public @ResponseBody ResponseEntity selectx( Model model,HttpServletRequest request,Principal principal){

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

        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());
        List<BigTags> biList = insertService.getBiList();
        List<RecipeApiDTO3> recipeApiDTO3s= new ArrayList<>();
        List<RecipeApiDTO> recipeLists = recipeApiService.getRecipeData();
        List<RecipeBoard> recipeBoards = recipeBoardRepository.findAllByDelCheckIsN();
        for(int i =0;i<recipeLists.size();i++){
            RecipeApiDTO3 dto3 = new RecipeApiDTO3(recipeLists.get(i));
            String[] count = recipeLists.get(i).getRcp_part().split(",");
            dto3.setCount_str(0+"/"+count.length);
            recipeApiDTO3s.add(dto3);
        }
        LocalDate date = LocalDate.now();
        List<IngreBigDto> inglist2 = new ArrayList<>();
        for(IngreBigDto dto4 : inglist){

            if(dto4.getIng_deadline()==null){
                inglist2.add(dto4);
            }
            else if(ChronoUnit.DAYS.between(date, dto4.getIng_deadline())>=0){
                inglist2.add(dto4);
            }
            else{

            }
        }
        for(RecipeBoard recipeBoard : recipeBoards) {
            RecipeApiDTO3 recipeApiDTO3 = new RecipeApiDTO3();
            String content = "";
            String ingredient = "";
            int num = 1;
            List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
            List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
            for(RecipeContent recipeContent : recipeContents) {
                content += num+". "+recipeContent.getContent()+"+";
                num++;
            }
            for(RecipeIngredient recipeIngredient : recipeIngredients) {
                ingredient += recipeIngredient.getIngrediant()+"("+recipeIngredient.getIngrediantVol()+"),";
            }
            recipeApiDTO3.setId(recipeBoard.getWritingid());
            recipeApiDTO3.setRep_nm(recipeBoard.getTitle());
            recipeApiDTO3.setInfo_eng(Double.toString(recipeBoard.getKcal()));
            recipeApiDTO3.setManual(content);
            recipeApiDTO3.setRcp_part(ingredient);
            String[] count = ingredient.split(",");
            recipeApiDTO3.setCount_str(0+"/"+count.length);
            recipeApiDTO3s.add(recipeApiDTO3);
        }
        List<RecipeApiDTO3> recipeApiDTO4 = new ArrayList<>();
        for(RecipeApiDTO3 dto3 :recipeApiDTO3s){
            for(IngreBigDto indto1 :inglist2){
                if(dto3.getRcp_part().indexOf(indto1.getSmallTags())!=-1){
                    recipeApiDTO4.add(dto3);
                    break;
                }
            }
        }


        return  new ResponseEntity<List<RecipeApiDTO3>>( recipeApiDTO4,HttpStatus.OK);
    }

    @PostMapping(value = "/finish")
    public @ResponseBody ResponseEntity finish(@RequestBody TwoDto twoDto, Model model,HttpServletRequest request,Principal principal){


        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
        Member member = new Member();
        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(),LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }
        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());

        System.out.println(twoDto.toString());
        List<IngreBigDto> inglist = new ArrayList<>();

        for(String s: twoDto.getInglist()) {
            IngreBigDto ingreBigDto =  insertService.getIngStr3(s);
            if(twoDto.getIngsmall().contains(ingreBigDto.getSmallTags())){
                inglist.add(ingreBigDto);
            }
        }
        return  new ResponseEntity<List<IngreBigDto>>(inglist, HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public @ResponseBody ResponseEntity deleteIng( @RequestBody String ids,Model model){

        insertService.DeleteIngs(ids);



        return  new ResponseEntity( HttpStatus.OK);
    }

    @GetMapping("/blog")
    @ResponseBody
    public List<BlogDTO> blogRecipe(@RequestParam("recipeName") String recipeName){

        List<BlogDTO> blogList =recipeApiService.searchBlogRecipe(recipeName);
        return blogList;

    }

    @GetMapping("/video")
    @ResponseBody
    public List<VideoDTO> videoRecipe(String recipeName){

        List<VideoDTO> videoList =recipeApiService.searchvideoRecipe(recipeName);
        return videoList;
    }
    @GetMapping("/choice")
    @ResponseBody
    public RecipeApiDTO RecipeDetail(Long id){

        RecipeApiDTO recipeDetails =recipeApiService.recipeDetails(id);
        return recipeDetails;
    }

    @GetMapping("/api/recipe/set")
    @ResponseBody
    public String recipeSet(){
        recipeApiService.recipeApiSave();
        return "sucess";
    }

    @GetMapping("/inglist")
    @ResponseBody
    public ResponseEntity inglists (HttpServletRequest request ,Model model,Principal principal){
        Member member = Member.guestMem();
        MemberDto dto = (MemberDto) request.getSession().getAttribute("user");


        if(dto == null){
            member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
        }
        else{
            member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
        }

        Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());


        return  new ResponseEntity<List<IngreBigDto>>(inglist,HttpStatus.OK);


    }
    @GetMapping("/recplist")
    @ResponseBody
    public ResponseEntity recplist (HttpServletRequest request ,Model model,Principal principal){
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

        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());
        List<BigTags> biList = insertService.getBiList();
        List<RecipeApiDTO3> recipeApiDTO3s= new ArrayList<>();
        List<RecipeApiDTO> recipeLists = recipeApiService.getRecipeData();
        List<RecipeBoard> recipeBoards = recipeBoardRepository.findAllByDelCheckIsN();
        for(int i =0;i<recipeLists.size();i++){
            RecipeApiDTO3 dto3 = new RecipeApiDTO3(recipeLists.get(i));
            String[] count = recipeLists.get(i).getRcp_part().split(",");
            dto3.setCount_str(0+"/"+count.length);
            recipeApiDTO3s.add(dto3);
        }

        for(RecipeBoard recipeBoard : recipeBoards) {
            RecipeApiDTO3 recipeApiDTO3 = new RecipeApiDTO3();
            String content = "";
            String ingredient = "";
            int num = 1;
            List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
            List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
            for(RecipeContent recipeContent : recipeContents) {
                content += num+". "+recipeContent.getContent()+"+";
                num++;
            }
            for(RecipeIngredient recipeIngredient : recipeIngredients) {
                ingredient += recipeIngredient.getIngrediant()+"("+recipeIngredient.getIngrediantVol()+"),";
            }
            recipeApiDTO3.setId(recipeBoard.getWritingid());
            recipeApiDTO3.setRep_nm(recipeBoard.getTitle());
            recipeApiDTO3.setInfo_eng(Double.toString(recipeBoard.getKcal()));
            recipeApiDTO3.setManual(content);
            recipeApiDTO3.setRcp_part(ingredient);
            String[] count = ingredient.split(",");
            recipeApiDTO3.setCount_str(0+"/"+count.length);
            recipeApiDTO3s.add(recipeApiDTO3);
        }
        LocalDate date = LocalDate.now();
        List<IngreBigDto> inglist2 = new ArrayList<>();
        for(IngreBigDto dto4 : inglist){

            if(dto4.getIng_deadline()==null){
                inglist2.add(dto4);
            }
            else if(ChronoUnit.DAYS.between(date, dto4.getIng_deadline())>=0){
                inglist2.add(dto4);
            }
            else{

            }
        }
        List<RecipeApiDTO3> recipeApiDTO4 = new ArrayList<>();
        for(RecipeApiDTO3 dto3 :recipeApiDTO3s){
            for(IngreBigDto indto1 :inglist2){
                if(dto3.getRcp_part().indexOf(indto1.getSmallTags())!=-1){
                    recipeApiDTO4.add(dto3);
                    break;
                }
            }
        }


        return  new ResponseEntity<List<RecipeApiDTO3>>( recipeApiDTO4,HttpStatus.OK);
    }

    @PostMapping(value = "/input")
    public @ResponseBody ResponseEntity Inputrecipe( @RequestBody String datass,HttpServletRequest request ,Model model,Principal principal){
        MemberDto dto2 = (MemberDto) request.getSession().getAttribute("user");
      Member member = new Member();
            if(dto2 == null){
                member = memberRepository.findByEmailAndLoginType(principal.getName(), LoginType.NOMAL);
            }
            else{
                member = memberRepository.findByEmailAndLoginType(dto2.getEmail(),dto2.getLoginType());
            }
            Refrigerator refrigerator = refRepository.findByMember_Id(member.getId());
            List<RecipeApiDTO3> recipeApiDTO3s= new ArrayList<>();
        List<RecipeApiDTO3> recipeApiDTo= new ArrayList<>();
            List<RecipeApiDTO> recipeLists = recipeApiService.getRecipeData();
            List<RecipeBoard> recipeBoards = recipeBoardRepository.findAllByDelCheckIsN();
        List<IngreBigDto> inglist =insertService.getAllIng(refrigerator.getId());
            for(int i =0;i<recipeLists.size();i++){
                RecipeApiDTO3 dto3 = new RecipeApiDTO3(recipeLists.get(i));
                String[] count = recipeLists.get(i).getRcp_part().split(",");
                dto3.setCount_str(0+"/"+count.length);
                recipeApiDTO3s.add(dto3);
            }
            for(RecipeBoard recipeBoard : recipeBoards) {
                RecipeApiDTO3 recipeApiDTO3 = new RecipeApiDTO3();
                String content = "";
                String ingredient = "";
                int num = 1;
                List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
                List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
                for(RecipeContent recipeContent : recipeContents) {
                    content += num+". "+recipeContent.getContent()+"+";
                    num++;
                }
                for(RecipeIngredient recipeIngredient : recipeIngredients) {
                    ingredient += recipeIngredient.getIngrediant()+"("+recipeIngredient.getIngrediantVol()+"),";
                }
                recipeApiDTO3.setId(recipeBoard.getWritingid());
                recipeApiDTO3.setRep_nm(recipeBoard.getTitle());
                recipeApiDTO3.setInfo_eng(Double.toString(recipeBoard.getKcal()));
                recipeApiDTO3.setManual(content);
                recipeApiDTO3.setRcp_part(ingredient);
                String[] count = ingredient.split(",");
                recipeApiDTO3.setCount_str(0+"/"+count.length);
                recipeApiDTO3s.add(recipeApiDTO3);
            }
        for(RecipeApiDTO3 dto :recipeApiDTO3s){
            if(dto.getRep_nm().contains(datass)){
                recipeApiDTo.add(dto);
            }
        }
        LocalDate date = LocalDate.now();
        List<IngreBigDto> inglist2 = new ArrayList<>();
        for(IngreBigDto dto4 : inglist){

            if(dto4.getIng_deadline()==null){
                inglist2.add(dto4);
            }
            else if(ChronoUnit.DAYS.between(date, dto4.getIng_deadline())>=0){
                inglist2.add(dto4);
            }
            else{
            }
        }
        List<RecipeApiDTO3> recipeApiDTO4 = new ArrayList<>();
        for(RecipeApiDTO3 dto3 :recipeApiDTo){
            for(IngreBigDto indto1 :inglist2){
                if(dto3.getRcp_part().indexOf(indto1.getSmallTags())!=-1){
                    recipeApiDTO4.add(dto3);
                    break;
                }
            }
        }
            System.out.println(recipeLists.size());
            model.addAttribute("recipeLists",recipeApiDTo);
        return  new ResponseEntity<List<RecipeApiDTO3>>(recipeApiDTO4, HttpStatus.OK);
    }



    public static String getOnlyDigit(String str){

        StringBuffer sb=new StringBuffer();

        if(str!=null && str.length()!=0){

            Pattern p =  Pattern.compile("[가-힣]");

            Matcher m = p.matcher(str);

            while(m.find()){

                sb.append(m.group());

            }

        }

        return sb.toString();

    }

}

