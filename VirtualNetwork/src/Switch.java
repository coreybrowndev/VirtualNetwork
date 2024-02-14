import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Switch extends Device{

    Map<String, String> forwardingTable;

    public Switch(String name) {
        super(name);
        this.forwardingTable = new HashMap<>();
    }

    //TODO get sender's UDP Packet
//    public void receiveFrame(Frame frame) {
//        String sourceMac = frame.getSrcMac();
//        if (!forwardingTable.containsKey(sourceMac)) {
//            forwardingTable.put(sourceMac, "ip + port of the UDP");
//        }
//        sendUDP();
//    }

    public void forwardFrame(Frame frame) {

    }

    public void learningAlgorithm(Frame frame) {
        if (forwardingTable.containsKey(frame.getDestMac())) {
            String destinationPort = forwardingTable.get(frame.getDestMac());
            System.out.println("Forwarding frame to port " + destinationPort);
        } else {
            for(Device device : connectedDevices){
                if(! "UDP packet: srcIP + port".equals(device.ip + device.port)){
                    forwardFrame(frame);
                }
            }
        }
    }

    public static void main(String[] args) {
       Switch S1 = new Switch(args[1]);
    }
}


