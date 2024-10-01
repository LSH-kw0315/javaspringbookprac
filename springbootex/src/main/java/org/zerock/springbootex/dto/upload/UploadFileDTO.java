package org.zerock.springbootex.dto.upload;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
//get,set,tostring,equals,hashcode,requiredArgsConsturctor을 제공
public class UploadFileDTO {
    private List<MultipartFile> files;
}
