package cn.KiesPro.module.modules.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;
import cn.KiesPro.module.modules.movement.Speed;
import cn.KiesPro.module.modules.player.InvCleaner;
import cn.KiesPro.module.modules.world.ChestStealer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

public class ModuleIndicator extends Module {
    EnumChatFormatting green = EnumChatFormatting.GREEN;
    EnumChatFormatting red = EnumChatFormatting.RED;
    String greenText = green + " Enable";
    String redText = red + " Disable";
    Color white = new Color(255,255,255);
    FontRenderer font = mc.fontRendererObj;
    Killaura aura = (Killaura) ModuleManager.getModuleByClass(Killaura.class);
    Speed speed = (Speed) ModuleManager.getModuleByClass(Speed.class);
   InvCleaner   invCleaner = (  InvCleaner) ModuleManager.getModuleByClass(  InvCleaner.class);
    ChestStealer   ChestStealer = (  ChestStealer) ModuleManager.getModuleByClass(  ChestStealer.class);

    public ModuleIndicator() {
        super("ModuleIndicator",new String[]{}, ModuleType.Render);
    }
    @EventHandler
    public void onRender2D(EventRender2D e) {
        this.setColor(new Color(0xFFE700).getRGB());
        //Killaura
        if (this.aura.isEnabled()) {
            font.drawStringWithShadow("Aura" + greenText + " "+ EnumChatFormatting.WHITE + Keyboard.getKeyName(aura.getKey()),2,155,white.getRGB());
        } else {
            font.drawStringWithShadow("Aura" + redText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName(aura.getKey()),2,155,white.getRGB());
        }
        //Speed
        if (this.speed.isEnabled()) {
            font.drawStringWithShadow("Speed" + greenText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName(speed.getKey()),2,165,white.getRGB());
        } else {
            font.drawStringWithShadow("Speed" + redText + " "+ EnumChatFormatting.WHITE + Keyboard.getKeyName(speed.getKey()),2,165,white.getRGB());
        }
        //Scaffold
        if (this.invCleaner .isEnabled()) {
            font.drawStringWithShadow("InvCleaner" + greenText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName(invCleaner .getKey()),2,175,white.getRGB());
        } else {
            font.drawStringWithShadow("InvCleaner" + redText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName(invCleaner .getKey()),2,175,white.getRGB());
        }
        //ChestStealer
        if (this. ChestStealer .isEnabled()) {
            font.drawStringWithShadow("ChestStealer " + greenText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName( ChestStealer .getKey()),2,185,white.getRGB());
        } else {
            font.drawStringWithShadow("ChestStealer " + redText + " " + EnumChatFormatting.WHITE+ Keyboard.getKeyName(ChestStealer .getKey()),2,185,white.getRGB());
        }

    }
}
