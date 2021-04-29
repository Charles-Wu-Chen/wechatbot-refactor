package au.com.charleswu.wechatbot.adaptor.in.web;

import au.com.charleswu.wechatbot.adaptor.in.web.dto.MessageDTO;
import au.com.charleswu.wechatbot.application.in.MessageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@Api(value = "/messages", tags = "messages")
public class MessageController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody MessageDTO newMessage) {
        logger.info(newMessage.toString());

        service.sendMessage(newMessage);
    }
}
