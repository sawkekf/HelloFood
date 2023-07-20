package com.refrigerator.springboot.controller;

import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.BoardRepository;
import com.refrigerator.springboot.repository.CookBoardRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.TemporaryImageRepository;
import com.refrigerator.springboot.service.CookBoardService;
import com.refrigerator.springboot.service.InsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("cookBoard")
public class CookBoardController {

	final MemberRepository memberRepository;
	final CookBoardService cookBoardService;
	final BoardRepository boardRepository;
	final CookBoardRepository cookBoardRepository;
	final TemporaryImageRepository temporaryImageRepository;
	private final InsertService insertService;
	@GetMapping("/CookWriting")
	public String cookWriting(Model model, CookWritingDTO cookWritingDTO,HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		Board board = boardRepository.findByBoardid(2l);
		model.addAttribute("member", member);
		model.addAttribute("board", board);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		return"board/cook/cookWriting";
	}
	@PostMapping("/doCookWriting")
	public String doCookWriting(Model model, CookWritingDTO cookWritingDTO, @RequestParam("images") List<MultipartFile>images, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		Board board = boardRepository.findByBoardid(2l);
		if(cookWritingDTO.getTitle().trim().equals("")) {
			model.addAttribute("member", member);
			model.addAttribute("board", board);
			model.addAttribute("ErrorMessage", "제목을 입력해주세요");
			return"community/ErrorPage";
		}
		if(cookWritingDTO.getContent().trim().equals("<p><br></p>")) {
			model.addAttribute("member", member);
			model.addAttribute("board", board);
			model.addAttribute("ErrorMessage", "내용을 입력해주세요");
			return"board/cook/ErrorPage";
		}
		System.out.println("cookWritingDTO");
		System.out.println(cookWritingDTO);
		cookBoardService.doCookWriting(cookWritingDTO, member, board, images);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		return"redirect:/cookBoard/";
	}

	@PostMapping("/doCookImageSet")
	public @ResponseBody ResponseEntity<String> imageSet(@ModelAttribute FormDataDTO formData) {
		MultipartFile image = formData.getImage();
		cookBoardService.saveTemporaryImage(image);
		Long maxCount = temporaryImageRepository.findMaxCount();
		String imageUrl = temporaryImageRepository.findByMaxId(maxCount);
		System.out.println("체크에용");
		System.out.println(imageUrl);
		return ResponseEntity.ok(imageUrl);
	}


	@GetMapping("/")
	public String CookBoard(Model model, Optional<Integer> page, CookSearchDTO cookSearchDTO, HttpServletRequest request) {
		try {
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			System.out.println(member);
			Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,8);
			Page<CookBoardDTO> cookBoards = cookBoardRepository.findByDelCheckIsNAndSearch(cookSearchDTO, pageable);
			if(cookBoards.getNumberOfElements()==0) {
				model.addAttribute("nonWriting", "등록된 게시물이 없습니다");
			}
			model.addAttribute("member", member);
			model.addAttribute("cookBoards", cookBoards);
			model.addAttribute("maxPage", 5);
			model.addAttribute("cookSearchDTO", cookSearchDTO);
			List<BigTags> biList = insertService.getBiList();
			model.addAttribute("biList",biList);

		}catch(Exception e){
			e.printStackTrace();
		}
		return "board/cook/CookBoard";
	}

	@GetMapping("/CookBoardDetail")
	public String cookBoardDetail(Model model, @RequestParam("writingId")Long writingId, Optional<Integer> page, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		Pageable pageable = PageRequest.of(page.isPresent()? page.get():0, 20);
		Page<CookCommentDTO> cookComment = cookBoardRepository.getCookComment(writingId, pageable);
		System.out.println(member);
		Board board = boardRepository.findByBoardid(2l);
		CookDetailViewDTO cookDetailViewDTO = cookBoardService.getDetailBoard(writingId);
		cookDetailViewDTO.setCookComment(cookComment);
		cookBoardService.updateSeenCount(cookDetailViewDTO);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		model.addAttribute("board", board);
		model.addAttribute("writingId", writingId);
		model.addAttribute("cookDetailViewDTO", cookDetailViewDTO);
		model.addAttribute("member", member);
		model.addAttribute("maxPage", 5);
		return"board/cook/CookBoardDetail";
	}

	@PostMapping("/CookCommentWriting/{writingId}")
	public @ResponseBody ResponseEntity<String> cookCommentWriting(@PathVariable("writingId") Long writingId, @RequestParam("comment")String content, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			Board board = boardRepository.findByBoardid(2l);
			cookBoardService.createComment(writingId, email, content, board);
		}

		return ResponseEntity.ok("hi");
	}

	@PostMapping("/doCookRecom/{writingId}")
	public @ResponseBody ResponseEntity<String> doCookRecom(@PathVariable("writingId")Long writingId,HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			boolean recom = cookBoardService.doCookRecom(email, writingId);
			if(recom==true) {
				return ResponseEntity.ok("true");
			}else {
				return ResponseEntity.ok("false");
			}
		}
		return ResponseEntity.ok("false");
	}
	@PostMapping("/doCookRe/{writingId}")
	public @ResponseBody ResponseEntity<String> doCookRecom2(@PathVariable("writingId")Long writingId,HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			boolean recom = cookBoardService.doCookRecom2(email, writingId);
			if(recom==true) {
				return ResponseEntity.ok("true");
			}else {
				return ResponseEntity.ok("fal");
			}
		}
		return ResponseEntity.ok("false");
	}

	@GetMapping("/CookBoardDetail/delete")
	public String delCook(@RequestParam("writingId")Long writingId, HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			cookBoardService.deleteWriting(email, writingId);
		}

		return"redirect:/cookBoard/";
	}

	@GetMapping("/rewrite")
	public String rewriteCook(@RequestParam("writingId")Long writingId, Model model, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		CookWritingDTO cookWritingDTO = cookBoardService.getCookRewrite(writingId);
		System.out.println(cookWritingDTO.getContent());
		model.addAttribute("writingId", writingId);
		model.addAttribute("member", member);
		model.addAttribute("cookWritingDTO", cookWritingDTO);
		return "board/cook/cookRewriting";
	}
	@PostMapping("/doCookreWriting/{writingId}")
	public String doRewriteCook(@PathVariable("writingId") Long writingId, CookWritingDTO cookWritingDTO, List<MultipartFile> images, @RequestParam("repUrl")String repUrl, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		cookBoardService.Rewrite(writingId ,cookWritingDTO, images, repUrl);
		return"redirect:/cookBoard/CookBoardDetail?writingId="+writingId;
	}

	@PostMapping("/CookCommentReWriting/{commentId}")
	public @ResponseBody ResponseEntity<Long> commentReWriting(@PathVariable("commentId")Long commentId, @RequestParam("content")String content, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		cookBoardService.reWriteComment(commentId, content);
		return ResponseEntity.ok(commentId);
	}
	@PostMapping("/CookCommentDelete/{commentId}")
	public @ResponseBody ResponseEntity<Long> commentDelete(@PathVariable("commentId")Long commentId, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			boolean chekck = cookBoardService.deleteComment(commentId, email);
		}
		return ResponseEntity.ok(commentId);
	}

	@PostMapping("/removeTemporalImage")
	public @ResponseBody ResponseEntity<String> removeTemporalImage(@RequestParam("oriName")String oriName){
		cookBoardService.removeTemporalImage(oriName);
		return ResponseEntity.ok("result");
	}

}
