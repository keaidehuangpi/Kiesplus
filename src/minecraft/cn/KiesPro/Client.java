/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro;

import java.io.PrintStream;
import java.time.OffsetDateTime;
import java.util.List;

import org.lwjgl.opengl.Display;

import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.CommandManager;
import cn.KiesPro.management.FileManager;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.modules.render.UI.TabUI;
import cn.KiesPro.ui.login.AltManager;
import cn.KiesPro.ui.notification.NotificationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class Client {
    public final static String name = "KiesPro GC";
    public final static double version = 1.0;
    public static boolean publicMode = false;
    public static Client instance = new Client();
    private static ModuleManager modulemanager;
    private CommandManager commandmanager;
    private NotificationManager notificationManager = new NotificationManager();
    private AltManager altmanager;
    private FriendManager friendmanager;
    private TabUI tabui;
    public static ResourceLocation CLIENT_CAPE = new ResourceLocation("ETB/cape.png");
	public static float Yaw;
    public static float Pitch;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public void initiate() {
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        this.altmanager = new AltManager();
        AltManager.init();
        AltManager.setupAlts();
        FileManager.init();
        Display.setTitle("KiesPro GC by huiwow 1.0");
    }

    public static ModuleManager getModuleManager() {
        return modulemanager;
    }
    
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public AltManager getAltManager() {
        return this.altmanager;
    }

    public void shutDown() {
        String values = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
        String hide = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.wasRemoved()) continue;
            hide = String.valueOf(hide) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Hide.txt", hide, false);
    }

	public static void RenderRotate(final float yaw, final float pitch) {
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
        Client.Pitch = pitch;
    }

	public static void setRotation(float yaw, float pitch) {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.rotationYawHead = yaw;
        Minecraft.getMinecraft();
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        EntityPlayerSP.rotationPitchHead = pitch;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.renderYawOffset = pitch;
        Minecraft.getMinecraft();
        Minecraft.thePlayer.renderYawOffset = yaw;
    }
}

