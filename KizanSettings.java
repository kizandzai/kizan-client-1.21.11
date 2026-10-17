package com.kizan.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

public final class KizanSettings {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("kizanclient.properties");

    public static boolean hudEnabled = true;
    public static boolean discordEnabled = false;
    public static boolean playerScannerEnabled = false;
    public static boolean autoTotemEnabled = false;
    public static boolean autoSprintEnabled = false;
    public static String webhookUrl = "";

    private KizanSettings() {}

    public static void load() {
        Properties p = new Properties();
        if (Files.exists(FILE)) {
            try (var reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
                p.load(reader);
                hudEnabled = Boolean.parseBoolean(p.getProperty("hudEnabled", "true"));
                discordEnabled = Boolean.parseBoolean(p.getProperty("discordEnabled", "false"));
                playerScannerEnabled = Boolean.parseBoolean(p.getProperty("playerScannerEnabled", "false"));
                autoTotemEnabled = Boolean.parseBoolean(p.getProperty("autoTotemEnabled", "false"));
                autoSprintEnabled = Boolean.parseBoolean(p.getProperty("autoSprintEnabled", "false"));
                webhookUrl = p.getProperty("webhookUrl", "");
            } catch (IOException ignored) {}
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hudEnabled", Boolean.toString(hudEnabled));
        p.setProperty("discordEnabled", Boolean.toString(discordEnabled));
        p.setProperty("playerScannerEnabled", Boolean.toString(playerScannerEnabled));
        p.setProperty("autoTotemEnabled", Boolean.toString(autoTotemEnabled));
        p.setProperty("autoSprintEnabled", Boolean.toString(autoSprintEnabled));
        p.setProperty("webhookUrl", webhookUrl);
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
                p.store(writer, "Kizan Client settings - keep your Discord webhook private");
            }
        } catch (IOException ignored) {}
    }
}
