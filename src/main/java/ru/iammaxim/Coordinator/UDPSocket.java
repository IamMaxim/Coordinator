package ru.iammaxim.Coordinator;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by maxim on 7/16/17.
 */
public class UDPSocket {
    private InetAddress addr;
    private int port;
    private DatagramSocket socket;

    public UDPSocket(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;

        new Thread(() -> {

        }).start();
    }
}
