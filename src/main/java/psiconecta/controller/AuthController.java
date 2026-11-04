package psiconecta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.auth.LoginRequestDTO;
import psiconecta.dto.auth.LoginResponseDTO;
import psiconecta.dto.usuario.UsuarioResponseDTO;
import psiconecta.mapper.UsuarioMapper;
import psiconecta.model.Usuario;
import psiconecta.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioMapper usuarioMapper;

    public AuthController(
            AuthService authService,
            UsuarioMapper usuarioMapper) {

        this.authService = authService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO resposta =
                authService.login(dto);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(
            Authentication authentication) {

        Usuario usuario =
                (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                usuarioMapper.toResponse(usuario)
        );
    }
}