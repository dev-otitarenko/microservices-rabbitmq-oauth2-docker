package com.maestro.app.sample.ms.messages.controllers;

import com.maestro.app.sample.ms.messages.services.UserMessagesService;
import com.maestro.app.sample.ms.messages.services.auth.IAuthenticationFacade;
import com.maestro.app.sample.ms.messages.utils.ExtensionUtils;
import com.maestro.app.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = MessageController.class)
@ActiveProfiles("test")
public class MessageControllerNoAuthTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserMessagesService messageService;
    @MockBean
    private IAuthenticationFacade authService;

    @Test
    @DisplayName("Trying to retrieve messages without authentication")
    public void getUserMessages() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ExtensionUtils.requestBody(null)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();;
    }

    @Test
    @DisplayName("Trying to retrieve the count of messages without authentication")
    public void countUserMessages() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ExtensionUtils.requestBody(null)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();;
    }

    @Test
    @DisplayName("Trying to remove the specific message without authentication")
    public void deleteUserMessage() throws Exception {
        mockMvc.perform(
                delete("/{id}", CommonUtils.generateGuid())
                        .content(ExtensionUtils.requestBody(null)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
