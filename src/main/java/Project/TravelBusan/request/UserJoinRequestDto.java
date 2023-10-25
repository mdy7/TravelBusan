package Project.TravelBusan.request;


import Project.TravelBusan.domain.User;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinRequestDto {

    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String passwordCheck;
    private String email;
    private Set<AuthorityDto> authorityDtoSet;
    public static UserJoinRequestDto from(User user) {
        if(user == null) return null;

        return UserJoinRequestDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}