package com.javer.ms_storage.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void devePassarValidacao_quandoTodosCamposValidos() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("Joao Silva")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void deveFalharValidacao_quandoNomeNulo() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome(null)
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nome"));
    }

    @Test
    void deveFalharValidacao_quandoNomeVazio() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nome"));
    }

    @Test
    void deveFalharValidacao_quandoTelefoneNulo() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("Joao Silva")
                .telefone(null)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telefone"));
    }

    @Test
    void deveFalharValidacao_quandoCorrenistaНulo() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("Joao Silva")
                .telefone(11999998888L)
                .correntista(null)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("correntista"));
    }

    @Test
    void deveFalharValidacao_quandoScoreCreditoNulo() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("Joao Silva")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(null)
                .saldoCc(2500.0f)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("scoreCredito"));
    }

    @Test
    void deveFalharValidacao_quandoSaldoCcNulo() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .nome("Joao Silva")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(null)
                .build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("saldoCc"));
    }

    @Test
    void deveFalharValidacao_quandoTodosCamposNulos() {
        ClienteRequestDTO dto = ClienteRequestDTO.builder().build();

        Set<ConstraintViolation<ClienteRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(5);
    }
}