package com.pyramid.usermanagement.domain.user.integration;

import com.pyramid.usermanagement.domain.user.model.AppUser;
import com.pyramid.usermanagement.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Suvorov Vassilievitch
 * Date: 08/04/2025
 * Time: 10:56
 * Project Name: user-management
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;
    private AppUser appUser1;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        appUser1 = new AppUser();
        appUser1.setLogin("washington");
        appUser1.setName("washington barry");
        appUser1.setEmail("washington@yahoo.fr");
        appUser1.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(appUser1));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser1));

    }

    @Test
    void whenUnauthenticatedThenUnauthorized() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"fally\", \"name\":\"fally\", \"email\":\"fallyi@yahoo.com\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    void whenAuthenticatedAsUserThenForbiddenForAdminEndpoints() throws Exception {

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/by-email/fallyi@yahoo.com"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"omar\", \"name\":\"omar diakite\", \"email\":\"omardiak@yahoo.com\"}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"fally\", \"name\":\"fally\", \"email\":\"update@yahoo.com\"}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled
    @WithMockUser(roles = "ADMIN")
    void whenAuthenticatedAsAdminThenAllowed() throws Exception {

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/by-email/fallyi@yahoo.com"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"fally\", \"name\":\"fally\", \"email\":\"fallyi@yahoo.com\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"fally\", \"name\":\"fally\", \"email\":\"update@yahoo.com\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPublicEndpoints() throws Exception {

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"new@example.com\",\"password\":\"password\",\"roles\": [\"ROLE_ADMIN\"]}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

    }


}
