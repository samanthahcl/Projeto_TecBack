package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.LoginRequestDTO;
import br.uniesp.si.techback.dto.LoginResponseDTO;
import br.uniesp.si.techback.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // RF4 - API de login para autenticação do usuário
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }
}