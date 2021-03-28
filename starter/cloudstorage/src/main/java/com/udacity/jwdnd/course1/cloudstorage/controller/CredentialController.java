package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller()
public class CredentialController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;
    private final HashService hashService;

    public String objectError = null;
    public String objectSuccess = null;
    public String objectSuccessMessage = null;
    public String objectErrorMessage = null;

    public CredentialController(FileService fileService, NoteService noteService, CredentialService credentialService,
                                UserService userService, EncryptionService encryptionService, HashService hashService){
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService=encryptionService;
        this.hashService=hashService;
    }

    @PostMapping("/credentialModal")
    public String postView(Authentication authentication, @ModelAttribute Credential credential,
                           @RequestParam("credentialId")  int credentialId, @RequestParam("url") String url,
                           @RequestParam("username") String username, @RequestParam("password") String password,
                           NoteForm noteForm, CredentialForm credentialForm, Model model) {

        User currentUser = userService.getUser(authentication.getName());

        int rowsAdded;

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if (credentialId == 0) {
            rowsAdded = credentialService.saveCredential(url, username, encodedKey, encryptedPassword, currentUser.getUserId());
        } else {
            rowsAdded = credentialService.updateCredential(credentialId, url, username, encodedKey, encryptedPassword);
        }

        if (rowsAdded == 0) {
            objectError = "true";
            objectErrorMessage = "You already have a credential with username " + username + " for '" + url + "'";
        }

        if (this.objectError == null) {
            model.addAttribute("objectSuccess", true);
            model.addAttribute("objectSuccessMessage", "You successfully added a new credential");
        } else {
            model.addAttribute("objectError", true);
            model.addAttribute("objectErrorMessage", this.objectErrorMessage);
        }

        model.addAttribute("file",this.fileService.getFileNames(currentUser.getUserId()));
        model.addAttribute("note",this.noteService.getNotes(currentUser.getUserId()));
        model.addAttribute("credential", this.credentialService.getCredentials(currentUser.getUserId()));
        model.addAttribute("encryptionService",encryptionService);

        return "home";
    }

    @GetMapping(value= "home/deleteCredential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, NoteForm noteform, CredentialForm credentialForm, RedirectAttributes redirectAttributes, Model model) {

        Integer userId = userService.getUserId(authentication.getName());

        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("objectSuccess", true);
            redirectAttributes.addFlashAttribute("objectSuccessMessage", "You successfully deleted a credential");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("objectError", true);
            redirectAttributes.addFlashAttribute("objectErrorMessage", "There was an unexpected error");
        }

        model.addAttribute("file",this.fileService.getFileNames(userId));
        model.addAttribute("note",this.noteService.getNotes(userId));
        model.addAttribute("credential", this.credentialService.getCredentials(userId));
        return "redirect:/home";
    }


}
