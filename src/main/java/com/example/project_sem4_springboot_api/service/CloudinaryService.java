package com.example.project_sem4_springboot_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CloudinaryService {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "doyi7x2s3");
        valuesMap.put("api_key", "642753163166488");
        valuesMap.put("api_secret", "bjRR3CDlmpr3rsen0Tc-J5_QQCU");
        cloudinary = new Cloudinary(valuesMap);
    }

    public String uploadImage(MultipartFile image, String tag, String folderName) {
        try {
            Map<String, String> param = new HashMap<>();
            param.put("folder", folderName);
            param.put("tags", tag);
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), param);
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }
    public List<String> uploadMultiImage(List<MultipartFile> images, String tag, String folderName) {
        List<String> listUrl = new ArrayList<>();
        for (MultipartFile image : images) {
            listUrl.add(uploadImage(image, tag, folderName));
        }
        return listUrl;
    }

    public void removeFileByTag(String tag,String folderName) throws Exception {
       cloudinary.api().deleteAllResources(Map.of("folder",folderName,"tags",tag));
    }

    public List<String> getImageUrl(String tag, String folder) {
        Map<String, Object> options = ObjectUtils.asMap(
                "resource_type", "image",
                "type", "upload",
                "folder", folder
        );
        try {
            var result = cloudinary.api().resourcesByTag(tag,options);
            List<Map> lisMap = (List<Map>)result.get("resources");
            System.out.println(result);
            return lisMap.stream().map(l->l.get("secure_url").toString()).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() +"Không thể lấy URL ảnh từ Cloudinary");

        }
    }

}
