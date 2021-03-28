package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller()
public class NoteController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public String objectError = null;
    public String objectSuccess = null;
    public String objectSuccessMessage = null;
    public String objectErrorMessage = null;


    public NoteController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService, EncryptionService encryptionService){
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/noteModal")
    public String postView(Authentication authentication, FileForm fileForm, NoteForm noteForm, CredentialForm credentialForm, Model model) {

        User currentUser = userService.getUser(authentication.getName());
        int rowsAdded;

        if (noteForm.getNoteDescription().length() > 1000){
            objectError = "true";
            objectErrorMessage = "Your note exceeds the maximum number of characters";
        } else if (noteForm.getNoteId()==0){
            noteForm.setUserId(currentUser.getUserId());
            rowsAdded = this.noteService.saveNote(noteForm);
            if (rowsAdded == 0) {
                objectError = "true";
                objectErrorMessage = "You already have a note with title '" + noteForm.getNoteTitle() + "'";
            } else {
                objectError = null;
            }
        } else {
            rowsAdded = this.noteService.updateNote(noteForm);
            if (rowsAdded == 0) {
                objectError = "true";
                objectErrorMessage = "You already have a note with title '" + noteForm.getNoteTitle() + "'";
            } else {
                objectError = null;
            }
        }


        model.addAttribute("file",this.fileService.getFileNames(currentUser.getUserId()));
        model.addAttribute("note",this.noteService.getNotes(currentUser.getUserId()));
        model.addAttribute("credential", this.credentialService.getCredentials(currentUser.getUserId()));
        model.addAttribute("encryptionService",encryptionService);

        if (this.objectError == null) {
            model.addAttribute("objectSuccess", true);
            model.addAttribute("objectSuccessMessage", "You successfully added a new note");
        } else {
            model.addAttribute("objectError", true);
            model.addAttribute("objectErrorMessage", this.objectErrorMessage);
        }
        return "home";
    }

    @GetMapping(value= "home/deleteNote/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication authentication, RedirectAttributes redirectAttributes, Model model) {

        User currentUser = userService.getUser(authentication.getName());

        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("objectSuccess", true);
            redirectAttributes.addFlashAttribute("objectSuccessMessage", "You successfully deleted a note");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("objectError", true);
            redirectAttributes.addFlashAttribute("objectErrorMessage", "There was an unexpected error");
        }

        model.addAttribute("file",this.fileService.getFileNames(currentUser.getUserId()));
        model.addAttribute("note",this.noteService.getNotes(currentUser.getUserId()));
        model.addAttribute("encryptionService",encryptionService);

        return "redirect:/home";
    }


}
