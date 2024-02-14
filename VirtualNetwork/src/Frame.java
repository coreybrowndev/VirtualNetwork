class Frame {
    public String srcMac;
    public String destMac;
    public String message;
    String srcIp;
    int inPort;

    public Frame(String srcMac, String message, String destMac) {
        this.srcMac = srcMac;
        this.message = message;
        this.destMac = destMac;
    }

    public String getSrcMac() {
        return this.srcMac;
    }

    public String getDestMac() {
        return this.destMac;
    }

    public String getMessage() {
        return this.message;
    }

}
