import org.json.simple.JSONObject;

import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;

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
         port = Parser.getDevicePort(jsonData, name);
        connectedDevices = Parser.getNeighbors(jsonData, this.name);
    }

    protected String getName() {
        return this.name;
    }

    // Getter method for the ip property
    public String getIp() {
        return ip;
    }

    // Getter method for the port property
    public Long getPort() {
        return port;
    }

    // Getter method for the connectedDevices property
    public List<Device> getConnectedDevices() {
        return connectedDevices;
    }

    //TODO: receive and send udp packet


    //TODO: UDP datagram packet (srcIP, srcPort, frame would be the payload)
    //TODO: 1 threads for receiving one for taking input and build UDP packet

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
    public void sendMessage(Frame frame) {
        System.out.println("Received frame: " + frame.toString());
    }


}
