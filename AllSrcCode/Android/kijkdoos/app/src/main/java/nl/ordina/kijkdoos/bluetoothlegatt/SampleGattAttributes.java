/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.ordina.kijkdoos.bluetoothlegatt;
import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public static String KIJKDOOS_CHARACTERISTIC_CONFIG  = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String KIJKDOOS_CONNECT_SERVICE_CONFIG  = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String KIJKDOOS_SERVICE_1800 = "00001800-0000-1000-8000-00805f9b34fb";
    public static String KIJKDOOS_SERVICE_1801 = "00001801-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        // Kijkdoos Characteristics.
        attributes.put(KIJKDOOS_CONNECT_SERVICE_CONFIG, "Kijkdoos connect service");
        // Kijkdoos Services.
        attributes.put(KIJKDOOS_SERVICE_1800, "Kijkdoos service 1800");
        attributes.put(KIJKDOOS_SERVICE_1801, "Kijkdoos service 1801");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
