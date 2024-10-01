package org.zerock.springbootex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.springbootex.domain.Member;
import org.zerock.springbootex.domain.MemberRole;
import org.zerock.springbootex.dto.MemberJoinDTO;
import org.zerock.springbootex.repository.MemberRepository;


@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void join(MemberJoinDTO dto) throws MidExistException {
        String mid=dto.getMid();
        if(memberRepository.existsById(mid)){
            throw new MidExistException();
        }

        Member member= modelMapper.map(dto,Member.class);
        member.changePassword(passwordEncoder.encode(dto.getMpw()));
        member.addRole(MemberRole.USER);

        memberRepository.save(member);
    }
}
