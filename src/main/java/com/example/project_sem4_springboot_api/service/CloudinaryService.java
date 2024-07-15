package com.example.project_sem4_springboot_api.service;

import com.cloudinary.Cloudinary;
import com.example.project_sem4_springboot_api.entities.FileStorage;
import com.example.project_sem4_springboot_api.repositories.FileStorageRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CloudinaryService {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;
    private final FileStorageRepository fileStorageRepository;
    private final ExecutorService executorService;
    private final TransactionTemplate transactionTemplate;

    public void uploadImage(MultipartFile image, String tag, String folderName) {
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
                fileStorageRepository.save(FileStorage.builder()
                        .fileUrl(rs.get("secure_url").toString())
                        .publicId(rs.get("public_id").toString())
                        .folderName(folderName)
                        .tags(tag)
                        .build());
                // rs: {asset_folder=homeWork, signature=209097821cbc65044ba91c5948401c6886120bb7, format=png, resource_type=image, secure_url=https://res.cloudinary.com/doyi7x2s3/image/upload/v1720768012/homeWork/y3dvjualcnfudukbx15s.png, created_at=2024-07-12T07:06:52Z, asset_id=152b81f23ace659fd808ecfc533deedd, version_id=993db8789a2be9781d621739b5a65899, type=upload, display_name=y3dvjualcnfudukbx15s, version=1720768012, url=http://res.cloudinary.com/doyi7x2s3/image/upload/v1720768012/homeWork/y3dvjualcnfudukbx15s.png, public_id=homeWork/y3dvjualcnfudukbx15s, tags=[homework5], original_filename=file, api_key=642753163166488, bytes=133136, width=897, etag=d5d2eede17dd5325e5f1a3bca4b482b7, placeholder=false, height=755}
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        });
    }
    public void uploadMultiImage(List<MultipartFile> images, String tag, String folderName) throws IOException, ExecutionException, InterruptedException {
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
                // khi xoa data trong db sẽ yêu cầu giao dịch riêng biệt nên cần phải tạo giao dịch khi xóa
                // có thể dùng @Transaction hoặc transactionTemplate
                transactionTemplate.execute(status ->{
                    var check = fileStorageRepository.deleteAllByTags(tag);
                      System.out.println("delete img size:"+check.size());
                    return null;
                });
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

    public Map<String,List<String>> getImageGroupByTags(List<String> tags){
        var listUrls = fileStorageRepository.findAllByTagsIn(tags);
        return listUrls.stream().collect(Collectors.groupingBy(FileStorage::getTags,Collectors.mapping(FileStorage::getFileUrl,Collectors.toList())));
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
