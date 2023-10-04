package Project.TravelBusan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "SIGHTS_REVIEW")
public class SightsReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String nickname;
    private String content;

    @CreationTimestamp
    @Column(name = "cre_date")
    private Timestamp creDate;

    @Column(name = "del_date")
    private Timestamp delDate;

    private int rate;
    private String img;
    private String state;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sights_id")
    private Sights sights;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



}