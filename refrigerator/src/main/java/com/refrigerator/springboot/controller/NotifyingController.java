package com.refrigerator.springboot.controller;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.repository.MemImgRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.NotifyRepository;
import com.refrigerator.springboot.service.BoardService;
import com.refrigerator.springboot.service.CookBoardService;
import com.refrigerator.springboot.service.InsertService;
import com.refrigerator.springboot.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class NotifyingController {

	final NotifyService notifyService;
	final NotifyRepository notifyRepository;
	final MemberRepository memberRepository;
	final BoardService boardService;
	final CookBoardService cookBoardService;
	final MemImgRepository memImgRepository;
	final InsertService insertService;


	@PostMapping("/Notifying")
	public String notifying(Model model, @ModelAttribute NotifyingDTO notifyingDTO,HttpServletRequest request) {
		//String email = principal.getName();
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		model.addAttribute("member", member);
		model.addAttribute("notifyingDTO", notifyingDTO);
		return"admin/notify/Notifying";
	}

	@PostMapping("/doNotifying")
	public @ResponseBody ResponseEntity<String> doNotifying(DoNotifyingDTO doNotifyingDTO, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			notifyService.doNotifying(doNotifyingDTO,email);
		}

		return ResponseEntity.ok("ok");

	}

	@GetMapping("/admin/NotifiedList")
	public String notifiedList(Model model, Optional<Integer> page, HttpServletRequest request) {
		try {
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			Pageable pageable = PageRequest.of(page.isPresent() ? page.get():0,10);
			Page<NotifyListDTO> notify = notifyRepository.getNotifyList(pageable);
			if(notify.getNumberOfElements()==0) {
				model.addAttribute("nonWriting", "등록된 게시물이 없습니다");
			}
			MemberImg memberImg = memImgRepository.findByMember(member);
			List<BigTags> biList = insertService.getBiList();
			model.addAttribute("biList",biList);
			model.addAttribute("memberImg", memberImg);
			model.addAttribute("member", member);
			model.addAttribute("notifies", notify);
			model.addAttribute("maxPage",5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//List<Notify> notify = notifyRepository.findAllNotify();
		return"admin/notify/notifiedList";
	}

	@PostMapping("/MemBannAndContentDel")
	public @ResponseBody ResponseEntity<String> MemBannAndContentDel(@ModelAttribute MemBannDTO memBannDTO, HttpServletRequest request){
		Long boardCheck = memBannDTO.getBoardId();
		String id = memBannDTO.getMemMail();
		Long writingId = memBannDTO.getWritingId();
		Long commentId = memBannDTO.getCommentId();
		String removeDate = memBannDTO.getSelectDate();
		if(boardCheck.equals(1l)) {
			boardService.deleteUpdate(id,writingId,request);
			notifyService.changeBann(id, writingId, commentId,removeDate);
		}else if(boardCheck.equals(2l)) {
			cookBoardService.deleteWriting(id, writingId);
			notifyService.changeBann(id, writingId, commentId,removeDate);
		}

		return ResponseEntity.ok("MemBannAndContentDel");

	}

	@PostMapping("/MemBannAndCommentDel")
	public @ResponseBody ResponseEntity<String> MemBannAndCommentDel(@ModelAttribute MemBannDTO memBannDTO, HttpServletRequest request){

		System.out.println(memBannDTO);
		System.out.println("쳌쳌");
		Long boardCheck = memBannDTO.getBoardId();
		String id = memBannDTO.getMemMail();
		Long writingId = memBannDTO.getWritingId();
		Long commentId = memBannDTO.getCommentId();
		String removeDate = memBannDTO.getSelectDate();
		if(boardCheck.equals(1l)) {
			boardService.deleteComment(commentId ,id , request);
			notifyService.changeBann(id, writingId, commentId,removeDate);
		}else if(boardCheck.equals(2l)) {
			System.out.println("체크임니당");
			cookBoardService.deleteComment(commentId, id);
			notifyService.changeBann(id, writingId, commentId,removeDate);
		}

		return ResponseEntity.ok("MemBannAndCommentDel");

	}

	@PostMapping("/admin/notifyingCheck")
	public String notifyingCheck(NotifyingCheckDTO notifyingCheckDTO, Model model) {
		model.addAttribute("notifyingCheckDTO", notifyingCheckDTO);
		return"admin/notify/NotifyingCheck";
	}

}
