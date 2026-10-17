package com.kizan.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class KizanScreen extends Screen {
    private EditBox webhookBox;
    private int top;

    public KizanScreen() {
        super(Component.literal("Kizan Client"));
        KizanSettings.load();
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        top = Math.max(34, this.height / 2 - 112);
        int w = 230;
        int x = center - w / 2;
        int y = top + 28;

        addRenderableWidget(toggle("HUD", () -> KizanSettings.hudEnabled,
                v -> KizanSettings.hudEnabled = v, x, y, w));
        y += 24;
        addRenderableWidget(toggle("Discord Coordinates (60s)", () -> KizanSettings.discordEnabled,
                v -> KizanSettings.discordEnabled = v, x, y, w));
        y += 24;
        addRenderableWidget(toggle("Player Scanner (visual list)", () -> KizanSettings.playerScannerEnabled,
                v -> KizanSettings.playerScannerEnabled = v, x, y, w));
        y += 24;
        addRenderableWidget(toggle("Auto Totem (prototype toggle)", () -> KizanSettings.autoTotemEnabled,
                v -> KizanSettings.autoTotemEnabled = v, x, y, w));
        y += 24;
        addRenderableWidget(toggle("Auto Sprint (prototype toggle)", () -> KizanSettings.autoSprintEnabled,
                v -> KizanSettings.autoSprintEnabled = v, x, y, w));

        y += 32;
        webhookBox = new EditBox(this.font, x, y, w, 20, Component.literal("Discord webhook URL"));
        webhookBox.setMaxLength(300);
        webhookBox.setValue(KizanSettings.webhookUrl);
        webhookBox.setHint(Component.literal("https://discord.com/api/webhooks/..."));
        addRenderableWidget(webhookBox);

        y += 26;
        addRenderableWidget(Button.builder(Component.literal("Save settings"), b -> {
            KizanSettings.webhookUrl = webhookBox.getValue().trim();
            KizanSettings.save();
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                        Component.literal("Kizan settings saved. Discord: " +
                                (KizanSettings.discordEnabled ? "ON" : "OFF")), true);
            }
        }).bounds(x, y, w, 20).build());

        y += 24;
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(x, y, w, 20).build());
    }

    private Button toggle(String label, BoolGetter getter, BoolSetter setter, int x, int y, int w) {
        Button[] ref = new Button[1];
        ref[0] = Button.builder(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")), b -> {
            setter.set(!getter.get());
            b.setMessage(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")));
            KizanSettings.save();
        }).bounds(x, y, w, 20).build();
        return ref[0];
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, "KIZAN CLIENT", this.width / 2, top + 8, 0x8B9DFF);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, "Right Shift to open/close", this.width / 2,
                Math.min(this.height - 12, top + 230), 0xAAAAAA);
    }

    @Override
    public void onClose() {
        KizanSettings.save();
        super.onClose();
    }

    @FunctionalInterface private interface BoolGetter { boolean get(); }
    @FunctionalInterface private interface BoolSetter { void set(boolean value); }
}
