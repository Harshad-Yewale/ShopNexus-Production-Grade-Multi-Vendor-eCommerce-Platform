package com.harshadcodes.EcommerceWebsite.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FilesService{

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String originalFileName= image.getOriginalFilename();
        String randomUid= UUID.randomUUID().toString();
        String fileName=randomUid.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator+fileName;

        File folder=new File(path);

        if(!folder.exists()){
            folder.mkdir();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }

}
