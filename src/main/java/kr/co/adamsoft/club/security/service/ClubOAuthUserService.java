package kr.co.adamsoft.club.security.service;

import kr.co.adamsoft.club.entity.ClubMember;
import kr.co.adamsoft.club.entity.ClubMemberRole;
import kr.co.adamsoft.club.repository.ClubMemberRepository;
import kr.co.adamsoft.club.security.dto.ClubAuthMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuthUserService extends DefaultOAuth2UserService {
    private final ClubMemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    private ClubMember saveSocialMember(String email){
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<ClubMember> result = repository.findByEmail(email, true);
        if(result.isPresent()){
            return result.get();
        }
        //없다면 회원 추가
        ClubMember clubMember = ClubMember.builder().email(email)
                .name(email)
                .password( passwordEncoder.encode("1111") )
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        repository.save(clubMember);

        return clubMember;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("--------------------------------------");
        log.info("userRequest:" + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName: " + clientName); log.info(userRequest.getAdditionalParameters());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("==============================");
        oAuth2User.getAttributes() .forEach( (k, v) -> {
            log.info(k + v);
        });

        String email = null;
        if(clientName.equals("Google")){
            //구글을 이용하는 경우
            email = oAuth2User.getAttribute("email");
        }
        log.info("EMAIL: " + email);
        ClubMember member = saveSocialMember(email); //조금 뒤에 사용

        return oAuth2User;
    }
}










