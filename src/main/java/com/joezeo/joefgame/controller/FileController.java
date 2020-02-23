package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.ImgDTO;
import com.joezeo.joefgame.exception.CustomizeErrorCode;
import com.joezeo.joefgame.exception.CustomizeException;
import com.joezeo.joefgame.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/file/")
public class FileController {

    @Autowired
    UCloudProvider uCloudProvider;

    @RequestMapping("imgUpload")
    @ResponseBody
    public ImgDTO imgUpload(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
        MultipartFile file = mulReq.getFile("editormd-image-file");

        try {
            String url = uCloudProvider.uploadImg(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            ImgDTO imgDTO = new ImgDTO();
            imgDTO.setMessage("上传图片OK");
            imgDTO.setSuccess(1);
            imgDTO.setUrl(url);
            return imgDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }
}
