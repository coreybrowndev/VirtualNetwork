import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch extends Device {
    Map<String, String> forwardingTable;
    static List<Device> connectedDevices;

    public Switch(String name) {
        super(name);
        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(this.getPort().intValue());
            Receiver receiver = new Receiver(socket);
            Thread receiverThread = new Thread(receiver);
            receiverThread.start();
        }catch(SocketException e) {
            throw new RuntimeException(e);
        }
    }


    static class Receiver implements Runnable {
        //grab packet, look at headers that contain the src IP
        private final DatagramSocket socket;

        Receiver(DatagramSocket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while(true) {
                    socket.receive(packet);
                    String data = new String(packet.getData(), 0, packet.getLength());

                    System.out.println("Data: " + data);

                    Switch.forwardPacket(data);
                    //extract the information data
                    Frame receivedFrame = Frame.deserialize(data);

                    String srcMac = receivedFrame.getSrcMac();
                    String destMac = receivedFrame.getDestMac();
                    String message = receivedFrame.getMessage();

                    System.out.println("Source MAC: " + srcMac);
                    System.out.println("Destination MAC: " + destMac);

                    if(destMac.equals("B")) {
                        System.out.println("Received message: " + message);
                    }

                    System.out.println("Received frame:");
                    System.out.println("Source MAC: " + srcMac);
                    System.out.println("Destination MAC: " + destMac);
                    System.out.println("Message: " + message);

                    SocketAddress socketAddress = packet.getSocketAddress();
                    if (socketAddress instanceof InetSocketAddress) {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                        String sourceIP = inetSocketAddress.getAddress().getHostAddress();
                        int sourcePort = inetSocketAddress.getPort();
                        System.out.println("Received packet from " + sourceIP + ":" + sourcePort);
                    }
                }
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static void forwardPacket(String data) {
        // Implement forwarding logic based on the virtual destination MAC address
        // For now, let's assume flooding to all connected devices
        for (Device device : connectedDevices) {
            device.receivePacket(data);
        }
    }

    public void addConnectedDevice(Device device) {
        connectedDevices.add(device);
    }

    public static void main(String[] args) {
        Switch s1 = new Switch(args[0]);
        Long myPort = s1.getPort();
        System.out.println("Switch port currently" + myPort);
        s1.run();
    }
}
