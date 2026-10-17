package com.kizan.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

final class DiscordReporter {
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    private DiscordReporter() {}

    static void send(String webhookUrl, String player, int x, int y, int z, String dimension) {
        // Only runs when the local user explicitly enables the feature in Kizan's menu.
        // Never log the webhook URL; treat it like a password.
        if (webhookUrl == null || !webhookUrl.startsWith("https://discord.com/api/webhooks/")) {
            return;
        }

        String message = "Kizan Client coordinates\\n"
                + "Player: " + safe(player) + "\\n"
                + "XYZ: " + x + ", " + y + ", " + z + "\\n"
                + "Dimension: " + safe(dimension);
        String body = "{\"content\":\"" + jsonEscape(message) + "\"}";

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(webhookUrl))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();
        } catch (IllegalArgumentException ignored) {
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                HTTP.send(request, HttpResponse.BodyHandlers.discarding());
            } catch (Exception ignored) {
                // Keep network errors out of the game loop.
            }
        }, "Kizan-Discord-Reporter");
        thread.setDaemon(true);
        thread.start();
    }

    private static String safe(String value) {
        return value == null ? "" : value.replace("\\n", " ").replace("\\r", " ");
    }

    private static String jsonEscape(String value) {
        return value.replace("\\\\", "\\\\\\\\")
                .replace("\"", "\\\\\"")
                .replace("\\n", "\\\\n")
                .replace("\\r", "\\\\r")
                .replace("\\t", "\\\\t");
    }
}
