package org.zerock.springbootex.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.springbootex.domain.Member;
import org.zerock.springbootex.domain.MemberRole;
import org.zerock.springbootex.repository.MemberRepository;
import org.zerock.springbootex.security.dto.MemberSecurityDTO;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest...");
        log.info(userRequest);

        ClientRegistration clientRegistration=userRequest.getClientRegistration();
        String clientName=clientRegistration.getClientName();

        log.info("NAME:"+clientName);

        OAuth2User oAuth2User=super.loadUser(userRequest);
        Map<String,Object> paramMap=oAuth2User.getAttributes();

        String email=null;

        switch (clientName){
            case "kakao":
                email=getKaKaoEmail(paramMap);
                break;
        }
        paramMap.forEach((k,v)->{
            log.info("----------");
            log.info(k+":"+v);
        });
        return generateDTO(email,paramMap);
    }

    private String getKaKaoEmail(Map<String,Object> paramMap){
        log.info("----------KAKAO----------");

        Object value=paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap=(LinkedHashMap) value;

        String email=(String) accountMap.get("email");

        log.info("email..."+email);

        return email;
    }

    private MemberSecurityDTO generateDTO(String email,Map<String,Object> params){

        Optional<Member> result=memberRepository.findByEmail(email);

        if(result.isEmpty()){
            Member member=Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();

            member.addRole(MemberRole.USER);

            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO=new MemberSecurityDTO(
                    member.getMid(),member.getMpw(),member.getEmail(),member.isDel(),member.isSocial(),
                    member.getRoleSet().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                            .collect(Collectors.toList())
            );

            return memberSecurityDTO;
        }else{
            Member member=result.get();
            MemberSecurityDTO memberSecurityDTO=
                    new MemberSecurityDTO(member.getMid(),member.getMpw(),member.getEmail(),member.isDel(),member.isSocial(),
                            member.getRoleSet().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                                    .collect(Collectors.toList()));

            return memberSecurityDTO;

        }
    }
}
