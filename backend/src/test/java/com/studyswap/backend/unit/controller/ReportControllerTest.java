package com.studyswap.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyswap.backend.controller.ReportController;
import com.studyswap.backend.dto.CreateReportDTO;
import com.studyswap.backend.dto.ReportResponseDTO;
import com.studyswap.backend.model.ReportReason;
import com.studyswap.backend.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    void createReport_WhenValidMaterialReport_ReturnsCreatedReport() throws Exception {
        // Cenário de sucesso: Denúncia de Material
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportedMaterialId(1L);
        dto.setReportedUserId(null); // Explicitamente nulo para clareza
        dto.setReason(ReportReason.ITEM_NAO_CONFORME);
        dto.setDescription("O material não corresponde à descrição.");

        ReportResponseDTO responseDTO = new ReportResponseDTO(
                1L, 2L, null, 1L, ReportReason.ITEM_NAO_CONFORME, "O material não corresponde à descrição.");

        when(reportService.createReport(any(CreateReportDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.reason").value(responseDTO.getReason().toString()));

        verify(reportService, times(1)).createReport(any(CreateReportDTO.class));
    }

    @Test
    void createReport_WhenValidUserReport_ReturnsCreatedReport() throws Exception {
        // Cenário de sucesso: Denúncia de Usuário
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportedUserId(3L);
        dto.setReportedMaterialId(null); // Explicitamente nulo para clareza
        dto.setReason(ReportReason.SPAM);
        dto.setDescription("Usuário está enviando mensagens de spam.");

        ReportResponseDTO responseDTO = new ReportResponseDTO(
                2L, 1L, 3L, null, ReportReason.SPAM, "Usuário está enviando mensagens de spam.");

        when(reportService.createReport(any(CreateReportDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.reason").value(responseDTO.getReason().toString()));

        verify(reportService, times(1)).createReport(any(CreateReportDTO.class));
    }

    @Test
    void createReport_WhenBothUserAndMaterialReported_ThrowsBadRequest() throws Exception {
        // Cenário de falha: Denunciando usuário E material ao mesmo tempo
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportedUserId(1L);
        dto.setReportedMaterialId(1L);
        dto.setReason(ReportReason.FRAUDE);
        dto.setDescription("Denúncia inválida.");

        // O serviço lançaria a exceção, então simulamos o comportamento esperado
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "A denúncia deve ser contra apenas um tipo: usuário ou material."))
                .when(reportService).createReport(any(CreateReportDTO.class));

        mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(reportService, times(1)).createReport(any(CreateReportDTO.class));
    }

    @Test
    void createReport_WhenInvalidReason_ThrowsBadRequest() throws Exception {
        // Cenário de falha: DTO inválido (razão nula)
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportedUserId(1L);
        dto.setReason(null);
        dto.setDescription("Razão nula");

        // O MockMvc agora espera um status 400 Bad Request diretamente.
        // O método do serviço NUNCA será chamado devido à validação do @NotNull.
        mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        // Verifique que o método do serviço NUNCA foi chamado
        verify(reportService, never()).createReport(any(CreateReportDTO.class));
    }

    @Test
    void createReport_WhenNoTargetReported_ThrowsBadRequest() throws Exception {
        // Cenário de falha: Nem usuário nem material são denunciados
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportedUserId(null);
        dto.setReportedMaterialId(null);
        dto.setReason(ReportReason.FRAUDE);
        dto.setDescription("Nenhum alvo de denúncia.");

        // O serviço lançaria a exceção, então simulamos o comportamento esperado
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "A denúncia deve ser contra apenas um tipo: usuário ou material."))
                .when(reportService).createReport(any(CreateReportDTO.class));

        mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(reportService, times(1)).createReport(any(CreateReportDTO.class));
    }
}