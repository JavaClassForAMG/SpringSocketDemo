package jp.co.amgakuin.javaclasscr30.springsocketdemo.udpchat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WebSocketUDPBridgeHandler extends TextWebSocketHandler {
    private DatagramSocket socket;
    @Value("${udp.server.port}")
    private int udpPort; // Injected from application.properties
    @Value("${udp.server.host:localhost}")
    private String udpHost; // Default to localhost if not specified

    public WebSocketUDPBridgeHandler() throws IOException {
        this.socket = new DatagramSocket();
        new Thread(this::listenToUDP).start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        byte[] buf = message.getPayload().getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(udpHost), udpPort);
        socket.send(packet);
    }

    private void listenToUDP() {
        byte[] buf = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                // Implement WebSocket session management to send messages back to clients
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}