package org.zerock.springbootex.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.springbootex.dto.upload.UploadFileDTO;
import org.zerock.springbootex.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RequestMapping("/api")
@RestController
@Log4j2
public class UpDownController {


    @Value("${org.zerock.upload.path}") //import 시 springframework로 시작하는 value
    private String uploadpath;

    @Operation(summary = "upload file",description = "POST 방식으로 파일 등록")
    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(
            @Parameter(
                    description = "File to be uploaded",
                    content = @Content(mediaType=MediaType.MULTIPART_FORM_DATA_VALUE)
            ) //opeanAPI를 사용할 때 파일을 파라미터로 주고 싶으면 파라미터 어노테이션을 이용해야한다.
            UploadFileDTO uploadFileDTO){
        log.info(uploadFileDTO);
        if(uploadFileDTO.getFiles()!=null){

            final List<UploadResultDTO> list=new ArrayList<>();

            uploadFileDTO.getFiles().forEach(
                    multipartFile -> {
                        String originalName=multipartFile.getOriginalFilename();
                        log.info(originalName);

                        String uuid= UUID.randomUUID().toString();
                        Path savePath= Paths.get(uploadpath,uuid+"_"+originalName);
                        boolean img=false;
                        try{
                            multipartFile.transferTo(savePath); //저장

                            if(Files.probeContentType(savePath).startsWith("image")){
                                File thumbFile=new File(uploadpath,"s_"+uuid+"_"+originalName);
                                Thumbnailator.createThumbnail(savePath.toFile(),thumbFile,200,200);
                                //썸네일 저장.
                                img=true;
                            }

                            list.add(
                                    UploadResultDTO.builder()
                                            .img(img)
                                            .fileName(originalName)
                                            .uuid(uuid)
                                            .build()
                            );
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
            );
            return list;
        }

        return null;
    }

    @Operation(summary = "read file GET", description = "GET 방식으로 파일조회")
    @GetMapping(value = "/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        //ResponseEntity: HTTP에서 응답 메시지를 보낼 때 내용을 정할 수 있다. 상태 코드,헤더, 페이로드를 정해서 보낼 수 있음.
        Resource resource=new FileSystemResource(uploadpath+File.separator+fileName);

        String resourceName=resource.getFilename();
        HttpHeaders header=new HttpHeaders();

        try{
            header.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(header).body(resource);

    }


    @Operation(summary = "delete file ",description = "파일을 DELETE 방식으로 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){
        Resource resource=new FileSystemResource(uploadpath+File.separator+fileName);
        String resoruceName=resource.getFilename();

        Map<String,Boolean> resultMap=new HashMap<>();
        boolean removed=false;

        try{
            String contentType=Files.probeContentType(resource.getFile().toPath());
            removed=resource.getFile().delete();

            if(contentType.startsWith("image")){
                File thumbFile=new File(uploadpath+File.separator+"s_"+fileName);

                thumbFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        resultMap.put("result",removed);

        return resultMap;
    }

}
