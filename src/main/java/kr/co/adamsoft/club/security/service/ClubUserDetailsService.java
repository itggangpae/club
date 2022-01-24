package kr.co.adamsoft.club.security.service;

import kr.co.adamsoft.club.entity.ClubMember;
import kr.co.adamsoft.club.repository.ClubMemberRepository;
import kr.co.adamsoft.club.security.dto.ClubAuthMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService  implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        ClubMember clubMember = clubMemberRepository.findByEmail(username, false).get();
        log.info("-----------------------------");
        log.info(clubMember);

        ClubAuthMember clubAuthMember = new ClubAuthMember(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
