package com.team1.dojang_crush.domain.group.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.dto.GroupMemberDto;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    //private final S3Service s3Service;
    private final MemberService memberService;
    private final GroupRepository groupRepository;

    //그룹 생성
//    public Group createGroup(String groupName, MultipartFile image, Member member) {
//
//        try{
//            String groupCode = createGroupCode(groupName);
//
//            String Url = s3Service.uploadFiles(image);
//            Group group = new Group(groupName, groupCode, Url);
//
//            return groupRepository.save(group);
//        }
//        catch(Exception e){
//            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
//        }
//    }

    // 그룹생성
    public Group createGroup(String groupName, Member member) {

        String groupCode = createGroupCode(groupName);
        Group group = new Group(groupName, groupCode);
        member.setGroup(group);

        return groupRepository.save(group);
    }


    //그룹 해시코드 생성
    public String createGroupCode(String groupName){
        UUID uuid = UUID.nameUUIDFromBytes(groupName.getBytes());
        return uuid.toString();
    }


    // 그룹원추가
    public Group addMember(String groupCode, Member member) {

        Group group = groupRepository.findByGroupCode(groupCode);

        if(group != null){
            member.setGroup(group);
            return group;
        }
        else{
            throw new AppException(ErrorCode.NOT_FOUND_GROUP, "그룹코드를 가진 그룹이 존재하지 않습니다.");
        }
    }

    //id로 그룹찾기
    public Group findById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id의 그룹을 찾을 수 없습니다."));
    }


    // 그룹 이미지 수정
//    public Group updateImage(Long groupId, MultipartFile image) {
//
//        try{
//            Group group = findById(groupId);
//            String url = s3Service.uploadFiles(image);
//
//            group.updateImg(url);
//            return group;
//        }
//        catch(Exception e){
//            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
//        }
//    }


    // 그룹 이름 수정
    public Group updateName(Long groupId, String groupName) {

        Group group = findById(groupId);
        group.updateName(groupName);

        return group;
    }

    // 그룹원 조회
    public List<GroupMemberDto> getMembers(Long groupId) {
        Group group = findById(groupId);

        List<Member> members = memberService.findGroupMemberList(group.getGroupId());

        List<GroupMemberDto> memberDto = new ArrayList<>();
        for(Member member: members){
            memberDto.add(GroupMemberDto.from(member));
        }

        return memberDto;
    }


    // 그룹 삭제
    public void delete(Long groupId, Member member) {
        if(member.isLead()){
            Group group = findById(groupId);
            List<Member> members = memberService.findGroupMemberList(groupId);

            for(Member m: members){
                // member의 그룹 default로
            }
            groupRepository.delete(group);
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "그룹을 삭제할 권한이 없습니다.");
        }
    }
}

