package jp.co.amgakuin.javaclasscr30.springsocketdemo.udpchat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UDPServer {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private Set<User> users = Collections.newSetFromMap(new ConcurrentHashMap<User, Boolean>());

    public UDPServer(@Value("${udp.server.port}") int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        new Thread(this::listen).start();
    }

    public void listen() {
        running = true;
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                User user = new User(packet.getAddress(), packet.getPort());
                users.add(user);

                for (User u : users) {
                    byte[] dataToSend = received.getBytes();
                    DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, u.getIpAddress(), u.getPort());
                    socket.send(packetToSend);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}