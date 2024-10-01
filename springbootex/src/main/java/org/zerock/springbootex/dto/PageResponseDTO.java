package org.zerock.springbootex.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    private int start;
    private int end;
    //화면 구성 단계에서 사용하기 위한 정보.

    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,List<E> dtoList,int total){

        if(total<=0){
            return;
        }

        this.page=pageRequestDTO.getPage();
        this.size=pageRequestDTO.getSize();

        this.total=total;
        this.dtoList=dtoList;

        this.end=(int)(Math.ceil(this.page/10.0))*10;
        //화면에서의 마지막 번호.

        this.start=this.end-9;

        int last=(int)(Math.ceil((total/(double)size)));
        //토탈을 기계적으로 사이즈로 나눴을 때 나오는 페이지의 수

        this.end=end>last?last:end;

        this.prev=this.start>1;

        this.next=total>this.end*this.size;
    }

}
