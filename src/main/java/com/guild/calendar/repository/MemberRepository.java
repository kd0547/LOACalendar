package com.guild.calendar.repository;

import com.guild.calendar.entity.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    /**
     * 유저 저장
     * @param users
     */
    public void save(Users users) {
        em.persist(users);
    }


    /**
     * 이메일 조회
     * @param email
     * @return
     */
    public Users findEmail(String email) {
        return em.createQuery("select m from Users m where m.email = :email", Users.class)
                .setParameter("email",email)
                .getSingleResult();
    }

}
