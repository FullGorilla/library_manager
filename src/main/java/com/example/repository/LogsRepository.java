package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Logs;


@Repository
public interface LogsRepository extends JpaRepository<Logs, Integer> {
	
    Logs findTopByLibraryIdAndUserIdOrderByRentDateDesc(Integer libraryId, Integer userId);
    List<Logs> findByUserIdOrderByRentDateDesc(Integer userId);

}