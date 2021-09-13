package me.mattdokn.chatcleaner;

import me.mattdokn.chatcleaner.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class ChatCleaner implements ClientModInitializer {
    public static ArrayList<Pattern> regexes = new ArrayList<>();
    public static Pattern ignoreRegex;
    @Override
    public void onInitializeClient() {
        ModConfig.init();
    }
}
