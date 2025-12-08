package kusuri12.teens_be.global.auth;

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

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private String password;
    private int postCount;
    private int commentCount;
    private String profileImg;

    public AuthDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.password = user.getPassword();
        this.postCount = user.getPostCount();
        this.commentCount = user.getCommentCount();
        this.profileImg = user.getProfileImg();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
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
