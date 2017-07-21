package ru.iammaxim.Coordinator;

import java.io.IOException;
import java.net.InetAddress;

import static ru.iammaxim.Coordinator.Protocol.AWAITING;
import static ru.iammaxim.Coordinator.Protocol.FOUND;

/**
 * Created by maxim on 7/13/17.
 */
public class CoordinatorClient {
    public static String getAddressUsingSTUN(MServerSocket ss, String addr, int port) throws IOException {
        MPacket packet = new MPacket()
                .setAddress(addr)
                .setPort(port);
        packet.getDOS().write(0);
        packet.send(ss);

        MPacket response = ss.receive();

        byte[] remoteAddr = new byte[4];
        response.getDIS().readFully(remoteAddr);

        int remotePort = response.getDIS().readInt();

        InetAddress remoteInetAddress = InetAddress.getByAddress(remoteAddr);
        return remoteInetAddress.getHostAddress() + ":" + remotePort;
    }

    public static void estabilishConnection(MServerSocket ss, String addr, int port, String id) throws IOException {
        MPacket packet = new MPacket()
                .setAddress(addr)
                .setPort(port);

        packet.getDOS().writeUTF(id);
        packet.send(ss);

        MPacket response = ss.receive();

        int resCode = response.getDIS().read();

        if (resCode == FOUND) {
            System.out.println("Awaiting client found!");

            // receive test packet
            MPacket packet1 = ss.receive();
            System.out.println("Test packet: " + packet1.getDIS().readUTF());
        } else if (resCode == AWAITING) {
            System.out.println("Added into awaiting list");
            MPacket response1 = ss.receive();

            byte[] addr1 = new byte[4];
            response1.getDIS().readFully(addr1);
            InetAddress remoteAddr = InetAddress.getByAddress(addr1);
            int remotePort = response1.getDIS().readInt();

            System.out.println("Remote client address: " + remoteAddr.getHostAddress() + ":" + remotePort);

            // send test packet
            MPacket testPacket = new MPacket()
                    .setAddress(remoteAddr)
                    .setPort(remotePort);

            testPacket.getDOS().writeUTF("Test message");
            testPacket.send(ss);
        }
    }

    public static void main(String[] args) throws IOException {
        MServerSocket ss = new MServerSocket((int) (Math.random() * 65533 + 1));

        System.out.println("This device external address: " + getAddressUsingSTUN(ss, "localhost", 3478));

        estabilishConnection(ss, "localhost", 3479, "IamMaxim");
    }
}
