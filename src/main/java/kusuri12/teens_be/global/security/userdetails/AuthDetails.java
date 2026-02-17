package kusuri12.teens_be.global.security.userdetails;

import kusuri12.teens_be.domain.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String email;
    private final String role;
    private final String password;
    private final int forumCount;
    private final int commentCount;
    private final String profileImg;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthDetails(
            Long id,
            String username,
            Collection<? extends GrantedAuthority> authorities) {

        this.id = id;
        this.username = username;
        this.authorities = authorities;

        this.nickname = null;
        this.email = null;
        this.role = null;
        this.password = null;
        this.forumCount = 0;
        this.commentCount = 0;
        this.profileImg = null;
    }

    public AuthDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.password = user.getPassword();
        this.forumCount = user.getForumCount();
        this.commentCount = user.getCommentCount();
        this.profileImg = user.getProfileImg();

        List<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_" + role));
        this.authorities = authList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
