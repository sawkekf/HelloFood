package com.refrigerator.springboot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.repository.MemberRepository;
import com.refrigerator.springboot.repository.RecipeBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeenCountSchedule {

	private final RecipeBoardRepository recipeBoardRepository;
	private final MemberRepository memberRepository;

	@Scheduled(cron = "0 0 10 * * *")
	public void updateExceptDate() {

		List<RecipeBoard> recipeBoards = recipeBoardRepository.findAll();
		for(RecipeBoard recipeBoard : recipeBoards) {
			recipeBoard.setExceptdaily(recipeBoard.getSeencount());
			recipeBoardRepository.save(recipeBoard);
		}

	}

	@Scheduled(cron = "* * * * * *")
	public void updateDaily() {

		List<RecipeBoard> recipeBoards = recipeBoardRepository.findAll();
		for(RecipeBoard recipeBoard : recipeBoards) {
			recipeBoard.setDailycount(recipeBoard.getSeencount()-recipeBoard.getExceptdaily());
			recipeBoardRepository.save(recipeBoard);
		}

	}

	@Scheduled(cron = "* * * * * *")
	public void removeBann() {
		List<Member> members = memberRepository.findBannedMember();
		for(Member member : members) {
			if(LocalDateTime.now().isAfter(member.getBanndate())) {
				member.setBanned("N");
			}
		}
	}



}