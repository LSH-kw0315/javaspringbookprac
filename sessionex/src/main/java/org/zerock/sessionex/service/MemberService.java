package org.zerock.sessionex.service;

import org.modelmapper.ModelMapper;
import org.zerock.sessionex.dao.MemberDAO;
import org.zerock.sessionex.domain.MemberVO;
import org.zerock.sessionex.dto.MemberDTO;
import org.zerock.sessionex.util.MapperUtil;

public enum MemberService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    MemberService(){
        memberDAO=new MemberDAO();
        modelMapper= MapperUtil.INSTANCE.get();
    }

    public MemberDTO login(String mid,String mpw) throws Exception{
        MemberVO vo=memberDAO.getWithPassword(mid,mpw);
        return modelMapper.map(vo,MemberDTO.class);
    }

    public void updateUUID(String mid,String uuid) throws Exception{
        memberDAO.updateUUID(mid,uuid);
    }

    public MemberDTO getByUUID(String uuid) throws Exception{
        MemberVO vo=memberDAO.selectUUID(uuid);
        return modelMapper.map(vo,MemberDTO.class);
    }
}
