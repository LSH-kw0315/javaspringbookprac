package org.zerock.springbootex.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.domain.BoardImage;
import org.zerock.springbootex.domain.QBoard;
import org.zerock.springbootex.domain.QReply;
import org.zerock.springbootex.dto.BoardDTO;
import org.zerock.springbootex.dto.BoardImageDTO;
import org.zerock.springbootex.dto.BoardListAllDTO;
import org.zerock.springbootex.dto.BoardListReplyCountDTO;
import org.zerock.springbootex.repository.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {


    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board =QBoard.board;//Q도메인

        JPQLQuery<Board> query=from(board);//select from

        BooleanBuilder booleanBuilder=new BooleanBuilder();//(

        booleanBuilder.or(board.title.contains("11")); //title like
        booleanBuilder.or(board.content.contains("11")); //content like

        query.where(booleanBuilder); // )

        query.where(board.bno.gt(0L)); //where로 쿼리를 이으면 and로 잇는다. bno>0


        this.getQuerydsl().applyPagination(pageable,query);//pageable 객체의 처리

        List<Board> list=query.fetch(); //작성된 쿼리 실행

        long cnt= query.fetchCount();//쿼리에 대한 count

        return null;

    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board=QBoard.board;
        JPQLQuery<Board> query=from(board);

        if((types!=null && types.length>0) && keyword!=null){
            BooleanBuilder booleanBuilder=new BooleanBuilder();

            for(String type:types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L));


        this.getQuerydsl().applyPagination(pageable,query);

        List<Board> list=query.fetch();
        long cnt= query.fetchCount();

        return new PageImpl<>(list,pageable,cnt);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board=QBoard.board;
        QReply reply=QReply.reply;

        JPQLQuery<Board> query=from(board);
        //select * from board;
        query.leftJoin(reply).on(reply.board.eq(board));
        //select * from board left outer join reply on reply.bno = board.bno;
        query.groupBy(board);
        //select * from board left outer join reply on reply.bno = board.bno group by board.bno;
        //즉, 모든 게시물에 대해 댓글을 보아야 하므로 left outer join이 필요하고, 게시물 별로 봐야 하므로 group by로 묶어야한다.

        if((types!=null && types.length>0) && keyword!=null){
            BooleanBuilder booleanBuilder=new BooleanBuilder();

            for(String type:types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }
        //...title like ${keyword}
        query.where(board.bno.gt(0L));
        // board.bno>0
        JPQLQuery<BoardListReplyCountDTO> dtojpqlQuery= query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ));
        //fetch 결과를 원하는 DTO로 변환하도록 함. 기존의 query대로라면 Board를 반환하지만, 대신 BoardListReplyCountDTO로 프로젝션했다.
        this.getQuerydsl().applyPagination(pageable,dtojpqlQuery);

        List<BoardListReplyCountDTO> result=dtojpqlQuery.fetch();

        long cnt=dtojpqlQuery.fetchCount();

        return new PageImpl<>(result,pageable,cnt);

    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board=QBoard.board;
        QReply reply=QReply.reply;

        JPQLQuery<Board> boardJPQLQuery=from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        BooleanBuilder booleanBuilder=new BooleanBuilder();
        if(types!=null&&types.length>0&&keyword!=null){
            for(String type :types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;

                }
            }
            boardJPQLQuery.where(booleanBuilder);
        }

        boardJPQLQuery.groupBy(board);
        getQuerydsl().applyPagination(pageable,boardJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery=boardJPQLQuery.select(board,reply.countDistinct());

        List<Tuple> tupleList=tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList=tupleList
                .stream()
                .map(tuple->
                {
                    Board b=(Board)tuple.get(board);
                    long replyCount=tuple.get(1,Long.class);

                    List<BoardImageDTO> imageDTOList=
                            b.getImageSet().stream().sorted()
                                    .map(image->
                                       BoardImageDTO.builder()
                                               .uuid(image.getUuid())
                                               .ord(image.getOrd())
                                               .fileName(image.getFileName())
                                               .build()
                                    ).collect(Collectors.toList());

                    BoardListAllDTO dto=BoardListAllDTO.builder()
                            .bno(b.getBno())
                            .title(b.getTitle())
                            .writer(b.getWriter())
                            .regDate(b.getRegDate())
                            .replyCount(replyCount)
                            .build();

                    dto.setBoardImages(imageDTOList);

                    return dto;
                }).collect(Collectors.toList());
        long totalCount=boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,totalCount);
    }
}
