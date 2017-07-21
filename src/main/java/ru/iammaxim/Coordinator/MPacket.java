package ru.iammaxim.Coordinator;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by maxim on 7/21/17.
 */
public class MPacket {
    // TODO: check for leaks
    private DatagramPacket packet;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private DataOutputStream dos = new DataOutputStream(baos);
    private ByteArrayInputStream bais;
    private DataInputStream dis;

    public MPacket() {
        packet = new DatagramPacket(new byte[0], 0);
    }

    public MPacket(DatagramPacket packet) throws IOException {
        this.packet = packet;
        bais = new ByteArrayInputStream(packet.getData());
        dis = new DataInputStream(bais);
        baos.write(packet.getData());
    }

    public MPacket(byte[] data) throws IOException {
        this.packet = new DatagramPacket(data, data.length);
        bais = new ByteArrayInputStream(data);
        dis = new DataInputStream(bais);
        baos.write(data);
    }

    public MPacket setAddress(String address) throws UnknownHostException {
        setAddress(InetAddress.getByName(address));
        return this;
    }

    public MPacket setAddress(InetAddress address) {
        packet.setAddress(address);
        return this;
    }

    public MPacket setPort(int port) {
        packet.setPort(port);
        return this;
    }

    public DataOutputStream getDOS() {
        return dos;
    }

    public DataInputStream getDIS() {
        return dis;
    }

    public DatagramPacket getPacket() {
        return packet;
    }

    public InetAddress getAddress() {
        return packet.getAddress();
    }

    public int getPort() {
        return packet.getPort();
    }

    public void send(DatagramSocket socket) throws IOException {
        packet.setData(baos.toByteArray());
        socket.send(packet);
    }

    public void send(MServerSocket ss) throws IOException {
        send(ss.getSocket());
    }
}
