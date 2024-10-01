package org.zerock.jwtprac.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIUser {
    @Id
    private String mid;
    private String mpw;

    public void changePassWord(String mpw){
        this.mpw=mpw;
    }
}
