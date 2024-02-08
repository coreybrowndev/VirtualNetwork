class Frame {
    String srcMac;
    String destMac;
    String message;

    public Frame(String srcMac, String message, String destMac) {
        this.srcMac = srcMac;
        this.message = message;
        this.destMac = destMac;
    }
}
