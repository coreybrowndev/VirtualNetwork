import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Switch extends Device{

    Map<String, String> forwardingTable;
    List<Device> connectedDevices;

    public Switch(String name, int port, String ip) {
        super(name, ip, port);

        this.forwardingTable = new HashMap<>();
        this.connectedDevices = new ArrayList<>();
    }

    //TODO get sender's UDP Pocket
    public void receiveFrame(Frame frame) {
        String sourceMac = frame.getSrcMac();
        if (!forwardingTable.containsKey(sourceMac)) {
            forwardingTable.put(sourceMac, "ip + port of the device");
        }
    }

    public void forwardFrame(Frame frame) {

    }

    public void learningAlgorithm(Frame frame) {
        if (forwardingTable.containsKey(frame.getDestMac())) {
            String destinationPort = forwardingTable.get(frame.getDestMac());
            System.out.println("Forwarding frame to port " + destinationPort);
        } else {
            for(Device device : connectedDevices){
                if(!frame.getSrcMac().equals(device.name)){
                    forwardFrame(frame);
                }
            }
        }
    }

    public void connect(Device device) {
        connectedDevices.add(device);
        System.out.println(name + " connected to " + device.name);
    }
}
