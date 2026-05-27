package com.lab.controller;

import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("请选择文件");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        File dir = new File(uploadPath).getAbsoluteFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, fileName);
        file.transferTo(dest);
        return R.ok().put("url", "/api/uploads/" + fileName).put("fileName", originalFilename);
    }
}
