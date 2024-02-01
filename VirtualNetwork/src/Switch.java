import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch {
    String name;
    int port;
    Map<String, Map<String, Object>> forwardingTable;
    List<Device> connectedDevices;


    public Switch(String name, int port) {
        this.name = name;
        this.port = port;
        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
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

    public void connect(Device device) {
        connectedDevices.add(device);
        System.out.println(device.getName() + " connected to " + name);
    }
}
