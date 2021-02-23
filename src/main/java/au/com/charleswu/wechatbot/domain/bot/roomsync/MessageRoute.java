package au.com.charleswu.wechatbot.domain.bot.roomsync;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageRoute {
    private String sourceName;
    private String destinationName;
}
