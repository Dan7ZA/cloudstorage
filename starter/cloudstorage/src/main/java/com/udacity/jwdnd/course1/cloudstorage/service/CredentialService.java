package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;
    private UserService userService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public int saveCredential(String url, String username, String encodedKey, String encryptedPassword, int userId) {

        Credential savedCredential = credentialMapper.getCredentialByUrl(url);

        if(savedCredential != null && savedCredential.getUsername().equals(username)) {
            return 0;
        } else {
            Credential credential = new Credential(url, username, encodedKey, encryptedPassword, userId);
            return credentialMapper.insertCredential(credential);
        }

    }

    public int updateCredential(int credentialId, String url, String username, String encodedKey, String encryptedPassword){

        Credential credential = new Credential(credentialId, url, username, encodedKey, encryptedPassword);
        return credentialMapper.updateCredential(credential);

        /*
        if(credentialMapper.getCredentialByUrl(url)==null) {
            Credential credential = new Credential(credentialId, url, username, encodedKey, encryptedPassword);
            return credentialMapper.updateCredential(credential);
        }
        return 0;
        */

    }

    public List<Credential> getCredentials(Integer userId){

        //String salt = userService.getSalt(userId);

        List<Credential> allCredentials = credentialMapper.getAllCredentials(userId);

//        for(Credential credential : allCredentials){
//            credential.setPassword(encryptionService.decryptValue(credential.getPassword(),salt));
//        }

        return allCredentials;
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public boolean checkCredential(Credential testCredential, Credential savedCredential){

        String testPassword = testCredential.getPassword();
        String savedKey = savedCredential.getKey();
        String savedPasswordEncrypted = savedCredential.getPassword();
        String savedPasswordDecrypted = encryptionService.decryptValue(savedPasswordEncrypted,savedKey);

        if(testPassword == savedPasswordDecrypted){
            return true;
        } else {
            return false;
        }
    }

    public Credential getCredentialById(Integer credentialId){
        return credentialMapper.getCredentialById(credentialId);
    }

}
