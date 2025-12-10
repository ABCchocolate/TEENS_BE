package kusuri12.teens_be.domain.user.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.comment.domain.Comment;
import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.user.domain.type.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String nickname = username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int forumCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    @Column
    private String profileImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Forum> forum;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
