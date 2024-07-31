package com.team1.dojang_crush.domain.postImgUrl.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import com.team1.dojang_crush.domain.postImgUrl.repository.PostImgUrlRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
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
public class PostImgUrlService {
    private final PostImgUrlRepository postImgUrlRepository;
    private final S3Service s3Service;



    // 파일을 S3에 업로드하고 업로드된 파일의 URL 리스트를 DB에 저장
    public PostImgUrl createPostImgUrl(List<MultipartFile> files, Post post) throws IOException {

        List<String> uploadedUrls = new ArrayList<>();

        for(MultipartFile file : files) {
            String uploadedUrl = s3Service.uploadFiles(file);
            uploadedUrls.add(uploadedUrl);
        }

        // url 저장
        PostImgUrl postImgUrl = new PostImgUrl(uploadedUrls,post);
        postImgUrlRepository.save(postImgUrl);
        return postImgUrl;
    }


    // post로 이미지 url 찾기
    @Transactional(readOnly = true)
    public PostImgUrl findImgUrlByPost(Post post) {
        PostImgUrl imgUrl = postImgUrlRepository.findByPost(post);

        // imgUrl이 null이면 그냥 null을 반환
        if (imgUrl == null) {
            return null;
        }

        // imgUrl이 존재하면 반환
        return imgUrl;
    }


    // postImgUrl이 있는지
    public boolean isExistsByPost(Post post) {
        return postImgUrlRepository.existsByPost(post);
    }


    // 이미지 삭제하기
    public void deletePostImgUrl(Post post) {

        if(isExistsByPost(post)) {

            PostImgUrl postImgUrl = findImgUrlByPost(post);
            // S3에서 이미지 삭제
            s3Service.deleteFile(postImgUrl.getPostImgUrl());
            //삭제
            postImgUrlRepository.delete(postImgUrl);
        }
    }

    public PostImgUrl updatePostImgUrl(List<MultipartFile> images, Post post) throws IOException {


        // 원래 이미지가 있었던 경우
        if(isExistsByPost(post)){

            //새로운 이미지 업로드
            List<String> uploadedUrls = new ArrayList<>();
            for(MultipartFile file : images) {
                String uploadedUrl = s3Service.uploadFiles(file);
                uploadedUrls.add(uploadedUrl);
            }


            PostImgUrl postImgUrl = findImgUrlByPost(post);
            //s3에서 기존의 파일 삭제
            s3Service.deleteFile(postImgUrl.getPostImgUrl());

            //새로운 url로 업데이트
            postImgUrl.update(uploadedUrls);
            postImgUrlRepository.save(postImgUrl);
            return postImgUrl;
        }

        // 원래 이미지가 없었는데 추가한 경우
        else{
            PostImgUrl postImgUrl = createPostImgUrl(images, post);
            return postImgUrl;
        }
    }
}