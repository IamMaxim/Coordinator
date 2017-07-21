package ru.iammaxim.Coordinator;

import java.io.IOException;
import java.util.HashMap;

import static ru.iammaxim.Coordinator.Protocol.AWAITING;
import static ru.iammaxim.Coordinator.Protocol.FOUND;

/**
 * Created by maxim on 7/13/17.
 */
public class CoordinatorServer {
    public void runSTUNServer() {
        new Thread(() -> {
            try {
                MServerSocket ss = new MServerSocket(3478);

                System.out.println("STUN server started at port " + 3478);

                while (true) {
                    MPacket packet = ss.receive();
                    new Thread(() -> {
                        try {
                            MPacket response = new MPacket()
                                    .setAddress(packet.getAddress())
                                    .setPort(packet.getPort());

                            response.getDOS().write(packet.getAddress().getAddress());
                            response.getDOS().writeInt(packet.getPort());

                            System.out.println("address: " + packet.getAddress());
                            System.out.println("port: " + packet.getPort());

                            response.send(ss);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void runCoordinatorServer() {
        new Thread(() -> {
            HashMap<String, MAddress> clients = new HashMap<>();

            try {
                MServerSocket ss = new MServerSocket(3479);

                System.out.println("Coordinator server started at port " + 3479);

                while (true) {
                    MPacket packet = ss.receive();
                    new Thread(() -> {
                        try {
                            String id = packet.getDIS().readUTF();

                            // there's someone awaiting for connection with this ID
                            if (clients.containsKey(id)) {
                                System.out.println("Found awaiting client for \"" + id + "\"");

                                MAddress addr = clients.remove(id);

                                // send response to new client
                                MPacket packet1 = new MPacket()
                                        .setAddress(packet.getAddress())
                                        .setPort(packet.getPort());

                                packet1.getDOS().write(FOUND);
                                packet1.send(ss);

                                // send new client's address and port to awaiting one
                                MPacket packet2 = new MPacket()
                                        .setAddress(addr.address)
                                        .setPort(addr.port);

                                packet2.getDOS().write(packet.getAddress().getAddress());
                                packet2.getDOS().writeInt(packet.getPort());
                                packet2.send(ss);
                            } else { // put this client in awaiting clients map
                                System.out.println("Adding client \"" + id + "\" into awaiting list");
                                clients.put(id, new MAddress(packet.getAddress(), packet.getPort()));
                                MPacket packet1 = new MPacket()
                                        .setAddress(packet.getAddress())
                                        .setPort(packet.getPort());

                                packet1.getDOS().write(AWAITING);
                                packet1.send(ss);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        CoordinatorServer server = new CoordinatorServer();
        server.runSTUNServer();
        server.runCoordinatorServer();
    }
}
