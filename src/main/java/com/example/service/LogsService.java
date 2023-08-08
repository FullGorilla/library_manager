package com.example.service;

import java.util.List;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Logs;
import com.example.repository.LogsRepository;

@Service
public class LogsService {

    private final LogsRepository logsRepository;

    @Autowired
    public LogsService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }
    
    public List<Logs> findAll() {
    	return this.logsRepository.findAll();
    }

	public Logs save(Logs logs) {
		return logsRepository.save(logs);
	}
	
    public Logs findTopByLibraryIdAndUserIdOrderByRentDateDesc(Integer libraryId, Integer userId) {
    	return this.logsRepository.findTopByLibraryIdAndUserIdOrderByRentDateDesc(libraryId, userId);
    }
    
    public List<Logs> findByUserIdOrderByRentDateDesc(Integer userId) {
    	return this.logsRepository.findByUserIdOrderByRentDateDesc(userId);
    }
}