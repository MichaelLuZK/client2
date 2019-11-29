package com.casic.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate template;

    public void sendMessage() throws Exception{
        template.convertAndSend("/topic/getResponse","Welcome,websocket!");
    }
}
