package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
	@Query("select b from Board b where boardid =:boardid")
	Board findByBoardid(@Param("boardid")Long id);
	
}
