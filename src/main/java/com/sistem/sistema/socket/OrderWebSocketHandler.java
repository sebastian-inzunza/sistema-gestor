package com.sistem.sistema.socket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.services.OrdenesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderWebSocketHandler extends TextWebSocketHandler {
    
    @Autowired
    OrdenesService ordenesService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<OrdenesEntity> ordenes = ordenesService.ObtenerPorEstatus("ESPERANDO");
        session.sendMessage(new TextMessage(ordenes.toString()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long orderId = Long.parseLong(message.getPayload());
        ordenesService.CambiarEstatus(orderId, "PREPARANDO");

        List<OrdenesEntity> ordenes = ordenesService.ObtenerPorEstatus("ESPERANDO");

        if (session.isOpen()) {
            session.sendMessage(new TextMessage(ordenes.toString()));
        }
        
    }

        @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Long orderId = Long.parseLong((String) message.getPayload());

       
        session.sendMessage(new TextMessage("Cambiando orden a  estatus PREPARANDO: "));
        ordenesService.CambiarEstatus(orderId, "PREPARANDO");
        session.sendMessage(new TextMessage("Proceso compleatado"));

        List<OrdenesEntity> ordenes = ordenesService.ObtenerPorEstatus("ESPERANDO");

        if (session.isOpen()) {
            session.sendMessage(new TextMessage(ordenes.toString()));
        }
        
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    
}
