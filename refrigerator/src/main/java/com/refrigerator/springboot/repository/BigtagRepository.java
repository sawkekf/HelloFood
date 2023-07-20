package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.entity.BigTags;

import com.refrigerator.springboot.entity.SmallTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BigtagRepository  extends JpaRepository<BigTags,Long> {

    @Query(value = "select * from bigtag s  where s.big_name = :name", nativeQuery = true)
    BigTags findbyname(@Param("name")String name);


}
