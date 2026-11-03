package psiconecta.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import psiconecta.dto.auth.LoginRequestDTO;
import psiconecta.dto.auth.LoginResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(
            LoginRequestDTO dto) {

        String email = dto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "E-mail ou senha inválidos."
                        )
                );

        boolean senhaCorreta =
                passwordEncoder.matches(
                        dto.senha(),
                        usuario.getSenha()
                );

        if (!senhaCorreta) {

            throw new IllegalArgumentException(
                    "E-mail ou senha inválidos."
            );
        }

        if (!Boolean.TRUE.equals(
                usuario.getAtivo()
        )) {

            throw new IllegalArgumentException(
                    "Usuário inativo."
            );
        }

        String token =
                jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil()
        );
    }
}