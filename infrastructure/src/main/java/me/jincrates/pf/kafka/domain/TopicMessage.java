package me.jincrates.pf.kafka.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopicMessage<T> implements Serializable {

    private String id;
    private String action;
    private String actionValue;
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
