package com.hengyi.japp.fund;

import com.hengyi.japp.fund.domain.Operator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by jzb on 16-10-26.
 */
public class Util {
    public static String jwtToken(Operator operator) {
        return Jwts.builder()
                .setSubject(operator.getId())
                .signWith(SignatureAlgorithm.HS512, Constant.JWT_KEY)
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
    }

    public static Claims claims(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(Constant.JWT_KEY)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public static <T> T getSingle(TypedQuery<T> q) {
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
