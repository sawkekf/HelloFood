package com.refrigerator.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.refrigerator.springboot.dto.CookBoardDTO;
import com.refrigerator.springboot.dto.CookSearchDTO;
import com.refrigerator.springboot.dto.FormDataDTO;
import com.refrigerator.springboot.dto.MemberDto;
import com.refrigerator.springboot.dto.MypageCommentDTO;
import com.refrigerator.springboot.dto.RecipeSearchDTO;
import com.refrigerator.springboot.dto.ShowBoardDTO;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.repository.CookBoardRepository;
import com.refrigerator.springboot.repository.MemImgRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.RecipeBoardRepository;
import com.refrigerator.springboot.service.CookBoardService;
import com.refrigerator.springboot.service.FileService;
import com.refrigerator.springboot.service.InsertService;
import com.refrigerator.springboot.service.MemberService;
import com.refrigerator.springboot.service.MypageService;
import com.refrigerator.springboot.service.NicknameService;
import com.refrigerator.springboot.service.PasswordService;
import com.refrigerator.springboot.service.ProfileService;

import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class myPageController {

	final RecipeBoardRepository recipeBoardRepository;
	final CookBoardRepository cookBoardRepository;
	final MemberRepository memberRepository;
	final MemberService memberService;
	final ProfileService profileService;
	final MypageService mypageService;
	final NicknameService nicknameService;
	final FileService fileService;
	final CookBoardService cookBoardService;
	final MemImgRepository memImgRepository;
	final BCryptPasswordEncoder encoder;
	final InsertService insertService;

	@GetMapping("/")
	public String mypage01(Model model, HttpServletRequest request){
		//principal
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		MemberImg memberImg = memImgRepository.findByMember(member);
		model.addAttribute("memberImg", memberImg);
		model.addAttribute("member", member);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		return "mypage/mypage01";
	}

	@GetMapping("/profile")
	public String mypage02(Model model, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(dto.getEmail(),dto.getLoginType());
		MemberImg memberImg = memImgRepository.findByMember(member);
		dto.setPassword(member.getPw());
		dto.setNickname(member.getNickname());
		HttpSession session = request.getSession();
		session.setAttribute("user", dto);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		model.addAttribute("memberImg", memberImg);
		model.addAttribute("member", member);
		model.addAttribute("name",member.getName());
		model.addAttribute("nickname",member.getNickname());
		model.addAttribute("email",member.getEmail());
		model.addAttribute("loginType", member.getLoginType());
		return "mypage/mypage02";

	}

	@GetMapping("/mywriting")
	public String recipeWritings(Model model, Optional<Integer>page, RecipeSearchDTO recipeSearchDTO, HttpServletRequest request){
		try {
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,5);
			Page<ShowBoardDTO> recipeBoards = recipeBoardRepository.myPageDelCheckIsNAndSearch(recipeSearchDTO, pageable, member);

			if(recipeBoards.getNumberOfElements()==0) {
				model.addAttribute("nonRecipeWriting", "등록된 게시물이 없습니다");
			}
			model.addAttribute("member", member);
			model.addAttribute("recipeBoards", recipeBoards);
			model.addAttribute("now", LocalDateTime.now());
			model.addAttribute("recipeSearchDTO", recipeSearchDTO);
			model.addAttribute("maxPage",10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return"mypage/myWritings";
	}
	@GetMapping("/cookwriting")
	public String cookWritings(Model model, Optional<Integer>page, CookSearchDTO cookSearchDTO, HttpServletRequest request){
		try {
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,5);
			Page<CookBoardDTO> cookBoards = cookBoardRepository.myPageDelCheckIsNAndSearch(cookSearchDTO, pageable, member);
			if(cookBoards.getNumberOfElements()==0) {
				model.addAttribute("nonCookWriting", "등록된 게시물이 없습니다");
			}
			model.addAttribute("member", member);
			model.addAttribute("cookBoards", cookBoards);
			model.addAttribute("now", LocalDateTime.now());
			model.addAttribute("cookSearchDTO", cookSearchDTO);
			model.addAttribute("maxPage",10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return"mypage/myCookWriting";
	}

	@GetMapping("/myComment")
	public String myComment(Model model, Optional<Integer>page, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,4);
		System.out.println(pageable+"페이저블");
		Page<MypageCommentDTO> mypageRecipeCommentDTO = recipeBoardRepository.getMyRecipeComment(member, pageable);
		if(mypageRecipeCommentDTO.getNumberOfElements()==0) {
			model.addAttribute("noRecipeComment", "작성한 댓글이 없습니다.");
		}
		Page<MypageCommentDTO> mypageCookCommentDTO = cookBoardRepository.getMyCookComment(member, pageable);
		if(mypageCookCommentDTO.getNumberOfElements()==0) {
			model.addAttribute("noCookComment", "작성한 댓글이 없습니다.");
		}
		System.out.println(mypageRecipeCommentDTO+"왜안댐");
		MemberImg memberImg = memImgRepository.findByMember(member);
		List<BigTags> biList = insertService.getBiList();
		model.addAttribute("biList",biList);
		model.addAttribute("memberImg", memberImg);
		model.addAttribute("mypageRecipeCommentDTO", mypageRecipeCommentDTO);
		model.addAttribute("mypageCookCommentDTO", mypageCookCommentDTO);
		model.addAttribute("maxPage",10);
		model.addAttribute("member", member);

		return"mypage/myComments";
	}

	@PostMapping("/myCommentResendPopUp")
	public String myCommentResendPopUp(Model model, @RequestParam("commentId")Long commentId, @RequestParam("content")String content, @RequestParam("boardId") Long boardId, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		System.out.println(commentId+"댓글아뒤");
		System.out.println(content+"컨텐츠");
		model.addAttribute("member", member);
		model.addAttribute("commentId", commentId);
		model.addAttribute("content", content);
		model.addAttribute("boardId", boardId);
		return"mypage/myCommentResend";
	}

	@GetMapping("/recipeBook")
	public String recipeBook(Model model, Optional<Integer>page, HttpServletRequest request, RecipeSearchDTO recipeSearchDTO) {
		try {
			Member member = Member.guestMem();
			MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
			if(dto!=null) {
				String email = dto.getEmail();
				member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
			}
			Pageable pageable = PageRequest.of(page.isPresent()? page.get() :0,5);
			Page<ShowBoardDTO> recipeBoards = recipeBoardRepository.myPageRecipeBook(pageable, member, recipeSearchDTO);
			if(recipeBoards.getNumberOfElements()==0) {
				model.addAttribute("nonRecipeWriting", "등록된 게시물이 없습니다");
			}
			MemberImg memberImg = memImgRepository.findByMember(member);
			List<BigTags> biList = insertService.getBiList();
			model.addAttribute("biList",biList);
			model.addAttribute("memberImg", memberImg);
			model.addAttribute("member", member);
			model.addAttribute("recipeBoards", recipeBoards);
			model.addAttribute("now", LocalDateTime.now());
			model.addAttribute("maxPage",10);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return"mypage/recipeBook";
	}

	@Autowired
	private PasswordService  passwordService;

	@GetMapping("/myPage/password")
	public String updatePassword(
			@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword,
			Principal principal, Model model
	) {
		if (!passwordService.checkCurrentPassword(principal.getName(), currentPassword)) {
			model.addAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
			return "redirect:/";
		}
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "redirect:/";
		}
		passwordService.changePassword(principal.getName(), newPassword);
		System.out.println(currentPassword);
		System.out.println(newPassword);
		System.out.println(confirmPassword);
		return "/LoginForm";
	}

	@PostMapping("/changeNickname")
	@ResponseBody
	public ResponseEntity<String> changeNick(@RequestParam("newNickname") String newNickname, HttpServletRequest request) throws UnsupportedEncodingException{
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(dto.getEmail(), dto.getLoginType());
		String decodeNickname = URLDecoder.decode(newNickname, "UTF-8");
		String replaceStr = decodeNickname.replace("=", "");
		member.setNickname(replaceStr);
		memberRepository.save(member);
		return ResponseEntity.ok("");
	}

	@PostMapping("/myPage/profile")
	public @ResponseBody ResponseEntity<String> updateProfile(@ModelAttribute FormDataDTO formData) {
		MultipartFile image = formData.getImage();
		String url = mypageService.saveTemporaryImage2(image);
		return ResponseEntity.ok(url);
	}
	@PostMapping("/mypage/updateProfileImage")
	public @ResponseBody ResponseEntity<String> updateProfileImage(@ModelAttribute FormDataDTO formData, HttpServletRequest request) {
		Member member = Member.guestMem();
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		if(dto!=null) {
			String email = dto.getEmail();
			member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		}
		MultipartFile image = formData.getImage();
		mypageService.updateProfileImage(image, member);
		return ResponseEntity.ok("");
	}

	@PostMapping("/checkPassword")
	public @ResponseBody ResponseEntity<String> checkPassword(@RequestParam("currentPassword")String currentPassword, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		boolean matches = encoder.matches(currentPassword, dto.getPassword());
		if(matches) {
			return ResponseEntity.ok("true");
		}else {
			return ResponseEntity.ok("false");
		}
	}
	@PostMapping("/setNewPassword")
	public @ResponseBody ResponseEntity<String> setNewPassword(@RequestParam("newPassword") String newPassword, HttpServletRequest request){
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(dto.getEmail(), dto.getLoginType());
		newPassword = encoder.encode(newPassword);
		member.setPw(newPassword);
		memberRepository.save(member);
		return ResponseEntity.ok("");
	}
	@GetMapping("/passwordChange")
	public String passwordChange(HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(dto.getEmail(), dto.getLoginType());
		return "mypage/passwordChange";
	}

}
