package ru.iammaxim.Coordinator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by maxim on 7/21/17.
 */
public class MServerSocket {
    private static final int bufferSize = 1024;

    private DatagramSocket ss;

    public MServerSocket(int port) throws SocketException {
        ss = new DatagramSocket(port);
    }

    public MPacket receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);
        ss.receive(packet);
        return new MPacket(packet);
    }

    public DatagramSocket getSocket() {
        return ss;
    }
}
