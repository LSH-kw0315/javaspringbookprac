package org.zerock.springbootex.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    private String type;

    private String keyword;

    private String link;

    public String[] getTypes(){
        if(type==null || type.length()==0){
            return null;
        }

        return type.split("");
    }

    public Pageable getPageable(String...props){ //현재 페이지, 한 번에 나열되는 원소 개수
        return PageRequest.of(this.page-1,this.size, Sort.by(props).descending());
    }
    public String getLink(){ //검색 조건의 전달
        if(link==null){
            StringBuilder builder=new StringBuilder();
            builder.append("page="+this.page);
            builder.append("&size="+this.size);
            if(type!=null && type.length()>0) {
                builder.append("&types=" + this.type);
            }
            if(keyword!=null && !keyword.isEmpty()){
                try{
                    builder.append("&keyword"+ URLEncoder.encode(keyword,"UTF-8"));
                }catch (Exception e){

                }
            }
            link=builder.toString();
        }

        return link;
    }

}
