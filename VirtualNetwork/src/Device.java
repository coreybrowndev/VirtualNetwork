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
}
