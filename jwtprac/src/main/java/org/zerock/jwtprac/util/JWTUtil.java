package org.zerock.jwtprac.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Log4j2
public class JWTUtil {

    @Value("${org.zerock.jwtprac.secret}")
    private String key;

    private final int DAY=60*24;
    private final int MINUTE=1;

    public String generateToken(Map<String,Object> valueMap,int days){
        log.info("generateToken..."+key);

        Map<String,Object> headers=new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        Map<String, Object> payloads = new HashMap<>(valueMap);

        int time=(DAY)*days;

        Date isat=Date.from(ZonedDateTime.now().toInstant());
        Date exp=Date.from(ZonedDateTime.now().plusMinutes(time).toInstant());

        log.info("isat:"+isat.getTime());
        log.info("exp:"+exp.getTime());
        String jwtStr= Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(isat)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256,key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String,Object> validateToken(String token) throws JwtException {
        Map<String,Object> claim=null;

        claim=Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }
}
