package io.ssafy.openticon.entity;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.dto.ExamineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmoticonPackEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩 설정
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "is_ai_generated", nullable = false)
    private boolean isAiGenerated = false;

    @Column(name = "price", nullable = false)
    private int price = 0;

    @Column(name = "view", nullable = false)
    private Long view = 0L;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "is_blacklist", nullable = false)
    private boolean isBlacklist = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "thumbnail_img")
    private String thumbnailImg;

    @Column(name = "list_img")
    private String listImg;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "examine", nullable = false)
    private ExamineType examine = ExamineType.IN_PROGRESS;

    @Column(name = "share_link", nullable = false)
    private String shareLink="public";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public EmoticonPackEntity(EmoticonPack emoticonPack, String thumbnailImg, String listImg){
        this.title=emoticonPack.getPackTitle();
        this.isAiGenerated=emoticonPack.isAiGenerated();
        this.isPublic=emoticonPack.isPublic();
        this.category=emoticonPack.getCategory();
        this.description=emoticonPack.getDescription();
        this.price=emoticonPack.getPrice();
        this.thumbnailImg=thumbnailImg;
        this.listImg=listImg;
    }

}
