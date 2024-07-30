package com.team1.dojang_crush.domain.group.repository;

import com.team1.dojang_crush.domain.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupCode(String groupCode);

    Group findByGroupName(String name);
}
