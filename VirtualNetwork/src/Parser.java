import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
//    public static String collectJsonAsString(String filename) {
//        //take the device and find the device in the config file, use the algorithm to build the relationship and return a new json file with the relationship built for the device in question
//                        //first parse links to get the neighors from "s1" for example, then parse the devices to get the IP and port for each neighbor
//        //open file
//        String jsonText = "";
//
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
//
//            String line;
//            while((line = bufferedReader.readLine()) != null) {
//                jsonText += line + "\n";
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonText;
//    }

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

    public static void updateDeviceConnections(JSONObject data) {
        JSONArray switches = (JSONArray) data.get("switches");
        JSONArray devices = (JSONArray) data.get("devices");
        JSONArray links = (JSONArray) data.get("links");

        Map<String, Integer> switchPortMap = new HashMap<>();
        for(Object obj : switches) {
            JSONObject switchObj = (JSONObject) obj;
            switchPortMap.put((String) switchObj.get("name"), Math.toIntExact((Long) switchObj.get("port")));
        }

        Map<String, String> deviceIPMap = new HashMap<>();
        for(Object obj : devices) {
            JSONObject deviceObj = (JSONObject) obj;
            deviceIPMap.put((String) deviceObj.get("name"), (String) deviceObj.get("ip"));
        }

        Map<String, String> switchDeviceMap = new HashMap<>();
        for (Object obj : links) {
            JSONObject linkObj = (JSONObject) obj;
            String switchName = (String) linkObj.get("node1");
            String deviceName = (String) linkObj.get("node2");
            switchDeviceMap.put(switchName, deviceName);
        }

        JSONArray updatedDevices = new JSONArray();
        for(Object obj : devices) {
            JSONObject deviceObj = (JSONObject) obj;
            String deviceName = (String) deviceObj.get("name");
            String deviceIP = deviceIPMap.get(deviceName);
            String connectedSwitch = null;
            Integer port = null;
            for(Object link : links) {
                JSONObject linkObj = (JSONObject) link;
                if(deviceName.equals(linkObj.get("node2"))){
                    connectedSwitch = (String) linkObj.get("node1");
                    port = switchPortMap.get(connectedSwitch);
                    break;
                }
            }
            JSONObject updatedDevice = new JSONObject();
            updatedDevice.put("name", deviceName);
            updatedDevice.put("ip", deviceIP);
            updatedDevice.put("connectedDevice", connectedSwitch);
            updatedDevice.put("port", port);
            updatedDevices.add(updatedDevice);
        }
        data.put("devices", updatedDevices);
    }

    public static void writeJsonToFile(JSONObject data, String filename) {
        try(FileWriter file = new FileWriter(filename)) {
            file.write(data.toJSONString());
            file.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JSONObject jsonData = parseJSONFile("src/NetworkConfig.json");
        if(jsonData != null) {
            updateDeviceConnections(jsonData);
            writeJsonToFile(jsonData, "UpdatedNetworkConfig.json");
            System.out.println("Updated JSON File");
        }else {
            System.out.println("Failed to parse file");
        }
    }
}
