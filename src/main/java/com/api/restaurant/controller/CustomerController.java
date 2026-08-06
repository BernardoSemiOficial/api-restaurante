package com.api.restaurant.controller;

import com.api.restaurant.interfaces.CreateRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerDTO;
import com.api.restaurant.interfaces.EditRequestBodyCustomerPasswordDTO;
import com.api.restaurant.interfaces.ResponseBodyCustomerDTO;
import com.api.restaurant.service.CustomerService;
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

@Tag(name = "Customer", description = "CRUD e busca de clientes")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Buscar cliente pelo ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseBodyCustomerDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> findCustomer(@PathVariable Long customerId) {
        return customerService.findCustomer(customerId)
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar clientes pelo nome")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de clientes (pode ser vazia)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ResponseBodyCustomerDTO.class))
                    )
            )
    })
    @GetMapping()
    public ResponseEntity<List<ResponseBodyCustomerDTO>> findCustomer(@RequestParam String customerName) {
        List<ResponseBodyCustomerDTO> customers = customerService.findCustomerByName(customerName);
        return ResponseEntity.ok().body(customers);
    }

    @Operation(summary = "Criar cliente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente criado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseBodyCustomerDTO.class)
                    )
            ),
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
    public ResponseEntity<ResponseBodyCustomerDTO> createCustomer(@RequestBody CreateRequestBodyCustomerDTO dto) {
        ResponseBodyCustomerDTO customer = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @Operation(summary = "Atualizar dados do cliente (sem senha)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente atualizado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseBodyCustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado",
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
    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseBodyCustomerDTO> editCustomer(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerDTO dto) {
        ResponseBodyCustomerDTO customer = customerService.editCustomer(customerId, dto);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "Trocar senha do cliente")
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
                    description = "Cliente não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PatchMapping("/{customerId}/change-password")
    public ResponseEntity<Void> editCustomerPassword(@PathVariable Long customerId, @RequestBody EditRequestBodyCustomerPasswordDTO dto) {
        customerService.editCustomerPassword(customerId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Excluir cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente excluído")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }
}
