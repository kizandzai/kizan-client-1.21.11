package com.kizan.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KizanClient implements ClientModInitializer {
    public static final String MOD_ID = "kizanclient";
    private static KeyMapping openMenuKey;
    private static long lastDiscordSend = 0L;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "main"));
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.kizanclient.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new KizanScreen());
                }
            }

            if (client.player != null && client.level != null
                    && KizanSettings.discordEnabled
                    && !KizanSettings.webhookUrl.isBlank()) {
                long now = System.currentTimeMillis();
                if (now - lastDiscordSend >= 60_000L) {
                    lastDiscordSend = now;
                    String username = client.player.getName().getString();
                    int x = (int) Math.floor(client.player.getX());
                    int y = (int) Math.floor(client.player.getY());
                    int z = (int) Math.floor(client.player.getZ());
                    String dimension = client.level.dimension().identifier().toString();
                    DiscordReporter.send(KizanSettings.webhookUrl, username, x, y, z, dimension);
                }
            }
        });

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(MOD_ID, "coordinates"),
                (graphics, deltaTracker) -> {
                    Minecraft client = Minecraft.getInstance();
                    if (KizanSettings.hudEnabled && client.player != null && client.level != null) {
                        String text = String.format("Kizan | XYZ: %d %d %d | FPS: %d",
                                (int)Math.floor(client.player.getX()),
                                (int)Math.floor(client.player.getY()),
                                (int)Math.floor(client.player.getZ()),
                                client.getFps());
                        graphics.drawString(client.font, text, 8, 8, 0xFFFFFF, true);
                    }
                }
        );
    }
}
