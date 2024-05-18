package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.ExceptionService;
import com.kalma.Patienten.Dossier.Services.PatientService;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PatientControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    @MockBean
    ExceptionService exceptionService;


    @Test
    void shouldReceivePatients() throws Exception {

        Employee employee1 = new Employee();
        employee1.setId(1L);

        Employee employee2 = new Employee();
        employee2.setId(2L);

        Employee employee3 = new Employee();
        employee3.setId(3L);
        // Example IDs
        PatientDto patientDto = new PatientDto();
        patientDto.id = 1L;
        patientDto.firstName = "Kim";
        patientDto.lastName = "Kardashian";
        patientDto.fullName = "Kim Kardashian";
        patientDto.dossierId = 1L;
        patientDto.nextAppointment = LocalDate.parse("2024-06-01");
        patientDto.employeeIds = Arrays.asList(employee1.getId(), employee2.getId(), employee3.getId());;

        PatientDto patientDto2 = new PatientDto();
        patientDto2.id = 2L;
        patientDto2.firstName = "Tony";
        patientDto2.lastName = "Hawk";
        patientDto2.fullName = "Tony Hawk";
        patientDto2.dossierId = 2L;
        patientDto2.nextAppointment = LocalDate.parse("2024-06-08");
        patientDto2.employeeIds = Arrays.asList(employee1.getId(), employee2.getId(), employee3.getId());
        List<PatientDto> patients = Arrays.asList(patientDto, patientDto2);

        when(patientService.getAllPatients()).thenReturn(patients);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/patients"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Kim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Kardashian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Kim Kardashian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dossierId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nextAppointment").value("2024-06-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employeeIds[0]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employeeIds[1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employeeIds[2]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Tony"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Hawk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName").value("Tony Hawk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dossierId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nextAppointment").value("2024-06-08"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employeeIds[0]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employeeIds[1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employeeIds[2]").value(3));
    }

    @Test
    void shouldUpdateNextAppointment() throws Exception {
        // Arrange
        String nextAppointmentString = "2024-06-01";
        Long patientId = 1L;
        String employeeUsername = "testEmployee";
        LocalDate nextAppointment = LocalDate.parse(nextAppointmentString);
        String appointmentConfirmed = "Appointment confirmed";

        when(patientService.setNextAppointment(nextAppointmentString, patientId, employeeUsername, nextAppointment))
                .thenReturn(appointmentConfirmed);

        // Act
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/patients/appointment")
                        .param("nextAppointment", nextAppointmentString)
                        .param("patientId", patientId.toString())
                        .param("employeeUsername", employeeUsername))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
