package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.service.LogsService;

public class LogsController {

    private final LogsService logsService;

    @Autowired
    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }
}
