package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.stream.Collectors;

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

    @PostMapping("/home")
    public String postHomeView(Authentication authentication, FileForm fileForm, NoteForm noteForm, CredentialForm credentialForm, Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        if(noteForm.getNoteTitle()!=null){
            noteForm.setUserId(userId);
            this.noteService.saveNote(noteForm);
        }
        if(credentialForm.getUrl()!=null){
            credentialForm.setUserId(userId);
            this.credentialService.saveCredential(credentialForm);
        }

        model.addAttribute("file",this.fileService.getFileNames(userId));
        model.addAttribute("note",this.noteService.getNotes(userId));
        model.addAttribute("credential", this.credentialService.getCredentials(userId));
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }

    @GetMapping(value= "home/deleteNote/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        noteService.deleteNote(noteId);

        model.addAttribute("file",this.fileService.getFileNames(userId));
        model.addAttribute("note",this.noteService.getNotes(userId));
        model.addAttribute("credential", this.credentialService.getCredentials(userId));
        return "redirect:/home";
    }

    @GetMapping(value= "home/deleteCredential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, NoteForm noteform, CredentialForm credentialForm, Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        credentialService.deleteCredential(credentialId);

        model.addAttribute("file",this.fileService.getFileNames(userId));
        model.addAttribute("note",this.noteService.getNotes(userId));
        model.addAttribute("credential", this.credentialService.getCredentials(userId));
        return "redirect:/home";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam MultipartFile fileUpload,
            Authentication authentication,
            Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        if (fileUpload.isEmpty()) {
            model.addAttribute("fileUploadError", "You must select a file to upload");
            return "result";
        }

        if (this.fileService.isFileNameAvailable(fileUpload.getOriginalFilename(), userId)) {
            try {
                Integer fileId = this.fileService.insertFile(fileUpload, userId);
                if (fileId == null) {
                    model.addAttribute("uploadError", true);
                    return "result";
                }
                model.addAttribute("success", true);
                return "result";
            } catch (Exception e) {
                model.addAttribute("fileUploadError", e.getMessage());
                return "result";
            }
        } else {
            model.addAttribute("fileUploadError", "Kindly provide unique file name");
            return "result";
        }
    }

    @GetMapping("/download")
    public ResponseEntity download(@RequestParam Integer fileId){

        File newFile = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newFile.getFilename() + "\"")
                .body(newFile);
    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam Integer fileId, Authentication authentication, Model model){

        fileService.deleteFile(fileId);

        Integer userId = userService.getUserId(authentication.getName());
        model.addAttribute("file",this.fileService.getFileNames(userId));

        return "redirect:/home";
    }

    @GetMapping("/error")
    public String getError() {

        return "error";
    }

}


