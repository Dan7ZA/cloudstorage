package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public Boolean isFileNameAvailable(String filename, Integer userId){
        return fileMapper.isFileNameAvailable(filename,userId)==null;
    }

    public Integer insertFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File file = new File();
        file.setFilename(fileUpload.getOriginalFilename());
        file.setContenttype(fileUpload.getContentType());
        file.setFilesize(String.valueOf(fileUpload.getSize()));
        file.setUserId(userId);
        file.setFiledata(fileUpload.getBytes());

        return fileMapper.insertFile(file);
    }


    public List<File> getFileNames(Integer userId){
        return fileMapper.getFileNames(userId);
    }

    public File getFile(Integer fileId){
        File newFile = fileMapper.getFile(fileId);
        return newFile;
    }

    public void deleteFile(Integer fileId){
        fileMapper.deleteFile(fileId);
    }
}


