package com.example.cheerdo.repository;

import com.example.cheerdo.entity.FriendRelation;
import com.example.cheerdo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<FriendRelation, Long> {
    Optional<List<FriendRelation>> findAllByMember_Id(Long memberId);
    Optional<List<FriendRelation>> findAllByMemberAndIsFriend(Optional<Member> member, boolean isFriend);
    Optional<FriendRelation> findFriendRelationByMemberAndFriendId(Optional<Member> member, String friendId);
    Optional<List<FriendRelation>> findAllByFriendIdAndIsFriend(String friendId, boolean isFriend);

}
