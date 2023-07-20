package com.refrigerator.springboot.controller;

import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.*;
import com.refrigerator.springboot.service.BoardService;
import com.refrigerator.springboot.service.InsertService;
import com.refrigerator.springboot.service.RecipeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("recipeBoard")
@RequiredArgsConstructor
public class RecipeBoardController {


	final BoardService boardService;
	final RecipeCommentService recipeCommentService;
	final BoardRepository boardRepository;
	final MemberRepository memberRepository;
	final RecipeBoardRepository recipeBoardRepository;
	final RecipeContentRepository recipeContentRepository;
	final RecipeImageRepository recipeImageRepository;
	final RecipeCommentRepository recipecommentRepository;
	final RecipeFollow recipeFollow;
	private final InsertService insertService;

	@GetMapping("/recipeWrite")
	public String letsWrite(Model model, WriteFormDTO writeFormDTO, HttpServletRequest request) {

		Board board = boardRepository.findByBoardid(1l);
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}

		model.addAttribute("board",board);
		model.addAttribute("member",member);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);

		return "board/recipe/writing";

	}

	@PostMapping("/doRecipeWrite")
	public String doRecipeWrite(WriteFormDTO writeFormDTO, @RequestParam("recipeImages") List<MultipartFile> recipeImages, HttpServletRequest request) {
		System.out.println(writeFormDTO);
		System.out.println(recipeImages);
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			Long boardId = 1l;
			boardService.Recipewriting(writeFormDTO, email, boardId, recipeImages,request);
		}
		return "redirect:/recipeBoard/";
	}

	@GetMapping("/")
	public String recipeBoard(Model model, Optional<Integer> page, RecipeSearchDTO recipeSearchDTO, HttpServletRequest request) {
		try {
			Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,10);
			Page<ShowBoardDTO> recipeBoards = recipeBoardRepository.findByDelCheckIsNAndSearch(recipeSearchDTO, pageable);
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			List<ShowBoardDTO> bestRecipe = recipeBoardRepository.bestRecipe();
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			if(recipeBoards.getNumberOfElements()==0) {
				model.addAttribute("nonWriting", "등록된 게시물이 없습니다");
			}
			List<BigTags> biList = insertService.getBiList();
			model.addAttribute("biList",biList);

			model.addAttribute("member", member);
			model.addAttribute("recipeBoards", recipeBoards);
			model.addAttribute("bestRecipe", bestRecipe);
			model.addAttribute("now", LocalDateTime.now());
			model.addAttribute("recipeSearchDTO", recipeSearchDTO);

			model.addAttribute("maxPage",10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/recipe/recipeBoard";
	}
	@GetMapping("/recipeView")
	public String recipeBoardDetail(@RequestParam("writingId")Long writingId, Model model,Optional<Integer> page ,WriteCommentDTO writeCommentDTO, HttpServletRequest request) {

		Member member = Member.guestMem();
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,20);
		Page<RecipeCommentDTO> recipeCommentDTOS = recipeBoardRepository.getRecipeComment(writingId, pageable);
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		Board board = boardRepository.findByBoardid(1l);
		DetailViewDTO detailViewDTO = boardService.getDetailView(writingId);
		detailViewDTO.setRecipeComments(recipeCommentDTOS);
		boardService.updateSeenCount(detailViewDTO);
		Integer recipeFollowWriting = recipeFollow.findFollowing(member, detailViewDTO.getRecipeBoard());
		model.addAttribute("Following", recipeFollowWriting);
		model.addAttribute("member", member);
		model.addAttribute("detailViewDTO", detailViewDTO);
		model.addAttribute("board", board);
		model.addAttribute("writingId",writingId);
		model.addAttribute("maxPage",10);

		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);

		return "board/recipe/recipeBoardDetail";

	}
	@PostMapping("/recipeCommentWriting/{writingId}")
	public @ResponseBody ResponseEntity<Long> recipeCommentWriting( @PathVariable("writingId")Long writingId, @RequestParam("comment") String comment, HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			Board board = boardRepository.findByBoardid(1l);
			recipeCommentService.writeComment(board, writingId, comment, email);
		}
		return ResponseEntity.ok(writingId);
	}
	@GetMapping("/recipeView/delete")
	public String delete(@RequestParam("writingId")Long writingId, Model model, HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			boardService.deleteUpdate(email,writingId, request);
		}
		return "redirect:/recipeBoard/";
	}
	@GetMapping("/rewrite")
	public String rewrite(@RequestParam("writingId")Long writingId, Model model, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		RewriteDTO rewriteDTO = boardService.getRewriteValue(writingId, request);
		model.addAttribute("rewriteDTO", rewriteDTO);
		model.addAttribute("member", member);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);

		return "board/recipe/recipeRewriting";
	}

	@PostMapping("/doRecipeReWrite")
	public String doRecipeReWrite(WriteFormDTO writeFormDTO, @RequestParam("recipeImages") List<MultipartFile> recipeImages, @RequestParam("writingId")Long writingId
			, @RequestParam("imgdelCheck")String imgdelCheck, @RequestParam("delCon")String delCon, HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			Long boardId = 1l;
			boardService.RecipeRewriting(writeFormDTO, email, boardId, recipeImages, writingId, imgdelCheck, delCon);
		}
		return "redirect:/recipeBoard/";
	}

	@PostMapping("/updateRecomCount/{writingId}")
	public @ResponseBody ResponseEntity<String> updateRecomCount(@PathVariable("writingId") Long writingId, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		DetailViewDTO detailViewDTO = boardService.getDetailView(writingId);
		boolean updateRecom = boardService.updateRecomCount(detailViewDTO, member);
		if(updateRecom==true) {
			return ResponseEntity.ok("true");
		}else {
			return ResponseEntity.ok("false");
		}
	}
	@PostMapping("/updateRecom/{writingId}")
	public @ResponseBody ResponseEntity<String> updateRecomCount2(@PathVariable("writingId") Long writingId, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		DetailViewDTO detailViewDTO = boardService.getDetailView(writingId);
		boolean updateRecom = boardService.updateRecomCount2(detailViewDTO, member);
		if(updateRecom==true) {
			return ResponseEntity.ok("true");
		}else {
			return ResponseEntity.ok("false");
		}
	}
	@PostMapping("/updateFollow/{writingId}")
	public @ResponseBody ResponseEntity<String> updateFollow(@PathVariable("writingId") Long writingId, HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			boardService.updateFollow(email, writingId, request);
		}
		return ResponseEntity.ok("result");
	}

	@PostMapping("/recipeCommentDelete/{commentId}")
	public @ResponseBody ResponseEntity<String> deleteRecCom(@PathVariable("commentId") Long commentId, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			boolean deleteComment = boardService.deleteComment(commentId, email, request);
			if(deleteComment==true) {
				return ResponseEntity.ok("true");
			}else {
				return ResponseEntity.ok("false");
			}
		}
		return ResponseEntity.ok("false");
	}

	@PostMapping("/recipeCommentResend/{commentId}")
	public @ResponseBody ResponseEntity<Long> recipeReComment(@PathVariable("commentId")Long commentId, @RequestParam("content")String comment,HttpServletRequest request){
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		boardService.reWriteComment(commentId, comment);
		return ResponseEntity.ok(commentId);
	}

	@PostMapping("/removeimagebyurl")
	public @ResponseBody ResponseEntity<String> removeImageByUrl(@RequestParam("oriName")String oriName){
		boardService.removeByUrl(oriName);
		return ResponseEntity.ok("");
	}

}
