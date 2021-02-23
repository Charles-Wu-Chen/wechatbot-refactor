package au.com.charleswu.wechatbot.domain.message.mapper;


import au.com.charleswu.wechatbot.domain.message.Message;

public interface MessageMapper {

    Message mapToMessage(
            io.github.wechaty.user.Message wechatyMessage) ;
}

