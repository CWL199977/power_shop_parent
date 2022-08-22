package com.powershop.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.powershop.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    private List<String> contentTypeList = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            //校验文件的类型
            String contentType = file.getContentType();
            if (!contentTypeList.contains(contentType)) {
                return Result.error("文件类型不合法:" + originalFilename);
            }
            //校验文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                return Result.error("文件内容不合法:" + originalFilename);
            }
            //上传图片
            String extName = StringUtils.substringAfter(originalFilename, ".");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extName, null);
            //返回图片地址
            return Result.ok("http://images.powershop.com/" + storePath.getFullPath());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }
}
