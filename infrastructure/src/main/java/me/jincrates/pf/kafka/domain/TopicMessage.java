package me.jincrates.pf.kafka.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class TopicMessage<T> {

    private String id;
    private String action;
    private String actionValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime occurredAt;
    private T data;

    @Builder
    public TopicMessage(String id, String action, String actionValue, T data) {
        this.id = id;
        this.action = action;
        this.actionValue = actionValue;
        this.occurredAt = LocalDateTime.now();
        this.data = data;
    }
}
