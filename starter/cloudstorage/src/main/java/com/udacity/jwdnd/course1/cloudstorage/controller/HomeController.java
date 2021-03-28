package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private AuthenticationService authenticationService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService, EncryptionService encryptionService){
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomeView(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        model.addAttribute("file",this.fileService.getFileNames(userId));
        model.addAttribute("note",this.noteService.getNotes(userId));
        model.addAttribute("credential",this.credentialService.getCredentials(userId));
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }

}


