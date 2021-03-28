package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller()
public class FileController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;

    public String objectError = null;
    public String objectSuccess = null;
    public String objectSuccessMessage = null;
    public String objectErrorMessage = null;

    public FileController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService){
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
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

       if(fileUpload.getSize() > 5000000){
           model.addAttribute("fileUploadError", "Your file exceeds the limit of 5 MB");
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
    public String deleteFile(@RequestParam Integer fileId, Authentication authentication, Model model) {

        try {
            fileService.deleteFile(fileId);
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            model.addAttribute("fileUploadError", e.getMessage());
            return "result";
        }
    }
}
