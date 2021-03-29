package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@org.springframework.web.bind.annotation.ControllerAdvice

public class ControllerAdvice {

    private final static String errorPage = "maxFileSizeError";

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request,
                                               HttpServletResponse response, RedirectAttributes redirectAttributes) {


        return new ModelAndView(errorPage);

    }

}
