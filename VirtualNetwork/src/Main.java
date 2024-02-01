// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        //Filler info
        Device deviceA = new Device("A", "192.168.1.2");
        Device deviceB = new Device("B", "192.168.1.4");
        Device deviceC = new Device("C", "192.168.1.5");
        Device deviceD = new Device("D", "192.168.1.3");

        Switch switch1 = new Switch("s1", 3000);
        Switch switch2 = new Switch("s2", 3001);

        switch1.connect(deviceA);
        switch1.connect(deviceB);

        switch2.connect(deviceC);
        switch2.connect(deviceD);
    }
}