package com.api.restaurant.controller;

import com.api.restaurant.interfaces.*;
import com.api.restaurant.service.CustomerService;
import com.api.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurant", description = "CRUD e busca de donos/restaurantes")
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public RestaurantController(CustomerService customerService, RestaurantService restaurantService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Listar/buscar restaurantes por nome")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de restaurantes",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResponseBodyRestaurantDTO.class))
                    )
            )
    })
    @GetMapping()
    public ResponseEntity<List<ResponseBodyRestaurantDTO>> getRestaurants(@RequestParam String restaurantName) {
        List<ResponseBodyRestaurantDTO> restaurants = this.restaurantService.getRestaurants(restaurantName);
        return ResponseEntity.ok().body(restaurants);
    }

    @Operation(summary = "Criar restaurante/dono")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado"),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail ou login já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "type": "https://api.restaurant.com/errors/conflict",
                                      "title": "Conflito de dados",
                                      "status": 409,
                                      "detail": "E-mail ou Login já cadastrados"
                                    }
                                    """)
                    )
            )
    })
    @PostMapping()
    public ResponseEntity<Void> createRestaurant(@RequestBody CreateRequestBodyRestaurantDTO dto) {
        this.restaurantService.createRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualizar dados do restaurante (sem senha)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante atualizado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseBodyRestaurantDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PutMapping("/{restaurantId}")
    public ResponseEntity<ResponseBodyRestaurantDTO> editRestaurant(@PathVariable Long restaurantId, @RequestBody EditRequestBodyRestaurantDTO dto) {
        ResponseBodyRestaurantDTO restaurant = restaurantService.editRestaurant(restaurantId, dto);
        return ResponseEntity.ok().body(restaurant);
    }

    @Operation(summary = "Trocar senha do dono do restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Senha atual incorreta",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "type": "https://api.restaurant.com/errors/bad-request",
                                      "title": "Requisição inválida",
                                      "status": 400,
                                      "detail": "A senha atual informada está incorreta."
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PatchMapping("/{restaurantId}/change-password")
    public ResponseEntity<Void> editRestaurantPassword(@PathVariable Long restaurantId, @RequestBody EditRequestBodyRestaurantPasswordDTO dto) {
        restaurantService.editRestaurantPassword(restaurantId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Excluir restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante excluído")
    })
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar clientes vinculados ao restaurante")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de clientes do restaurante",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResponseBodyCustomerDTO.class))
                    )
            )
    })
    @GetMapping("/{restaurantId}/customers")
    public ResponseEntity<List<ResponseBodyCustomerDTO>> getUsersByRestaurant(@PathVariable Long restaurantId) {
        List<ResponseBodyCustomerDTO> customers = customerService.getCustomersByRestaurant(restaurantId);
        return ResponseEntity.ok().body(customers);
    }

    @Operation(summary = "Criar cliente vinculado a um restaurante")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente criado e vinculado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseBodyCustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PostMapping("/{restaurantId}/customers")
    public ResponseEntity<ResponseBodyCustomerDTO> createCustomerToRestaurant(@PathVariable Long restaurantId, @RequestBody CreateRequestBodyCustomerDTO dto) {
        ResponseBodyCustomerDTO customer = customerService.createCustomerToRestaurant(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
}
