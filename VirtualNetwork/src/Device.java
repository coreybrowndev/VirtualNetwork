import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;
import java.net.DatagramPacket;


public class Device {
    protected String name;
    protected String ip;
    protected Long port;

    protected List<Device> connectedDevices;

    JSONObject jsonData = Parser.parseJSONFile("src/NetworkConfig.json");


    public Device(String name) {
        //Use Parser to get ip and port of the device
        this.name = name;
         ip = Parser.getDeviceIp(jsonData, name);
         port = Parser.getDevicePort(jsonData, name);
        connectedDevices = Parser.getNeighbors(jsonData, this.name);
    }

    public void run() {
        //Start the two threads for both receiving and sending to neighbor nodes
    }

    protected String getName() {
        return this.name;
    }


    public String getIp() {
        return ip;
    }

    public Long getPort() {
        return port;
    }

    public List<Device> getConnectedDevices() {
        return connectedDevices;
    }

    public void receivePacket() {
        try {
            DatagramSocket socket = new DatagramSocket(port.intValue());

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                socket.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received UDP packet: " + data);

                //if the name is the Same as the destination from the UDP, print message, else flood.



            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class Sender implements Runnable{

        private InetSocketAddress destination;

        private String message;

        public Sender(InetSocketAddress address, String message){
            this.destination = address;
            this.message = message;
        }

        @Override
        public void run() {

            DatagramSocket socket = null;

            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            DatagramPacket request = new DatagramPacket(message.getBytes(),
                    message.getBytes().length,
                    destination
            );

            try {
                socket.send(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            socket.close();
        }
    }
    static class Receiver implements Runnable {
        //grab packet, look at headers that contain the src IP
        private final Device device;

        Receiver(Device device) {
            this.device = device;
        }

        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(device.getPort().intValue());

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while(true) {
                    socket.receive(packet);
                    String data = new String(packet.getData(), 0, packet.getLength());
                    //extract the information data
                    Frame receivedFrame = Frame.deserialize(data);

                    //Figure out after this

                }
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //Prepare a UDP packet containing the virtual frame and send it to the connected switch.
    public void sendUDPPacket(String destinationIP, int destinationPort, String payload) {
        try {
            byte[] sendData = payload.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destinationIP), destinationPort);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //TODO: 1 threads for receiving one for taking input and build UDP packet



    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return port == device.port && Objects.equals(name, device.name) && Objects.equals(ip, device.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ip, port);
    }
    public void sendMessage(Frame frame) {
        System.out.println("Received frame: " + frame.toString());
    }


}
