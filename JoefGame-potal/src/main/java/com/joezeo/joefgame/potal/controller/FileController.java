package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.provider.UCloudProvider;
import com.joezeo.joefgame.common.dto.ImgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/file/")
public class FileController {

    @Autowired
    UCloudProvider uCloudProvider;

    @PostMapping("imgUpload")
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
