import java.util.Objects;

public class Device {
    protected String name;
    protected String ip;
    protected int port;

    public Device(String name, String ip, Integer port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String setName(String newName) {
        return name = newName;
    }

    public String setIp(String newIp) {
        return ip = newIp;
    }

    public Integer setPort(Integer newPort) {
        return port = newPort;
    }

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

}
