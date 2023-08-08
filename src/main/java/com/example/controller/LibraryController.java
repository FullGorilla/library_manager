package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Library;
import com.example.entity.Logs;
import com.example.service.LibraryService;
import com.example.service.LoginUser;
import com.example.service.LogsService;

@Controller
@RequestMapping("library")
public class LibraryController {
	
	private final LibraryService libraryService;
    private final LogsService logsService;


	@Autowired
	public LibraryController(LibraryService libraryService, LogsService logsService) {
		this.libraryService = libraryService;
        this.logsService = logsService;		
	}
	
    @GetMapping
    public String index(Model model) {
    	List<Library> libraries = this.libraryService.findAll();
    	model.addAttribute("libraries", libraries);
        return "library/index";
    }
    
    @GetMapping("borrow")
    public String borrowingForm(@RequestParam("id") Integer id, Model model) {
    	
    	Optional<Library> optionalLibrary = this.libraryService.findById(id);
    	
    	 Library library = optionalLibrary.get();
         model.addAttribute("library", library);
         return "library/borrowingForm";
    }
    
    @PostMapping("borrow")
    public String borrow(@RequestParam("id") Integer id, @RequestParam("return_due_date") String returnDueDate, @AuthenticationPrincipal LoginUser loginUser) {
        
        Optional<Library> optionalLibrary = libraryService.findById(id);
        Library library = optionalLibrary.get();
        
        library.setUserId(loginUser.getId());
        libraryService.save(library);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        
        Logs logs = new Logs();
        logs.setLibraryId(library.getId());
        logs.setUserId(loginUser.getId());
        logs.setRentDate(LocalDateTime.now());
        logs.setReturnDueDate(LocalDateTime.parse(returnDueDate + "T00:00:00", formatter));
        logs.setReturnDate(null);
        logsService.save(logs);
        
        return "redirect:/library";
    }
    
    @PostMapping("return")
    public String returnBook(@RequestParam("id") Integer id, @AuthenticationPrincipal LoginUser loginUser) {
    	
        Optional<Library> optionalLibrary = libraryService.findById(id);
        Library library = optionalLibrary.get();

        library.setUserId(0);
        libraryService.save(library);
        
        Logs latestLog = logsService.findTopByLibraryIdAndUserIdOrderByRentDateDesc(library.getId(), loginUser.getId());
        latestLog.setReturnDate(LocalDateTime.now());
        logsService.save(latestLog);
        
        return "redirect:/library";
    }
    
    @GetMapping("history")
    public String history(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        List<Logs> borrowHistory = logsService.findByUserIdOrderByRentDateDesc(loginUser.getId());

        model.addAttribute("borrowHistory", borrowHistory);
    	return "library/borrowHistory";
    }
}