package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Select("SELECT filename FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    List<File> isFileNameAvailable(String filename, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFileNames(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userId, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userId}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(Integer fileId);

    @Select("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFile(Integer fileId);

}
