import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch {
    String name;
    int port;
    Map<String, String> forwardingTable;
    List<Device> connectedDevices;

    public void receiveUDP() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                SocketAddress socketAddress = packet.getSocketAddress();
                if (socketAddress instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                    String sourceIP = inetSocketAddress.getAddress().getHostAddress();
                    int sourcePort = inetSocketAddress.getPort();
                    System.out.println(sourceIP + ":" + sourcePort);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Switch(String name, int port) {
        this.name = name;
        this.port = port;
        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
    }

    public void receiveFrame(Frame frame, String incomingPort) {
        String sourceMac = frame.srcMac;
        if (!forwardingTable.containsKey(sourceMac)) {
            forwardingTable.put(sourceMac, incomingPort);
        }
    }


    public void forwardFrame(Frame frame, String outgoingPort) {
        if (forwardingTable.containsKey(frame.destMac)) {
            String destinationInfo = forwardingTable.get(frame.destMac);
            System.out.println("Forwarding frame to " + destinationInfo);
        } else {
            System.out.println("Destination MAC address not found in forwarding table. Flooding the frame.");
            floodFrame(frame);
        }
    }


    private void floodFrame(Frame frame) {
        for (Device device : connectedDevices) {
            if (device.getPort() != frame.inPort) {
                System.out.println("Flooding frame to " + device.getName());
                if (device instanceof VirtualPC) {
//                    VirtualPC virtualPC = (VirtualPC) device;
                    //send message to everyone
                }
            }
        }
    }


    public void connect(Device device) {
        connectedDevices.add(device);
        System.out.println(device.getName() + " connected to " + name);
    }
}
