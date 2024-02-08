import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class VirtualPC implements Runnable {
    private String ipAddress;
    private String portNumber;
    private String virtualMACAddress;
    private Switch ethernetSwitch;

    public VirtualPC(String ipAddress, String portNumber, String virtualMACAddress, Switch ethernetSwitch) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.virtualMACAddress = virtualMACAddress;
        this.ethernetSwitch = ethernetSwitch;
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(Integer.parseInt(portNumber));
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] frameElements = receivedData.split(",");
                String destinationMAC = frameElements[0];
                String message = frameElements[1];
                if (destinationMAC.equals(virtualMACAddress)) {
                    System.out.println("Received message: " + message);
                } else {
                    System.out.println("Ignoring frame, not intended for this PC");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String destinationMAC, String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            String frameData = destinationMAC + "," + message;
            byte[] sendData = frameData.getBytes();
            InetAddress address = InetAddress.getByName(ipAddress);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(portNumber));
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Switch ethernetSwitch = new Switch();
        VirtualPC pc = new VirtualPC("127.0.0.1", "5000", "A", ethernetSwitch);
        Thread pcThread = new Thread(pc);
        pcThread.start();


        // Wait for user input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("Enter destination MAC address: ");
                String destinationMAC = reader.readLine();
                System.out.print("Enter message: ");
                String message = reader.readLine();
                pc.sendMessage(destinationMAC, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


