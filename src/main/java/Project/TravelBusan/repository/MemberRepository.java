package Project.TravelBusan.repository;

import Project.TravelBusan.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    default Member findByIdOrElseThrow(Long memberId) {
        return findById(memberId).orElseThrow(() ->
                new NullPointerException("존재하지 않는 아이디 입니다"));
    }

    Optional<Member> findByUsername(String username);

}