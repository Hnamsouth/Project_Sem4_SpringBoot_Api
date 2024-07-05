package com.example.project_sem4_springboot_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
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
            param.put("tag", tag);
            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), param);
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }


//    public Map upload(MultipartFile multipartFile) throws IOException {
//        File file = convert(multipartFile);
//        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
//        if (!Files.deleteIfExists(file.toPath())) {
//            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
//        }
//        return result;
//    }
//
//    public Map delete(String id) throws IOException {
//        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
//    }
//
//    private File convert(MultipartFile multipartFile) throws IOException {
//        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        FileOutputStream fo = new FileOutputStream(file);
//        fo.write(multipartFile.getBytes());
//        fo.close();
//        return file;
//    }

}
