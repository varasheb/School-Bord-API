package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.Scheduleld;
@Repository
public interface ScheduleldRepository extends JpaRepository<Scheduleld, Integer> {

}
