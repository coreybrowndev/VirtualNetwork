import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {


    public static JSONObject parseJSONFile(String filename) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(filename);
            return (JSONObject) parser.parse(reader);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Device getDeviceInfo(JSONArray devices, JSONArray switches, String deviceName) {
        for (Object obj : devices) {
            JSONObject deviceObj = (JSONObject) obj;
            if (deviceObj.get("name").equals(deviceName)) {
                return new Device((String) deviceObj.get("name"), (String) deviceObj.get("ip"), Math.toIntExact((Long) deviceObj.get("port")));
            }
        }

        for (Object obj : switches) {
            JSONObject switchObj = (JSONObject) obj;
            if (switchObj.get("name").equals(deviceName)) {
                return new Device((String) switchObj.get("name"), (String) switchObj.get("ip"),
                        Math.toIntExact((Long) switchObj.get("port")));
            }
        }
        return null;
    }

    public static List<Device> getNeighbors(JSONObject data, String deviceName) {
        JSONArray links = (JSONArray) data.get("links");
        JSONArray devices = (JSONArray) data.get("devices");
        JSONArray switches = (JSONArray) data.get("switches");
        JSONArray neighbors = new JSONArray();

        for (Object obj : links) {
            JSONObject linkObj = (JSONObject) obj;
            String node1 = (String) linkObj.get("node1");
            String node2 = (String) linkObj.get("node2");

            if (node1.equals(deviceName)) {
                neighbors.add(getDeviceInfo(devices, switches, node2));
            } else if (node2.equals(deviceName)) {
                neighbors.add(getDeviceInfo(devices, switches, node1));
            }
        }
        return neighbors;
    }
    public static void main(String[] args) {
        JSONObject jsonData = parseJSONFile("src/NetworkConfig.json");
        if(jsonData != null) {
            String deviceName = "s1";
            List<Device> neighbors = getNeighbors(jsonData, deviceName);

            System.out.println("Updated JSON File");
        }else {
            System.out.println("Failed to parse file");
        }
    }
}
