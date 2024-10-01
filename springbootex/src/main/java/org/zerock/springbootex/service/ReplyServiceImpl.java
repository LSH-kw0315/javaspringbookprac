package org.zerock.springbootex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.domain.Reply;
import org.zerock.springbootex.dto.BoardListAllDTO;
import org.zerock.springbootex.dto.PageRequestDTO;
import org.zerock.springbootex.dto.PageResponseDTO;
import org.zerock.springbootex.dto.ReplyDTO;
import org.zerock.springbootex.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO dto) {

        Reply reply=modelMapper.map(dto,Reply.class);

        return replyRepository.save(reply).getRno();
    }

    @Override
    public ReplyDTO read(Long rno) {

        Optional<Reply> optionalReply=replyRepository.findById(rno);

        Reply reply=optionalReply.orElseThrow();

        return modelMapper.map(reply,ReplyDTO.class);
    }

    @Override
    public void update(ReplyDTO dto) {
        Optional<Reply> replyOptional=replyRepository.findById(dto.getRno());

        Reply reply=replyOptional.orElseThrow();

        reply.changeText(dto.getReplyText());

        replyRepository.save(reply);

    }

    @Override
    public void delete(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable= PageRequest.of(
                pageRequestDTO.getPage()<=0?0: pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("rno"));
        //지금 몇 페이지인가?, 몇 개씩 보여주는가, 정렬기준

        log.info("before listOfReply "+bno);
        Page<Reply> replyPage= replyRepository.listOfReply(bno,pageable);

        List<ReplyDTO> dtoList=replyPage.getContent().stream()
                .map(i->modelMapper.map(i,ReplyDTO.class))
                .collect(Collectors.toList());
        PageResponseDTO<ReplyDTO> responseDTO=
                PageResponseDTO.<ReplyDTO>withAll()
                        .dtoList(dtoList)
                        .total((int)replyPage.getTotalElements())
                        .pageRequestDTO(pageRequestDTO)
                        .build();

        return  responseDTO;
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        return null;
    }
}
