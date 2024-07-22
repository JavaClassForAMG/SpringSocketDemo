package jp.co.amgakuin.javaclasscr30.springsocketdemo.udpchat;

import java.net.InetAddress;
import java.util.Objects;

public class User {
    private InetAddress ipAddress;
    private int port;

    public User(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return port == user.port && ipAddress.equals(user.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }
}