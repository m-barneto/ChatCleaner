package me.mattdokn.chatcleaner.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import me.mattdokn.chatcleaner.ChatCleaner;

// Stolen from https://github.com/zabi94/NBTTooltip/blob/7c3e38adbf8d5b2e018202de6a6c50f37ddca3e2/src/main/java/zabi/minecraft/nbttooltip/config/ModConfig.java
public class ModConfig {
    private static final File folder = new File("config");
    private static File configFile;
    private static final Gson config = new GsonBuilder().setPrettyPrinting().create();
    public static ConfigInstance INSTANCE;

    public static void init() {
        loadDefaults();
        generateFoldersAndFiles();
        readJson();
        writeJson(); //Write to file new config options
    }

    public static void loadDefaults() {
        INSTANCE = new ConfigInstance();
        INSTANCE.ignoreRegex = "(\\u27A6)";
        INSTANCE.regexes = ChatCleanerConfig.defaultRegexes;
    }

    private static void generateFoldersAndFiles() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (folder.isDirectory()) {
            configFile = new File(folder, "chatcleaner.json");

            if (!configFile.exists()) {
                System.out.println("Creating new config file");
                try {
                    configFile.createNewFile();
                    loadDefaults();
                    String json = config.toJson(INSTANCE);
                    FileWriter writer = new FileWriter(configFile);
                    writer.write(json);
                    writer.close();
                } catch (IOException e) {
                    throw new IllegalStateException("Can't create config file", e);
                }
            } else if (configFile.isDirectory()) {
                throw new IllegalStateException("'chatcleaner.json' must be a file!");
            }
        } else {
            throw new IllegalStateException("'config' must be a folder!");
        }
    }

    public static void readJson() {
        try {
            INSTANCE = config.fromJson(new FileReader(configFile), ConfigInstance.class);
            if (INSTANCE == null) {
                throw new IllegalStateException("Null configuration");
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid configuration!");
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // No op
        }
    }

    public static void writeJson() {
        ChatCleanerConfig.updateIgnoreRegex();
        ChatCleanerConfig.updateRegexes();

        try {
            String json = config.toJson(INSTANCE);
            FileWriter writer = new FileWriter(configFile, false);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Can't update config file", e);
        }
    }

}