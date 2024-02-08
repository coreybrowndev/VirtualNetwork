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
