package ru.iammaxim.Coordinator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by maxim on 7/21/17.
 */
public class MAddress {
    public InetAddress address;
    public int port;

    public MAddress(String address, int port) throws UnknownHostException {
        this.address = InetAddress.getByName(address);
        this.port = port;
    }

    public MAddress(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
}
