package org.zerock.springbootex.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.dto.*;
import org.zerock.springbootex.repository.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result=boardRepository.findByIdWithImage(bno);

        Board board=result.orElseThrow();

        return entityToDTO(board);
    }

    @Override
    public void modify(BoardDTO dto) {
        Optional<Board> result=boardRepository.findByIdWithImage(dto.getBno());

        Board board=result.orElseThrow();

        board.change(dto.getTitle(),dto.getContent());
        board.clearImage();

        if(dto.getFileNames()!=null){
            for(String fileName:dto.getFileNames()){
                String[] arr=fileName.split("_");
                board.addImage(arr[0],arr[1]);
            }
        }

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO dto) {
        String[] types=dto.getTypes();
        String keyword=dto.getKeyword();
        Pageable pageable=dto.getPageable("bno");


        Page<Board> result= boardRepository.searchAll(types,keyword,pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(i->modelMapper.map(i,BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(dto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public Long register(BoardDTO dto) {
        //Board board=modelMapper.map(dto, Board.class);
        Board board=dtoToEntity(dto);
        return boardRepository.save(board).getBno();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO dto) {
        String[] types=dto.getTypes();
        String keyword=dto.getKeyword();
        Pageable pageable=dto.getPageable("bno");

        Page<BoardListAllDTO> page=boardRepository.searchWithAll(types,keyword,pageable);



        return PageResponseDTO.<BoardListAllDTO>withAll()
                .dtoList(page.getContent())
                .total((int)page.getTotalElements())
                .pageRequestDTO(dto)
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCont(PageRequestDTO dto) {
        String[] types=dto.getTypes();
        String keyword=dto.getKeyword();
        Pageable pageable=dto.getPageable("bno");
        //페이징 처리에 필요한 pageable을 받아옴.

        Page<BoardListReplyCountDTO> result=
                boardRepository.searchWithReplyCount(
                  types,
                  keyword,
                  pageable
                );

        //리포지토리를 통해 replyCount까지 포함된 게시글리스트를 받아옴.
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(dto)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
