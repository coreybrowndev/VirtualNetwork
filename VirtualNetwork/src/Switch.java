import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch {
    String name;
    int port;
    Map<String, String> forwardingTable;
    List<Device> connectedDevices;


    public Switch(String name, int port) {
        this.name = name;
        this.port = port;
        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
    }

    public void receiveFrame(Frame frame) {
        String sourceMac = frame.srcMac;
        String senderInfo = frame.srcIp + ":" + frame.inPort;
        if (!forwardingTable.containsKey(sourceMac)) {
            forwardingTable.put(sourceMac, senderInfo);
        }
    }


    public void forwardFrame(Frame frame) {
        if (forwardingTable.containsKey(frame.destMac)) {
            String destinationInfo = forwardingTable.get(frame.destMac);
            System.out.println("Forwarding frame to " + destinationInfo);
        } else {
            System.out.println("Destination MAC address not found in forwarding table. Flooding the frame.");
            floodFrame(frame);
        }
    }
    private void floodFrame(Frame frame) {
        for (Device device : connectedDevices) {
            if (device.getPort() != frame.inPort) {
                System.out.println("Flooding frame to " + device.getName());
                // You may want to send the frame to these devices using a sendMessage() method if available
            }
        }
    }


    public void connect(Device device) {
        connectedDevices.add(device);
        System.out.println(device.getName() + " connected to " + name);
    }
}
