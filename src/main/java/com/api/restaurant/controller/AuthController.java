package com.api.restaurant.controller;

import com.api.restaurant.interfaces.AuthRequestBodyDTO;
import com.api.restaurant.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Validação de login (cliente e dono de restaurante)")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Realiza login do usuário",
            description = "Valida login e senha para cliente ou dono de restaurante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject(value = "Login realizado com sucesso")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Login ou senha inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "type": "https://api.restaurant.com/errors/unauthorized",
                                      "title": "Não autorizado",
                                      "status": 401,
                                      "detail": "Não foi possível realizar o login"
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthRequestBodyDTO dto) {
        String auth = this.authService.login(dto);
        return ResponseEntity.ok(auth);
    }
}
