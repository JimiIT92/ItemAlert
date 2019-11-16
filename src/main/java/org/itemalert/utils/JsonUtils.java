package org.itemalert.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JSON Utility class
 *
 * @author JimiIT92
 */
public class JsonUtils {

    /**
     * Parse a Json String to a Class
     *
     * @param json  Json string
     * @param clazz Class instance
     * @param <T>   Class type
     * @return Parsed Json
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    /**
     * Serialize an HashMap to a JSON string
     *
     * @param map HashMap
     * @return JSON string
     */
    static <K, V> String serializeMap(HashMap map) {
        return new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(map, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    /**
     * Save a JSON string to a File
     *
     * @param json      JSON string
     * @param directory Directory
     * @param file      File
     */
    static void saveToFile(String json, File directory, File file) {
        FileWriter fileWriter = null;
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(json);
        } catch (IOException ex) {
            LogUtils.getLogger().error(Arrays.toString(ex.getStackTrace()));
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    LogUtils.getLogger().error(Arrays.toString(ex.getStackTrace()));
                }
            }
        }
    }

    /**
     * Load a JSON string from a File
     *
     * @param file File
     * @return JSON string
     */
    static String loadFromFile(File file) {
        if (!file.exists()) {
            return null;
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            return bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException ex) {
            LogUtils.getLogger().error(Arrays.toString(ex.getStackTrace()));
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    LogUtils.getLogger().error(Arrays.toString(ex.getStackTrace()));
                }
            }
        }
    }
}
