package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefRepository extends JpaRepository<Refrigerator,Long> {


    @Query(value = "select * from refrigerator s where s.mem_id = :name", nativeQuery = true)
    Refrigerator findByMember_Id(@Param("name")Long id);

}
