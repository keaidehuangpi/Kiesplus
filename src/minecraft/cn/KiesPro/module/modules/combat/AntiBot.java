package cn.KiesPro.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class AntiBot
        extends Module {
    public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[]) botmode.values(), (Enum) botmode.Hypixel);
    public final Set<EntityPlayer> bots = new HashSet();

    public AntiBot() {
        super("AntiBot", new String[]{"nobot", "botkiller"}, ModuleType.Combat);
        this.addValues(this.mode);
    }

    @EventTarget
    private void onUpdate(MotionUpdateEvent event) {
        this.setSuffix(this.mode.getValue());
        switch (this.mode.getValue().toString()) {
            case "Hypixel": {
                if (!event.isPre()) break;
                final List playerEntities = mc.theWorld.playerEntities;
                for (int playerEntitiesSize = playerEntities.size(), i = 0; i < playerEntitiesSize; ++i) {
                    final EntityPlayer player = (EntityPlayer) playerEntities.get(i);
                    if (!player.getName().contains("��c")) { //!player.getName().startsWith("��") || 
                        if (!this.isEntityBot(player)) {
                            continue;
                        }
                        if (player.getDisplayName().getFormattedText().contains("NPC")) {
                            continue;
                        }
                    }
                    mc.theWorld.removeEntity(player);
                }
                break;
            }
            case "Mineplex": {
                for (Entity e : mc.theWorld.getLoadedEntityList()) {
                    if (!(e instanceof EntityPlayer)) continue;
                    EntityPlayer bot = (EntityPlayer) e;
                    if (e.ticksExisted >= 2 || !(bot.getHealth() < 20.0f) || !(bot.getHealth() > 0.0f) || e == mc.thePlayer)
                        continue;
                    mc.theWorld.removeEntity(e);
                }
            }
        }
    }

    public static boolean isEntityBot(Entity entity) {
        double distance = entity.getDistanceSqToEntity((Entity) Minecraft.thePlayer);
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (mc.getCurrentServerData() == null) {
            return false;
        }
        return mc.getCurrentServerData().serverIP.toLowerCase().contains((CharSequence) "hypixel") && entity.getDisplayName().getFormattedText().startsWith("\u0e22\u0e07") || !isOnTab(entity) && mc.thePlayer.ticksExisted > 100;
    }

    private static boolean isOnTab(Entity entity) {
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (!info.getGameProfile().getName().equals((Object) entity.getName())) continue;
            return true;
        }
        return false;
    }

    enum botmode {
        Hypixel,
        Mineplex;
    }
}

