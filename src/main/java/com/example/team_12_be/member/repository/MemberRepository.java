package com.example.team_12_be.member.repository;

import com.example.team_12_be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT * FROM Member m WHERE m.is_deleted = true and m.id = :id", nativeQuery = true)
    Optional<Member> findByIdIncludingDeleted(@Param("id") Long id);


}
