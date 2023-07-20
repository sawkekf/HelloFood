package com.refrigerator.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.refrigerator.springboot.entity.CookBoard;
import com.refrigerator.springboot.entity.CookComment;
import com.refrigerator.springboot.entity.Member;

public interface CookCommentRepository extends JpaRepository<CookComment, Long> {

    @Query("select c from CookComment c where c.cookboard =:cookboard")
    List<CookComment>findByCookBoard(@Param("cookboard") CookBoard cookboard);

    @Query("select c from CookComment c where c.commentid =:commentid")
    CookComment findByCommentId(@Param("commentid") Long commentid);
    
    @Query("select c from CookComment c where c.member =:member order by regdate desc")
    List<CookComment>findMyCookComment(@Param("member")Member member);
    
    @Query("select count(c) from CookComment c where c.member =:member")
	Long findMyCookCommentsCount(@Param("member")Member member);
}
