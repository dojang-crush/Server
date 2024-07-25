package com.team1.dojang_crush.domain.postImgUrl.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import com.team1.dojang_crush.domain.postImgUrl.repository.PostImgUrlRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;

    // 파일 업로드해서 imgUrl 반환
    public String uploadFiles(MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        File convertedFile = convertMultiPartFileToFile(file)
                .orElseThrow(()->new RuntimeException("MultipartFile -> File 전환 실패"));

        s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
        convertedFile.delete(); // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        return fileUrl;
    }


    // MultipartFile -> File 전환
    private Optional<File> convertMultiPartFileToFile(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // s3에서 이미지 파일 삭제
    public void deleteFile(List<String> postImgUrl) {
        for (String fileUrl : postImgUrl) {

            try {
                String bucketUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
                String fileName = fileUrl.substring(bucketUrl.length());


                s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            } catch (Exception e){
                System.err.println("Failed to delete file: " + fileUrl);
            }
        }
    }


}
