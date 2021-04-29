package au.com.charleswu.wechatbot.adaptor.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import au.com.charleswu.wechatbot.adaptor.in.web.dto.MessageDTO;
import au.com.charleswu.wechatbot.application.in.MessageService;
import au.com.charleswu.wechatbot.domain.message.MessageChannel;
import au.com.charleswu.wechatbot.domain.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService service;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        MessageDTO messageDTO = MessageDTO.builder()
                .messageChannel(MessageChannel.Person)
                .messageType(MessageType.Text)
                .toID("wechatid")
                .content("test content")
                .build();

        doNothing().when(service).sendMessage(any(MessageDTO.class));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(messageDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
