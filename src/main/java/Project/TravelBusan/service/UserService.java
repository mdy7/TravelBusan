package Project.TravelBusan.service;
import java.util.Collections;

import Project.TravelBusan.domain.Member;
import Project.TravelBusan.request.UserDto;
import Project.TravelBusan.domain.Authority;
import Project.TravelBusan.domain.User;
import Project.TravelBusan.exception.DuplicateMemberException;
import Project.TravelBusan.exception.NotFoundMemberException;
import Project.TravelBusan.repository.UserRepository;
import Project.TravelBusan.response.MemberResponseDto;
import Project.TravelBusan.response.ResponseDto;
import Project.TravelBusan.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }
    @Transactional
    public ResponseDto<?> login(UserDto userDto)
    {
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 아이디 입니다"));

        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            throw new IllegalStateException("패스워드가 일치하지 않습니다");
        }

        return ResponseDto.success("로그인 성공",
                UserDto.builder()
                        .username(user.getUsername())
                        .build()
        );
    }
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}