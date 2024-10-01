package org.zerock.jwtprac.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.jwtprac.domain.APIUser;
import org.zerock.jwtprac.dto.APIUserDTO;
import org.zerock.jwtprac.repository.APIUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {
//인증에 대한 인가를 하는 써비스 계층

    private final APIUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<APIUser> result=apiUserRepository.findById(username);

        APIUser apiUser=result.orElseThrow(
                ()->new UsernameNotFoundException("Cannot Find mid")
        );
        //검색한 것과 실제 입력 받은 것을 대조하는 부분은 어딘지 모르겠지만, 이걸 호출한 놈이 하지 않을까?.
        return new APIUserDTO(
                apiUser.getMid()
                ,apiUser.getMpw()
                ,List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
    }
}
