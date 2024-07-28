package com.team1.dojang_crush.domain.group.controller;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.dto.AddGroupMemberDto;
import com.team1.dojang_crush.domain.group.dto.GroupCreateDto;
import com.team1.dojang_crush.domain.group.dto.GroupMemberDto;
import com.team1.dojang_crush.domain.group.dto.GroupResponseDto;
import com.team1.dojang_crush.domain.group.service.GroupService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.domain.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import com.team1.dojang_crush.global.oauth.AuthUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final MemberService memberService;


    //그룹 생성
//    @PostMapping
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public GroupResponseDto createGroup(@RequestPart (value = "images", required = false) MultipartFile image,
//                                        @RequestBody GroupCreateDto dto){
//
//        Member member = findMember(1l);
//        Group group = groupService.createGroup(dto.getGroupName(), image, member);
//
//        List<Member> members= new ArrayList<>();
//
//
//        return GroupResponseDto.from(group, members, null);
//    }

    //그룹 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public GroupResponseDto createGroup(@RequestBody GroupCreateDto dto,
                                        @AuthUser Member member){

        Group group = groupService.createGroup(dto.getGroupName(), member);

        List<Member> members= new ArrayList<>();
        members.add(member);

        return GroupResponseDto.from(group, members, null);
    }

    //그룹원 추가
    @PatchMapping
    @ResponseStatus(value = HttpStatus.OK)
    public GroupResponseDto addGroupMember(@RequestBody AddGroupMemberDto dto,
                                           @AuthUser Member member){

        String groupCode = dto.getGroupCode();

        Group group = groupService.addMember(groupCode,member);
        List<Member> members= memberService.findGroupMemberList(group.getGroupId());

        return GroupResponseDto.from(group, members, null);
    }

    //그룹원조회
    @GetMapping("/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GroupResponseDto getGroupMember(@PathVariable(name = "groupId")Long groupId){

        Group group = groupService.findById(groupId);
        List<GroupMemberDto> memberDto = groupService.getMembers(groupId);

        return GroupResponseDto.from(group, null, memberDto);
    }

    //그룹 이미지 수정
//    @PatchMapping("/{groupId}/image")
//    @ResponseStatus(value = HttpStatus.OK)
//    public GroupResponseDto UpdateGroupImage(@PathVariable(name = "groupId")Long groupId,
//                                            @RequestParam(value = "image", required = false) MultipartFile image){
//
//        Group group = groupService.updateImage(groupId, image);
//        List<Member> members= memberService.findGroupMemberList(group.getGroupId());
//
//        return GroupResponseDto.from(group, members, null);
//    }

    //그룹 이름 수정
    @PatchMapping("/{groupId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public GroupResponseDto UpdateGroupName(@PathVariable(name = "groupId")Long groupId,
                                            @RequestBody GroupCreateDto dto){

        Group group = groupService.updateName(groupId, dto.getGroupName());
        List<Member> members= memberService.findGroupMemberList(group.getGroupId());

        return GroupResponseDto.from(group, members, null);
    }

    //그룹 삭제
    @DeleteMapping("/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteGroup(@PathVariable(name = "groupId")Long groupId,
                              @AuthUser Member member){

        groupService.delete(groupId, member);
        return "그룹을 삭제했습니다.";
    }
}

