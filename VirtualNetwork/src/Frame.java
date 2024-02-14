class Frame {
    private String srcMac;
    private String destMac;
    private String message;

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
