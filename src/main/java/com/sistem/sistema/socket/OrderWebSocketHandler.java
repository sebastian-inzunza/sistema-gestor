package com.sistem.sistema.socket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.services.OrdenEstatus;
import com.sistem.sistema.services.OrdenesService;

@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {
    
    @Autowired
    OrdenesService ordenesService;

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @SuppressWarnings("")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage(getWaitingOrdersJson()));
    }

    @SuppressWarnings("")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage(message.toString()));
    }

    public boolean notificarOrdenCreada() throws Exception{
     
        for(WebSocketSession session: sessions){
            session.sendMessage(new TextMessage(getWaitingOrdersJson()));
        }

        return true;
    }

    public boolean notificarCambioEstatusOrdenes(String orderId, String estatus) throws Exception{
        ordenesService.CambiarEstatus(Long.valueOf(orderId), estatus);
        
        for(WebSocketSession session: sessions){
            session.sendMessage(new TextMessage(getWaitingOrdersJson()));
        }

        return true;
    }


    private String getWaitingOrdersJson() throws JsonProcessingException {
        // Convertir las órdenes con estatus "ESPERANDO" a JSON
        List<OrdenesEntity> waitingOrders = ordenesService.ObtenerPorEstatus(OrdenEstatus.ESPERANDO.toString());
        return new ObjectMapper().writeValueAsString(waitingOrders);
    }
    
}
