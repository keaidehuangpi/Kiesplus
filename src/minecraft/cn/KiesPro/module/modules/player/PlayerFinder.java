/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventChat;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.AntiBot;
import cn.KiesPro.utils.Helper;

public class PlayerFinder
extends Module {
	List<String> strList = new ArrayList<String>();
	String[] bad = new String[] {strList.toArray().toString()};
	public static Option<Boolean> Warn = new Option("Warn hack", "warn hack", true);
    public PlayerFinder() {
        super("PlayerFinder", new String[]{"Playerfinder"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(Warn);
    }
    @Override
    public void onDisable() {
    	strList.clear();
    }
    @EventTarget
    public void onJoin(EventChat e) {
    	if(e.getMessage().contains("You won") || e.getMessage().contains("你赢了") || e.getMessage().contains("You died") || e.getMessage().contains("你死了")) {
    		strList.clear();
    	}
    	if(e.getMessage().contains("has joined") || e.getMessage().contains("加入了游戏") || e.getMessage().contains("游戏将在")) {
    		strList.clear();
    	}
    }
    private boolean isBot(Entity v1) {
        if (!v1.getDisplayName().getFormattedText().equalsIgnoreCase(v1.getName() + "\u00a7r")) return false;
        if (!v1.getDisplayName().getFormattedText().contains("NPC")) return false;
        return true;
    }
    @EventTarget
    public void onUpdate(EventPreUpdate event) {
    	AntiBot ab = new AntiBot();
    	for (Object o : mc.theWorld.loadedEntityList) {
			if (!(o instanceof EntityPlayer)) continue;
			if (o == mc.thePlayer) continue;
            EntityPlayer ent = (EntityPlayer)o;
            if (ent instanceof EntityPlayer) {
                if (ent == mc.thePlayer) continue;
                if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && (ent.getCurrentEquippedItem() != null)) {
                	Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                	strList.add(ent.getName());
                }
                if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && (ent.onGround == true)) {
                	Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                	strList.add(ent.getName());
                }
                if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && (ent.getCurrentEquippedItem() == null)) {
                	Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                	strList.add(ent.getName());
                }
                if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && (ent.getCurrentEquippedItem() != null) && ((ent.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
                	Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                	strList.add(ent.getName());
                }
                //Check 距离
                if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && (mc.thePlayer.getDistanceSqToEntity(ent) < 5.0)) {
                	Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                	strList.add(ent.getName());
                }
                if(Warn.getValue().booleanValue()) {
                	if(ent == mc.thePlayer || ab.isEntityBot(ent) && !strList.contains(ent.getName()) && ent.isBlocking()) {
                		Helper.sendMessage("Name: §7" + ent.getName() + " §cHP: §a" + ent.getHealth() + " §cX:§7" + MathHelper.floor_double(ent.posX) + " §cY:§7" + MathHelper.floor_double(ent.posY) + " §cZ:§7" + MathHelper.floor_double(ent.posZ));
                		Helper.sendMessage("Found a hacker and reported automatically");
                		mc.thePlayer.sendChatMessage("/wdr " + ent.getName() + " speed ka fly reach jesus scaffold timer");
                		strList.add(ent.getName());
                	}
                }
            }
    	}
    }
}

