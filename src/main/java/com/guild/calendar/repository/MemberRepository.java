package com.guild.calendar.repository;

import com.guild.calendar.entity.Member;
import com.guild.calendar.exception.EmailDuplicateException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    /**
     * 유저 저장
     * @param member
     */
    public void save(Member member) {
        em.persist(member);
    }


    /**
     * 이메일 조회
     * @param email
     * @return
     */
    public Member findEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email",Member.class)
                .setParameter("email",email)
                .getSingleResult();
    }

}
