package psiconecta.service;

import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import psiconecta.model.Usuario;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String gerarToken(Usuario usuario) {

        Date agora = new Date();

        Date expiracao = new Date(
                agora.getTime() + expiration
        );

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim(
                        "usuarioId",
                        usuario.getId()
                )
                .claim(
                        "perfil",
                        usuario.getPerfil().name()
                )
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey())
                .compact();
    }

    public String extrairEmail(String token) {

        return extrairClaims(token)
                .getSubject();
    }

    public Long extrairUsuarioId(String token) {

        Number usuarioId = extrairClaims(token)
                .get(
                        "usuarioId",
                        Number.class
                );

        return usuarioId.longValue();
    }

    public boolean tokenValido(String token) {

        try {

            Claims claims = extrairClaims(token);

            return claims
                    .getExpiration()
                    .after(new Date());

        } catch (Exception exception) {

            return false;
        }
    }

    private Claims extrairClaims(String token) {

    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
}