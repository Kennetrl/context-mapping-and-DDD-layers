package com.veritrabajo.backend.serviceexecution.application;

import com.veritrabajo.backend.serviceexecution.domain.exception.ServiceExecutionNotFoundException;
import com.veritrabajo.backend.serviceexecution.domain.model.EvidencePhoto;
import com.veritrabajo.backend.serviceexecution.domain.model.ServiceExecution;
import com.veritrabajo.backend.serviceexecution.domain.model.ServiceExecutionData;
import com.veritrabajo.backend.serviceexecution.domain.model.ServiceExecutionStatus;
import com.veritrabajo.backend.shared.api.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ServiceExecutionControllerWebTest {

    private static final int BAD_REQUEST_STATUS = 400;
    private static final int NOT_FOUND_STATUS = 404;

    @Mock
    private ServiceExecutionApplicationService service;

    private MockMvc mockMvc;
    private UUID executionId;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ServiceExecutionController(service))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        this.executionId = UUID.randomUUID();
    }

    @Test
    void addPhotoShouldReturnOkForValidImage() throws Exception {
        when(service.addPhoto(eq(executionId), anyString(), any(byte[].class)))
                .thenReturn(buildExecutionWithPhoto(executionId));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.png",
                "image/png",
                "hello".getBytes()
        );

        mockMvc.perform(multipart("/api/service-executions/{id}/photos", executionId).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(executionId.toString()))
                .andExpect(jsonPath("$.photoUrls[0]").value("uploads/test-photo.png"));

        verify(service).addPhoto(eq(executionId), eq("photo.png"), any(byte[].class));
    }

    @Test
    void addPhotoShouldReturnBadRequestForNonImageContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "hello".getBytes()
        );

        mockMvc.perform(multipart("/api/service-executions/{id}/photos", executionId).file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_STATUS))
                .andExpect(jsonPath("$.message").value("Only image files are allowed"))
                .andExpect(jsonPath("$.path")
                        .value("/api/service-executions/" + executionId + "/photos"));

        verifyNoInteractions(service);
    }

    @Test
    void addPhotoShouldReturnBadRequestForEmptyImageFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.png",
                "image/png",
                new byte[0]
        );

        mockMvc.perform(multipart("/api/service-executions/{id}/photos", executionId).file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST_STATUS))
                .andExpect(jsonPath("$.message").value("Image file is required"));

        verifyNoInteractions(service);
    }

    @Test
    void addPhotoShouldReturnNotFoundWhenExecutionDoesNotExist() throws Exception {
        when(service.addPhoto(eq(executionId), anyString(), any(byte[].class)))
                .thenThrow(new ServiceExecutionNotFoundException(executionId));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.png",
                "image/png",
                "hello".getBytes()
        );

        mockMvc.perform(multipart("/api/service-executions/{id}/photos", executionId).file(file))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND_STATUS))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Execution not found: " + executionId));
    }

    private static ServiceExecution buildExecutionWithPhoto(UUID id) {
        return ServiceExecution.rehydrate(new ServiceExecutionData(
                id,
                "client-1",
                "worker-1",
                ServiceExecutionStatus.IN_PROCESS,
                List.of(EvidencePhoto.of("uploads/test-photo.png"))
        ));
    }
}
