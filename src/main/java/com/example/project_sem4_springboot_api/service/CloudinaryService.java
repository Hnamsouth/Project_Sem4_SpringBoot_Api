package com.example.project_sem4_springboot_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Search;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    private final ExecutorService executorService;

    public void uploadImage(MultipartFile image, String tag, String folderName) throws IOException {
        executorService.submit(() -> {
            try {
                Map<String, String> param = Map.of(
                        "folder", folderName,
                        "resource_type", "image",
                        "type", "upload",
                        "tags", tag
                );
                var rs = cloudinary.uploader().upload(image.getBytes(), param);
                System.out.println(rs);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        });
    }
    public void uploadMultiImage(List<MultipartFile> images, String tag, String folderName) throws IOException {
        for (MultipartFile image : images) {
            uploadImage(image, tag, folderName);
        }
    }

    public void removeFileByTag(String tag,String folderName) throws Exception {
        executorService.submit(()->{
            try {
                Map<String,Object> options = Map.of(
                        "folder", folderName,
                        "type", "upload",
                        "resource_type", "image"
                );
                cloudinary.api().deleteResourcesByTag(tag,options);
            } catch (Exception e) {
                log.error("Error deleting image from Cloudinary", e);
            }
        });

    }

    public List<String> getImageUrlByTag(String tag, String folder) {
        Map<String,Object> options = Map.of(
                "folder", folder,
                "type", "upload",
                "resource_type", "image",
                "tags", true
        );
        try {
            var result = cloudinary.api().resourcesByTag(tag, options);
            List<Map> lisMap = (List<Map>)result.get("resources");
            return lisMap.stream().map(l->l.get("secure_url").toString()).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() +"Không thể lấy URL ảnh từ Cloudinary");
        }
    }

    public Map<String,List<String>> getImageUrlByTags(List<String> tags, String folder) {

        try {
            Map<String,Object> options = Map.of(
                    "folder", folder,
                    "type", "upload",
                    "resource_type", "image",
                    "tags", true
            );
            String tagss = String.join(",", tags);
            var result = cloudinary.api().resourcesByTag(tagss, options);
            List<Map> lisMap = (List<Map>)result.get("resources");
            System.out.println(result);
            lisMap.forEach(System.out::println);
            return tags.stream().collect(Collectors.toMap(Function.identity(),
                    t-> lisMap.stream().filter(l->l.get("tags").toString().contains(t))
                    .map(l->l.get("secure_url").toString()).toList()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() +"Không thể lấy URL ảnh từ Cloudinary");
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            System.out.println("Shutting down executor service");
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                System.out.println("Executor service did not terminate in the specified time. Shutting down now.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }


}
