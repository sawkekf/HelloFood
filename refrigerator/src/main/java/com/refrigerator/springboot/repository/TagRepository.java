package com.refrigerator.springboot.repository;


import com.refrigerator.springboot.entity.SmallTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TagRepository extends JpaRepository<SmallTags,Long> {
    @Query(value = "select * from SmallTag s order by s.small_id ", nativeQuery = true)
    List<SmallTags> findsmalltagsDtoList(@Param("name")String name);

    @Query(value = "select * from SmallTag s  where s.tag_small_name = :name", nativeQuery = true)
    SmallTags findbyname(@Param("name")String name);

    @Query(value = "select * from SmallTag s  where s.small_id = :id", nativeQuery = true)
    SmallTags findbyId(@Param("id")Long id);


}
