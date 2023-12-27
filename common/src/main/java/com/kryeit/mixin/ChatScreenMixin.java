package com.kryeit.mixin;

import com.kryeit.client.screen.MissionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow
    protected EditBox input;
    @Inject(method = "keyPressed", at = @At("RETURN"))
    private void runServer(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if (i == 257 && input != null && input.getValue().equals("/missions")) {
            Minecraft.getInstance().setScreen(new MissionScreen());
        }
    }
}
