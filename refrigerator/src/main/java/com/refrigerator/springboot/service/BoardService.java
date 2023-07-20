package com.refrigerator.springboot.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.refrigerator.springboot.dto.MemberDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.refrigerator.springboot.dto.DetailViewDTO;
import com.refrigerator.springboot.dto.RewriteDTO;
import com.refrigerator.springboot.dto.WriteFormDTO;
import com.refrigerator.springboot.constant.Delcheck;
import com.refrigerator.springboot.constant.RepImage;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.entity.RecipeComment;
import com.refrigerator.springboot.entity.RecipeContent;
import com.refrigerator.springboot.entity.RecipeFollowWriting;
import com.refrigerator.springboot.entity.RecipeImage;
import com.refrigerator.springboot.entity.RecipeIngredient;
import com.refrigerator.springboot.entity.RecipeRecommend;
import com.refrigerator.springboot.entity.TemporaryImage;
import com.refrigerator.springboot.repository.BoardRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.RecipeBoardRepository;
import com.refrigerator.springboot.repository.RecipeCommentRepository;
import com.refrigerator.springboot.repository.RecipeContentRepository;
import com.refrigerator.springboot.repository.RecipeFollow;
import com.refrigerator.springboot.repository.RecipeImageRepository;
import com.refrigerator.springboot.repository.RecipeIngedientRepository;
import com.refrigerator.springboot.repository.RecipeRecommendRepository;
import com.refrigerator.springboot.repository.TemporaryImageRepository;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class BoardService {

	final FileService fileService;
	final MemberRepository memberRepository;
	final RecipeBoardRepository recipeBoardRepository;
	final RecipeContentRepository recipeContentRepository;
	final BoardRepository boardRepository;
	final RecipeImageRepository recipeImageRepository;
	final RecipeRecommendRepository recipeRecommendRepository;
	final RecipeFollow recipeFollow;
	final RecipeCommentRepository recipeCommentRepository;
	final RecipeIngedientRepository recipeIngedientRepository;
	final TemporaryImageRepository temporaryImageRepository;

	public void Recipewriting(WriteFormDTO writeFormDTO, String email, Long boardid, List<MultipartFile> recipeImages, HttpServletRequest request) {

		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		Board board = boardRepository.findByBoardid(boardid);
		List<String>ingrediants = writeFormDTO.getIngrediant();
		List<String>ingrediantVols = writeFormDTO.getIngrediantVol();
		System.out.println(board);
		System.out.println("체크");
		Integer writingCount = recipeBoardRepository.findByAllWriting();
		if(writingCount==null) {
			writingCount=1;
		}else if(writingCount!=null) {
			writingCount = recipeBoardRepository.findByAllWriting()+1;
		}
		writeFormDTO.setMember(member);
		writeFormDTO.setBoard(board);
		RecipeBoard newOne=RecipeBoard.writing(writeFormDTO,writingCount);
		recipeBoardRepository.save(newOne);
		List<String>recipeContents = writeFormDTO.getRecipeContent();
		ArrayList<String> urls = new ArrayList<>();

		for(int i=0; i<recipeImages.size(); i++) {
			RecipeImage recipeImage = new RecipeImage();
			recipeImage.setRecipeboard(newOne);
			recipeImage.setRepimage("N");
			if(i==0) {
				recipeImage.setRepimage("Y");
			}
			String url = this.saveRecipeImage(recipeImage, recipeImages.get(i));
			if(i>0) {
				urls.add(url);
			}
		}

		List<RecipeContent> newContents = RecipeContent.createContents(recipeContents, newOne, urls);
		for(RecipeContent Content :newContents) {
			recipeContentRepository.save(Content);
		}

		List<RecipeIngredient> recipeIngredients = RecipeIngredient.newRecipeIngredient(ingrediants, ingrediantVols, newOne);
		for(RecipeIngredient recipeIngredient : recipeIngredients) {
			recipeIngedientRepository.save(recipeIngredient);
		}


	}

	public String saveRecipeImage(RecipeImage recipeImage, MultipartFile Image) {
		String uploadPath = "C:/storage/project";
		String oriName = Image.getOriginalFilename();
		String imgName = "";
		String url = "";
		try {
			if(!StringUtils.isEmpty(oriName)) {
				imgName = fileService.uploadFile(uploadPath, oriName, Image.getBytes());
				url="/main/project/"+imgName;
			}

			recipeImage.saveRecipeImage(imgName, oriName, url);
			recipeImageRepository.save(recipeImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public void deleteUpdate(String email, Long writingId,HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		RecipeBoard recipeBoard = recipeBoardRepository.findByRecipeDetail(writingId);
		if(member==recipeBoard.getMember()) {
			recipeBoard.setDelcheck(Delcheck.Y);
			recipeBoardRepository.save(recipeBoard);
		}
		List<RecipeBoard> recipeBoards = recipeBoardRepository.findAllByDelCheckIsNAndASC();
		for(int i=0; i<recipeBoards.size(); i++) {
			RecipeBoard rep = recipeBoards.get(i);
			rep.setWritingcount(i+1);
			recipeBoardRepository.save(rep);
		}
	}

	public void RecipeRewriting(WriteFormDTO writeFormDTO, String id, Long boardId, List<MultipartFile> recipeImages, Long writingId, String imgDelCheck, String delCon) {

		RecipeBoard recipeBoard =recipeBoardRepository.findByRecipeDetail(writingId);
		recipeBoard.updateWriting(writeFormDTO);
		List<String>ingredients = writeFormDTO.getIngrediant();
		List<String>ingredientVols = writeFormDTO.getIngrediantVol();
		List<RecipeIngredient> postRecipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
		List<RecipeContent> ContentsList = recipeContentRepository.findByRecipeContent(recipeBoard);
		List<RecipeImage> ImageList = recipeImageRepository.findRecipeImagesByWritingId(recipeBoard);
		List<RecipeIngredient> recipeIngredients = RecipeIngredient.newRecipeIngredient(ingredients, ingredientVols, recipeBoard);
		List<String>recipeContents = writeFormDTO.getRecipeContent();
		String[] delChecks = imgDelCheck.split(",");
		String[] delCons = delCon.split(",");
		ArrayList<String> urls = new ArrayList<>();
		ArrayList<String> postUrls = new ArrayList<>();
		System.out.println(delCons[0]+"델콘~");
		//System.out.println(delCons[1]);
		for(int i=0; i<delCons.length; i++) {
			String delC = delCons[i];
			if(!delC.equals("")) {
				Long del = Long.parseLong(delC);
				RecipeContent content = recipeContentRepository.findBycontentId(del);
				recipeContentRepository.delete(content);
			}
		}

		for(int i=0; i<recipeImages.size(); i++) {
			RecipeImage recipeImage = new RecipeImage();
			recipeImage.setRecipeboard(recipeBoard);
			recipeImage.setRepimage("N");
			String url = this.saveRecipeImage(recipeImage, recipeImages.get(i));
			if(i==0 && !url.equals("")) {
				System.out.println();
				RecipeImage postRepImage = recipeImageRepository.findRepImage(recipeBoard);
				recipeImageRepository.delete(postRepImage);
				recipeImage.setRepimage("Y");
			}else if(i==0 && url.equals("") && delChecks[i].equals("Y")) {
				RecipeImage postRepImage = recipeImageRepository.findRepImage(recipeBoard);
				recipeImageRepository.delete(postRepImage);
				recipeImage.setRepimage("Y");
			}else if(i>0) {
				urls.add(url);
			}
		}

		//System.out.println(urls.get(1)+"유알엘즈");
		//System.out.println(delChecks[2]+"델체쿠");
		for(int i=0; i<recipeContents.size(); i++) {
			if(i<ContentsList.size()) {
				RecipeContent content = ContentsList.get(i);
				content.setContent(recipeContents.get(i));
				if(!urls.get(i).equals("")) {
					content.setUrl(urls.get(i));
					recipeContentRepository.save(content);
				}else if(urls.get(i).equals("") && delChecks[i+1].equals("Y")) {
					content.setUrl(urls.get(i));
					recipeContentRepository.save(content);
				}
				recipeContentRepository.save(content);
			}else {
				RecipeContent newContent = new RecipeContent();
				newContent.setContent(recipeContents.get(i));
				newContent.setUrl(urls.get(i));
				newContent.setRecipeboard(recipeBoard);
				recipeContentRepository.save(newContent);
			}
		}

		for(RecipeContent recipecontent : ContentsList) {
			String content = recipecontent.getUrl();
			postUrls.add(content);
		}

		for(RecipeIngredient recipeIngredient : postRecipeIngredients) {
			recipeIngedientRepository.delete(recipeIngredient);
		}

		for(RecipeIngredient recipeIngredient : recipeIngredients) {
			recipeIngedientRepository.save(recipeIngredient);
		}

//		for(int i=0; i<ImageList.size(); i++) {
//			if(!postUrls.contains(ImageList.get(i).getUrl())) {
//				recipeImageRepository.delete(ImageList.get(i));
//			}
//			if(ImageList.get(i).getUrl().equals("")&&ImageList.get(i).getRepimage().equals("N")) {
//				recipeImageRepository.delete(ImageList.get(i));
//			}
//		}
	}

	public void updateSeenCount(DetailViewDTO detailViewDTO) {
		RecipeBoard recipeBoard = detailViewDTO.getRecipeBoard();
		int seenCount = recipeBoard.getSeencount()+1;
		recipeBoard.setSeencount(seenCount);
		recipeBoardRepository.save(recipeBoard);
	}

	public boolean updateRecomCount(DetailViewDTO detailViewDTO, Member member) {
		RecipeBoard recipeBoard = detailViewDTO.getRecipeBoard();
		if(recipeRecommendRepository.findByRecipeBoardAndMember(recipeBoard, member)==0) {
			RecipeRecommend recipeRecommend = RecipeRecommend.updateRecommend(recipeBoard, member);
			recipeRecommendRepository.save(recipeRecommend);
			int recomCount = recipeRecommendRepository.countRecommend(recipeBoard);
			recipeBoard.setRecomcount(recomCount);
			recipeBoardRepository.save(recipeBoard);
			return true;
		}
		return false;
	}
	public boolean updateRecomCount2(DetailViewDTO detailViewDTO, Member member) {
		RecipeBoard recipeBoard = detailViewDTO.getRecipeBoard();
		if(recipeRecommendRepository.findByRecipeBoardAndMember(recipeBoard, member)==0) {

			return true;
		}
		return false;
	}
	public RewriteDTO getRewriteValue(Long writingId,HttpServletRequest request) {

		RecipeBoard recipeBoard = recipeBoardRepository.findByRecipeDetail(writingId);
		List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
		RecipeImage recipeImages= recipeImageRepository.findRepImage(recipeBoard);
		List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
		RewriteDTO rewriteDTO = new RewriteDTO();
		rewriteDTO.setRecipeBoard(recipeBoard);
		rewriteDTO.setRecipeContents(recipeContents);
		rewriteDTO.setRecipeImage(recipeImages);
		rewriteDTO.setRecipeIngredients(recipeIngredients);
		return rewriteDTO;

	}

	public void updateFollow(String email, Long writingId,HttpServletRequest request) {

		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		RecipeBoard recipeBoard = recipeBoardRepository.findByRecipeDetail(writingId);
		RecipeFollowWriting recipeFollowWriting = new RecipeFollowWriting();
		if(recipeFollow.findFollowing(member, recipeBoard)==0) {
			recipeFollowWriting.setMember(member);
			recipeFollowWriting.setRecipeboard(recipeBoard);
			recipeFollow.save(recipeFollowWriting);
		}else if(recipeFollow.findFollowing(member, recipeBoard)!=0) {
			recipeFollow.delete(recipeFollow.findToDel(member, recipeBoard));
		}

	}

	public DetailViewDTO getDetailView(Long writingid) {
		DetailViewDTO detailViewDTO = new DetailViewDTO();
		System.out.println(writingid);
		System.out.println("체크");
		RecipeBoard recipeBoard = recipeBoardRepository.findByRecipeDetail(writingid);
		List<RecipeContent> recipeContents = recipeContentRepository.findByRecipeContent(recipeBoard);
		RecipeImage recipeImages = recipeImageRepository.findRepImage(recipeBoard);
		List<RecipeIngredient> recipeIngredients = recipeIngedientRepository.findByrecipeBoard(recipeBoard);
		detailViewDTO.setRecipeBoard(recipeBoard);
		detailViewDTO.setRecipeContents(recipeContents);
		detailViewDTO.setRecipeImage(recipeImages);
		detailViewDTO.setRecipeIngredients(recipeIngredients);
		return detailViewDTO;
	}

	public boolean deleteComment(Long commentId, String email,HttpServletRequest request) {
		MemberDto dto = (MemberDto) request.getSession().getAttribute("user");
		Member member = memberRepository.findByEmailAndLoginType(email,dto.getLoginType());
		RecipeComment recipeComment = recipeCommentRepository.findCommentByCommentId(commentId);
		Member commentMember = recipeComment.getMember();
		if(recipeComment!=null) {
			if(member==commentMember) {
				recipeCommentRepository.delete(recipeComment);
			}else {
				return false;
			}
		}
		return true;
	}

	public void reWriteComment(Long commentId, String comment) {
		RecipeComment recipeComment =recipeCommentRepository.findCommentByCommentId(commentId);
		recipeComment.setContent(comment);
		recipeComment.setRegdate(LocalDateTime.now());
		recipeCommentRepository.save(recipeComment);
	}

	public void removeByUrl(String oriName) {
		TemporaryImage temporaryImage =  temporaryImageRepository.findByUrl(oriName);
		temporaryImageRepository.delete(temporaryImage);
	}

}
