package me.mattdokn.chatcleaner.mixins;

import me.mattdokn.chatcleaner.ChatCleaner;
import me.mattdokn.chatcleaner.config.ModConfig;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void addMessage(Text text, CallbackInfo info) {
        String json = text.toString();
        if (ModConfig.INSTANCE.ignoreRegex.length() > 0 && ChatCleaner.ignoreRegex.matcher(json).find()) {
            return;
        }
        for (Pattern p : ChatCleaner.regexes) {
            if (p.matcher(json).find()) {
                info.cancel();
            }
        }
    }
}
