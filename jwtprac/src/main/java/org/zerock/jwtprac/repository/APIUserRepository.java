package org.zerock.jwtprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.jwtprac.domain.APIUser;


public interface APIUserRepository extends JpaRepository<APIUser,String> {
}
