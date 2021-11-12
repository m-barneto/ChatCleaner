package me.mattdokn.chatcleaner.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.mattdokn.chatcleaner.ChatCleaner;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ChatCleanerConfig implements ModMenuApi {
    public static ArrayList<String> defaultRegexes = new ArrayList<>(
            Arrays.asList(
                    "(\\/chestsort)",
                    "(For the list of commands)",
                    "(\\/commands)",
                    "(Enable\\/Disable your auto tree)",
                    "(\\/tree toggle)",
                    "(You can toggle editing)",
                    "(\\/signedit)",
                    "(You can disable notifications from)",
                    "(\\/cstoggle)",
                    "(Want to know about world borders?)",
                    "(\\/info)"
            ));
    private static ConfigBuilder builder() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new TranslatableText("category.chatcleaner"))
                .setEditable(true)
                .setSavingRunnable(ModConfig::writeJson);

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("chatcleaner.config.hideregex"));
        general.addEntry(builder.entryBuilder()
                .startStrField(new TranslatableText("chatcleaner.config.ignoreregex"), ModConfig.INSTANCE.ignoreRegex)
                .setDefaultValue("(\\u27A6)")
                .setSaveConsumer(val -> {
                    ModConfig.INSTANCE.ignoreRegex = val;
                    updateIgnoreRegex();
                })
                .build());

        general.addEntry(builder.entryBuilder()
                .startStrList(new TranslatableText("chatcleaner.config.regexes"), ModConfig.INSTANCE.regexes)
                .setDefaultValue(defaultRegexes)
                .setSaveConsumer(val -> {
                    ModConfig.INSTANCE.regexes = (ArrayList<String>) val;
                    updateRegexes();
                })
                .build());

        return builder;
    }
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> builder().setParentScreen(parent).build();
    }

    public static void updateIgnoreRegex() {
        try {
            ChatCleaner.ignoreRegex = Pattern.compile(ModConfig.INSTANCE.ignoreRegex);
        } catch (PatternSyntaxException ex) {
            System.err.println("Invalid chat message ignore regex pattern: " + ex);
        }
    }

    public static void updateRegexes() {
        String lastRegex = "";
        try {
            ChatCleaner.regexes.clear();
            for (String regex : ModConfig.INSTANCE.regexes) {
                lastRegex = regex;
                ChatCleaner.regexes.add(Pattern.compile(regex));
            }
        } catch (PatternSyntaxException ex) {
            System.err.println("Invalid regex pattern: " + lastRegex + "\nError:" + ex);
        }
    }
}
