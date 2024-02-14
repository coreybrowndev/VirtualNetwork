import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualPC extends Device implements Runnable {

    private String virtualMACAddress;
    private Switch ethernetSwitch;
    private ExecutorService executor;

    public VirtualPC(String name, String ip, int port, Switch ethernetSwitch) {
        super(name, ip, port);
        virtualMACAddress = name;
        this.ethernetSwitch = ethernetSwitch;
//        this.executor = Executors.newSingleThreadExecutor(); // Initialize executor service
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
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
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(portNumber));
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startUserInputListener() {
        Future<String> userInputFuture = executor.submit(new UserMessage());
        try {
            String userInput = userInputFuture.get();
            System.out.println("User input: " + userInput);
            // Process user input as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    private class UserMessage implements Callable<String> {
        @Override
        public String call() throws Exception {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter your message:");
            return scanner.nextLine();
        }
    }

//    public static void main(String[] args) {
//        Switch ethernetSwitch = new Switch();
//        VirtualPC pc = new VirtualPC("127.0.0.1", "5000", "A", ethernetSwitch);
//        Thread pcThread = new Thread(pc);
//        pcThread.start();
//
//
//        // Wait for user input
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            pc.startUserInputListener();
//        }
//    }
}



