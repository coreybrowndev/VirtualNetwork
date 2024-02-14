import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch {
    String name;
    int port;

    String ip;
    Map<String, String> forwardingTable;
    List<Device> connectedDevices;


    public Switch(String name, int port, String ip) {
        this.name = name;
        this.port = port;
        this.ip = ip;
        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
    }

    //TODO get sender's UDP Pocket
    public void receiveFrame(Frame frame) {
        String sourceMac = frame.srcMac;
        if (!forwardingTable.containsKey(sourceMac)) {
            forwardingTable.put(sourceMac, "ip + port of the device");
        }
    }

    public void forwardFrame(Frame frame) {
        if (forwardingTable.containsKey(frame.destMac)) {
            String destinationPort = forwardingTable.get(frame.destMac);
            System.out.println("Forwarding frame to port " + destinationPort);
        } else {
            System.out.println("Destination MAC address not found in forwarding table. Flooding the frame.");
        }
    }

    public void connect(Device device) {
        connectedDevices.add(device);
        System.out.println(device.getName() + " connected to " + name);
    }
}
