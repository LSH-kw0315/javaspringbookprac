package org.zerock.springbootex.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
@Table(name="Reply",indexes = {@Index(name="idx_reply_board_bno",columnList = "board_bno")})
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;
    private String replyer;

    public void changeText(String replyText){
        this.replyText=replyText;
    }
}
