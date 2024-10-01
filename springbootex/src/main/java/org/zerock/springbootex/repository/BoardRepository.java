package org.zerock.springbootex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.repository.search.BoardSearch;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {

    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
    //제목에 keyword가 포함된 것만을 내림차 순으로 정렬해서 보여준다

    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(String keyword,Pageable pageable);

    @EntityGraph(attributePaths = {"imageSet"}) //조인 처리를 쿼리에 집어넣어서 하나의 쿼리에 imageSet까지 넣을 수 있도록 함.
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImage(Long bno);
}
