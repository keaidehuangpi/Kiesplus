/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;
import java.util.Random;

import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class FakeName
extends Module {
	public static String customname = "SomeBody";
	public static Mode mode = new Mode("Mode", "mode", (Enum[])NameMode.values(), (Enum)NameMode.Hypixel);
	public static Mode rankmode = new Mode("RankMode", "RankMode", (Enum[])RankMode.values(), (Enum)RankMode.MVP);
	public static Option<Boolean> rank = new Option<Boolean>("Rank", "Rank", true);
	public static Option<Boolean> Tab = new Option<Boolean>("Tab", "Tab", true);
    public FakeName() {
        super("FakeName", new String[]{""}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(mode, rankmode, rank, Tab);
    }
    
    public String getFakeName() {
    	String curRankString = "";
    	if(ModuleManager.getModuleByName("FakeName").isEnabled()) {
    	if(mode.getValue() == NameMode.Hypixel) {
    	switch (rankmode.getModeAsString()) {
    		case "VIP":
    		curRankString = EnumChatFormatting.GREEN + "[VIP] ";
    		break;
    		case "VIPPlus":
    		curRankString = EnumChatFormatting.GREEN + "[VIP" + EnumChatFormatting.GOLD + "+" + EnumChatFormatting.GREEN + "] ";
    		break;
    		case "MVP":
    		curRankString = EnumChatFormatting.AQUA + "[MVP] ";
    		break;
    		case "MVPPlus":
    		curRankString = EnumChatFormatting.AQUA + "[MVP" + EnumChatFormatting.RED + "+" + EnumChatFormatting.AQUA + "] ";
    		break;
    		case "MVPPlusPlus":
    		curRankString = EnumChatFormatting.GOLD + "[MVP" + EnumChatFormatting.RED + "++" + EnumChatFormatting.GOLD + "] ";
    		break;
    		case "Helper":
    		curRankString = EnumChatFormatting.BLUE + "[Helper] ";
    		break;
    		case "Mod":
    		curRankString = EnumChatFormatting.DARK_GREEN + "[Mod] ";
    		break;
    		case "Youtuber":
    		curRankString = EnumChatFormatting.RED + "[" + EnumChatFormatting.WHITE + "YOUTUBE" + EnumChatFormatting.RED + "] " + EnumChatFormatting.RED;
    		break;
    		case "Admin":
    		curRankString = EnumChatFormatting.RED + "[ADMIN] " + EnumChatFormatting.RED;
    		break;
    	}
    	if(rank.getValue().booleanValue()) {
    		return curRankString + mc.thePlayer.getName();
    	}else {
    		return EnumChatFormatting.BLUE + "You";
    	}
    	}else {
    		return EnumChatFormatting.BLUE + customname;
    	}
    	}else {
        	if(mode.getValue() == NameMode.Hypixel) {
            	switch (rankmode.getModeAsString()) {
            		case "VIP":
            		curRankString = EnumChatFormatting.GREEN + "";
            		break;
            		case "VIPPlus":
            		curRankString = EnumChatFormatting.GREEN + "";
            		break;
            		case "MVP":
            		curRankString = EnumChatFormatting.AQUA + "";
            		break;
            		case "MVPPlus":
            		curRankString = EnumChatFormatting.AQUA + "";
            		break;
            		case "MVPPlusPlus":
            		curRankString = EnumChatFormatting.GOLD + "";
            		break;
            		case "Helper":
            		curRankString = EnumChatFormatting.BLUE + "";
            		break;
            		case "Mod":
            		curRankString = EnumChatFormatting.DARK_GREEN + "";
            		break;
            		case "Admin":
            		curRankString = EnumChatFormatting.RED + "";
            		break;
            	}
            	if(rank.getValue().booleanValue()) {
            		return curRankString + mc.thePlayer.getName();
            	}else {
            		return EnumChatFormatting.BLUE + "You";
            	}
            	}else {
            		return EnumChatFormatting.BLUE + customname;
            	}
    	}
    }
    
    public enum NameMode {
    	Custom,
    	Hypixel;
    }
    
    public enum RankMode{
    	VIP,
    	VIPPlus,
    	MVP,
    	MVPPlus,
    	MVPPlusPlus,
    	Helper,
    	Mod,
    	Youtuber,
    	Admin;
    }
}

