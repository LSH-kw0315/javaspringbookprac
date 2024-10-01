package org.zerock.jwtprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jwtprac.domain.Todo;
import org.zerock.jwtprac.repository.search.TodoSearch;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoSearch {
}
