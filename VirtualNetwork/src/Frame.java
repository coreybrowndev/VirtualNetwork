class Frame {
    public String srcMac;
    public String destMac;
    public String payload;
    int inPort;

    public Frame(String srcMac, String message, String destMac) {
        this.srcMac = srcMac;
        this.destMac = destMac;
    }

    public String getSrcMac() {
        return this.srcMac;
    }

    public String getDestMac() {
        return this.destMac;
    }
}
