import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
         this.port = Parser.getDevicePort(jsonData, name);
        connectedDevices = Parser.getNeighbors(jsonData, this.name);
    }

    public void run() {
        try{
            Sender sender = new Sender(name);
            Thread senderThread = new Thread(sender);
            senderThread.start();
        }catch (RuntimeException e) {
            e.printStackTrace();
        }
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


    class Sender implements Runnable {
        private final String senderName;

        Sender(String senderName) {
            this.senderName = senderName;

        }

        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    System.out.println("Enter Destination PC");
                    String pcName = reader.readLine();

//                    String destIp = Parser.getDeviceIp(jsonData, pcName);
//                    Long destPort = Parser.getDevicePort(jsonData, pcName);

                    String switchIp = Parser.getDeviceIp(jsonData, senderName);
                    Long switchPort = Parser.getDevicePort(jsonData, senderName);

                    System.out.print("Enter message: ");
                    String message = reader.readLine();

                    Frame frame = new Frame(senderName, message, pcName);

                    constructUDPacket(switchIp, switchPort, frame.serialize());

//ignore the commented lines

//                    Frame frame = new Frame(senderName, pcName, message);
//                    constructUDPacket(destIp, destPort, frame.serialize());
//
//                    System.out.println("Sending frame:");
//                    System.out.println("Source MAC: " + senderName);
//                    System.out.println("Destination MAC: " + pcName);
//                    System.out.println("Message: " + message);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //TODO: 1 threads for receiving one for taking input and build UDP packet

    public void constructUDPacket(String destinationIP, Long destinationPort, String payload) {
        try {
            byte[] sendData = payload.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destinationIP), Math.toIntExact(destinationPort));
            socket.send(packet);
            System.out.println("Packet sent: " + new String(packet.getData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void receivePacket(String data) {
        try {
            Frame receivedFrame = Frame.deserialize(data);

            String srcMac = receivedFrame.getSrcMac();
            String destMac = receivedFrame.getDestMac();
            String message = receivedFrame.getMessage();
            if (destMac.equals(name)) {
                // If the frame is intended for this device, print the message
                System.out.println("Received message: " + message);
            } else {
                // Otherwise, ignore the frame
                System.out.println("Ignoring frame, not intended for this device");
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during frame deserialization or processing
            e.printStackTrace();
        }
    }

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
}
