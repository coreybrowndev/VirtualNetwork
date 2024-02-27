import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualPC extends Device implements Runnable {

    public VirtualPC(String name) {
        super(name);
    }

    private Long setPort() {return this.getPort();}

    public void run() {
        try {

            Sender sender = new Sender(name);
            Thread senderThread = new Thread(sender);
            senderThread.start();
            senderThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String pcName = args[0];
        Long port = Parser.getDevicePort(Parser.parseJSONFile("src/NetworkConfig.json"), pcName);

//        VirtualPC pcA = new VirtualPC(pcName);
//        Thread pcAThread = new Thread(pcA);
//        pcAThread.start();
//        System.out.println(pcA.getPort());

        VirtualPC pcB = new VirtualPC(pcName);
        Thread pcBThread = new Thread(pcB);
        System.out.println(pcB.getPort());
        pcBThread.start();

    }
}



