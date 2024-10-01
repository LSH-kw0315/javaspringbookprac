package org.zerock.springbootex.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.springbootex.domain.Member;
import org.zerock.springbootex.repository.MemberRepository;
import org.zerock.springbootex.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername:"+username);

        Optional<Member> result=memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("username not found...");
        }

        Member member=result.get();

        MemberSecurityDTO memberSecurityDTO=
                new MemberSecurityDTO(
                  member.getMid(),
                  member.getMpw(),
                  member.getEmail(),
                  member.isDel(),
                  false,
                        member.getRoleSet().stream()
                                .map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                                .collect(Collectors.toList())
                );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);
        return memberSecurityDTO;
    }
}
