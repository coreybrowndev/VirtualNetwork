import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Frame {
    String srcMac;
    int inPort;
    String srcIp;
    String destMac;

    public Frame(String srcMac, int inPort, String srcIp, String destMac) {
        this.srcMac = srcMac;
        this.inPort = inPort;
        this.srcIp = srcIp;
        this.destMac = destMac;
    }
}

class Switch {
    String name;
    int port;
    Map<String, Map<String, Object>> forwardingTable;

    public Switch(String name, int port) {
        this.name = name;
        this.port = port;
        this.forwardingTable = new HashMap<>();
    }

    public void receiveFrame(Frame frame) {
        String sourceMac = frame.srcMac;
        if (forwardingTable.containsKey(sourceMac)) {
            Map<String, Object> entry = forwardingTable.get(sourceMac);
            entry.put("port", frame.inPort);
            entry.put("ip", frame.srcIp);
        } else {
            Map<String, Object> entry = new HashMap<>();
            entry.put("port", frame.inPort);
            entry.put("ip", frame.srcIp);
            forwardingTable.put(sourceMac, entry);
        }
    }

    public void forwardFrame(Frame frame, String destMac) {
        if (forwardingTable.containsKey(destMac)) {
            int port = (int) forwardingTable.get(destMac).get("port");
            System.out.println("Forwarding frame to port " + port);
        } else {
            System.out.println("Destination MAC address not found in forwarding table. Flooding the frame.");
        }
    }
}
