package com.team1.dojang_crush.domain.group.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.dto.GroupMemberDto;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final MemberService memberService;
    private final GroupRepository groupRepository;

    // 그룹생성
    public Group createGroup(String groupName, Member member) {

        String groupCode = createGroupCode(groupName);
        Group group = new Group(groupName, groupCode);

        Group savedgroup = groupRepository.save(group);

        memberService.updateGroup(savedgroup, member);
        memberService.updateLeader(member, true);

        return savedgroup;
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
            if(member.isLead()){
                memberService.updateLeader(member,false);
            }
            memberService.updateGroup(group, member);
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


    // 그룹에 속하는 member들 찾기
    public List<Member> findGroupMemberList(Long groupId) {
        Group group = findById(groupId);
        return group.getMembers();
    }


    // 그룹 이름 수정
    public Group updateName(Long groupId, String groupName, Member member) {

        if(member.getGroup().getGroupId().equals(groupId)){
            Group group = findById(groupId);
            group.updateName(groupName);
            groupRepository.save(group);

            return group;
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "수정 권한이 없습니다.");
        }
    }

    // 그룹원 조회
    public List<GroupMemberDto> getMembers(Long groupId, Member member) {

        if(member.getGroup().getGroupId().equals(groupId)){
            Group group = findById(groupId);

            List<Member> members = findGroupMemberList(group.getGroupId());

            List<GroupMemberDto> memberDto = new ArrayList<>();
            for(Member m: members){
                memberDto.add(GroupMemberDto.from(m));
            }

            return memberDto;
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "그룹원 조회 권한이 없습니다.");
        }
    }


    // 그룹 삭제
    public void delete(Long groupId, Member member) {
        if(member.isLead() && member.getGroup().getGroupId().equals(groupId)){
            Group group = findById(groupId);
            Group defaultGroup = groupRepository.findByGroupName("DEFAULT");
            List<Member> members = findGroupMemberList(groupId);

            for(Member m: members){
                //삭제되는 그룹 멤버들의 그룹을 default 변경
                memberService.updateGroup(defaultGroup, m);
            }
            memberService.updateLeader(member, false);
            groupRepository.delete(group);
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "그룹을 삭제할 권한이 없습니다.");
        }
    }
}

