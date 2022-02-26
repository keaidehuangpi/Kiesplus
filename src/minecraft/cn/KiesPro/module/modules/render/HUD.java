/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cn.KiesPro.Client;
import cn.KiesPro.api.AALAPI;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.rendering.EventRenderCape;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.AntiBot;
import cn.KiesPro.module.modules.player.Teams;
import cn.KiesPro.module.modules.render.TargetHUD.Colors;
import cn.KiesPro.module.modules.render.UI.TabUI;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.Palette;
import cn.KiesPro.utils.math.RotationUtil;
import cn.KiesPro.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class HUD
extends Module {
	private Mode colorMode = new Mode("Mode", "mode", (Enum[])ColorMode.values(), (Enum)ColorMode.Rainbow);
	public static Mode suffixMode = new Mode("SuffixType", "SuffixType", (Enum[])SuffixMode.values(), (Enum)SuffixMode.Basic);
	public Numbers<Double> CustomRGB = new Numbers<Double>("CustomRGB", "CustomRGB", 0.7D, 0.0D, 1.0D, 0.1D);
    public static Numbers<Double> R = new Numbers<Double>("Red", "Red", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> G = new Numbers<Double>("Green", "Green", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> B = new Numbers<Double>("Blue", "Blue", 255.0, 0.0, 255.0, 1.0);
    public TabUI tabui;
    private Option<Boolean> info = new Option<Boolean>("Information", "information", true);
    private Option<Boolean> customlogo = new Option<Boolean>("Logo", "logo", false);
    public static String clientName = "KiesProGC " + Client.version;
    public static boolean shouldMove;
    private String[] directions = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public HUD() {
        super("HUD", new String[]{"gui"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.setEnabled(true);
        this.setRemoved(true);
        this.addValues(this.colorMode, this.suffixMode, this.CustomRGB, this.R, this.G, this.B, this.info, this.customlogo);
    }

    @EventTarget
    private void renderHud(EventRender2D event) {
    	String name;
        if ((boolean)this.customlogo.getValue()) {
        	HUD.shouldMove = true;
        	GlStateManager.enableAlpha();
        	GlStateManager.enableBlend();
        	RenderUtil.drawCustomImage(-4, -4, 35, 33, new ResourceLocation("ETB/logo.png"));
        	GlStateManager.disableAlpha();
        	GlStateManager.disableBlend();
        	final FontRenderer fontRendererObj = this.mc.fontRendererObj;
        	final StringBuilder append3 = new StringBuilder().append(EnumChatFormatting.GRAY);
        	Client.instance.getClass();
        	final String string2 = append3.append(0.7).toString();
        	final FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
        	Client.instance.getClass();
        	final StringBuilder append4 = new StringBuilder(String.valueOf("ETB")).append(" ");
        	Client.instance.getClass();
        	fontRendererObj.drawStringWithShadow(string2, fontRendererObj2.getStringWidth(append4.append(0.7).toString()) - 12, HUD.shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
       }else {
    	   ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
           SimpleDateFormat sdf;
           Calendar cal;
           sdf = new SimpleDateFormat("hh:mm");
           cal = Calendar.getInstance();
    	   HUD.shouldMove = false;
    	   String firstLetter = clientName.substring(0, 1);
    	   final StringBuilder append6 = new StringBuilder(String.valueOf(clientName)).append(" ").append(EnumChatFormatting.GRAY);
    	   mc.fontRendererObj.drawStringWithShadow(firstLetter, 2.0f, 2.0f, new Color(R.getValue().intValue(), G.getValue().intValue(), B.getValue().intValue()).getRGB()); // new Color(102, 172, 255).getRGB()
    	   mc.fontRendererObj.drawStringWithShadow(clientName.substring(1), mc.fontRendererObj.getStringWidth(firstLetter) + 2, 2.0f, new Color(255, 255, 255).getRGB()); //170 170 170\
           mc.fontRendererObj.drawStringWithShadow("(", mc.fontRendererObj.getStringWidth(firstLetter + " " + clientName.substring(1))+2, (float)2.0f, new Color(180,180,180).getRGB());
           mc.fontRendererObj.drawStringWithShadow(sdf.format(cal.getTime()), mc.fontRendererObj.getStringWidth(firstLetter + " " + clientName.substring(1) + "(")+2, (float)2.0f, new Color(225,225,225).getRGB());
           mc.fontRendererObj.drawStringWithShadow(")", mc.fontRendererObj.getStringWidth(firstLetter + " " + clientName.substring(1) + "(" + sdf.format(cal.getTime()) + "")+2, (float)2.0f, new Color(180,180,180).getRGB());
   		}
       ArrayList<Module> sorted = new ArrayList<Module>();
       Client.instance.getModuleManager();
       for (Module m : ModuleManager.getModules()) {
    	   if (!m.isEnabled() || m.wasRemoved()) continue;
                sorted.add(m);
       }
       sorted.sort((o1, o2) -> this.mc.fontRendererObj.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - this.mc.fontRendererObj.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix())));
       int y = 1;
       int rainbowTick = 0;
       int color;
       for (Module m : sorted) {
           	color = Palette.fade(new Color(255,0,0), 100, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorWhite = Palette.fade(new Color(255,255,255), 100, sorted.indexOf(m) * 2 + 10).getRGB();//autumnµÄ½¥±ä
           	int colorBlue = Palette.fade(new Color(0, 111, 255), 10, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorBlue_2 = Palette.fade(new Color(R.getValue().intValue(), G.getValue().intValue(), B.getValue().intValue()), 10, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorCustom_2 = Palette.fade(new Color(R.getValue().intValue(), G.getValue().intValue(), B.getValue().intValue()), 10, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorCustom = Palette.fade(new Color(R.getValue().intValue(), G.getValue().intValue(), B.getValue().intValue()), 100, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorAqua = Palette.fade(new Color(0, 255, 255), 10, sorted.indexOf(m) * 2 + 10).getRGB();
           	int colorAqua_1 = Palette.fade(new Color(R.getValue().intValue(), G.getValue().intValue(), B.getValue().intValue()), 10, sorted.indexOf(m) * 2 + 10).getRGB();
           	name = m.getSuffix().isEmpty() ? m.getName() : String.format("%s %s", m.getName(), m.getSuffix());
           	float x = RenderUtil.width() - this.mc.fontRendererObj.getStringWidth(name);
           	if(colorMode.getValue() == ColorMode.Autumn_Custom) {
               	Gui.drawRect(RenderUtil.width(), y-2,x-6, y+10, new Color(0,0,0,150).getRGB());
            }else {
            	if(colorMode.getValue() == ColorMode.Autumn || colorMode.getValue() == ColorMode.Autumn_White
            			|| colorMode.getValue() == ColorMode.ETB || colorMode.getValue() == ColorMode.Rainbow || colorMode.getValue() == ColorMode.Custom) {
                Gui.drawRect(RenderUtil.width(), y-2,x-5.5, y+10, new Color(0,0,0,150).getRGB());
            	}
            }
           	Color AW = new Color(RenderUtil.getRainbow(3000, - 12 * y));
            Color rainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.7f, 1.0f));
           	Color KiesPro = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.6f, 1.0f));
           	Color Custom = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, CustomRGB.getValue().floatValue(), 1.0f));
           	if(colorMode.getValue() == ColorMode.Autumn) {
               	this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, color);
            }else if(colorMode.getValue() == ColorMode.Autumn_White) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, colorWhite);
            }else if(colorMode.getValue() == ColorMode.Rainbow) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, rainbow.getRGB());
            }else if(colorMode.getValue() == ColorMode.ETB) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, KiesPro.getRGB());
            }else if(colorMode.getValue() == ColorMode.Custom) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, Custom.getRGB());
            }else if(colorMode.getValue() == ColorMode.Autumn_Custom) {
                Gui.drawRect(RenderUtil.width(), (y - 2), (RenderUtil.width() - 2), (y + 10), colorCustom_2);
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 3.0f, y + 1f, colorCustom_2);
            }else if(colorMode.getValue() == ColorMode.Autumn_Custom_2) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 3.0f, y + 2, colorCustom_2);
            }else if(colorMode.getValue() == ColorMode.idk) {
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 3.0f, y + 3, colorWhite);
            }else if(colorMode.getValue() == ColorMode.Youtuber) {
        		Gui.drawRect(RenderUtil.width(), y-2,x-5.5, y+10, new Color(0,0,0,100).getRGB());
                Gui.drawRect(x-4, y,x-3, y+8,colorAqua);  
                mc.fontRendererObj.drawStringWithShadow(m.getName(), x-1, y+1, colorAqua);
                mc.fontRendererObj.drawStringWithShadow(m.getSuffix(),RenderUtil.width() - mc.fontRendererObj.getStringWidth(m.getSuffix())-1, y+1, new Color(180,180,180).getRGB());
            }else if(colorMode.getValue() == ColorMode.Youtuber_Custom) {
            		Gui.drawRect(RenderUtil.width(), y-2,x-5.5, y+10, new Color(0,0,0,100).getRGB());
                    Gui.drawRect(x-4, y,x-3, y+8,colorAqua_1);  
                    mc.fontRendererObj.drawStringWithShadow(m.getName(), x-1, y+1, colorAqua_1);
                    mc.fontRendererObj.drawStringWithShadow(m.getSuffix(),RenderUtil.width() - mc.fontRendererObj.getStringWidth(m.getSuffix())-1, y+1, new Color(180,180,180).getRGB());
            }else if(colorMode.getValue() == ColorMode.Power) {
            		Gui.drawRect(RenderUtil.width(), y-1,x-5, y+9, new Color(0,0,0,255).getRGB());
                    Gui.drawRect(x-4, y-0.5F,x-5, y+9,colorBlue);  
                    this.mc.fontRendererObj.drawStringWithShadow(name, x - 1.0f, y, colorBlue);
                    if (m.getAnim() < mc.fontRendererObj.getStringWidth(name) && m.isEnabled()) {
                        m.setAnim(m.getAnim() + 1);
                    }
                    if (m.getAnim() > -1 && !m.isEnabled()) {
                        m.setAnim(m.getAnim() - 1);
                    }
                    if (m.getAnim() > mc.fontRendererObj.getStringWidth(name) && m.isEnabled()) {
                        m.setAnim(mc.fontRendererObj.getStringWidth(name));
                    }
            }else if(colorMode.getValue() == ColorMode.Power2) {
        		Gui.drawRect(RenderUtil.width(), y-1,x-3, y+8, new Color(0,0,0,40).getRGB());
                Gui.drawRect(x-4, y-1.0F,x-3, y+8.5,colorBlue_2);  
                this.mc.fontRendererObj.drawStringWithShadow(name, x - 1.0f, y-0.5f, colorBlue_2);
                if (m.getAnim() < mc.fontRendererObj.getStringWidth(name) && m.isEnabled()) {
                    m.setAnim(m.getAnim() + 1);
                }
                if (m.getAnim() > -1 && !m.isEnabled()) {
                    m.setAnim(m.getAnim() - 1);
                }
                if (m.getAnim() > mc.fontRendererObj.getStringWidth(name) && m.isEnabled()) {
                    m.setAnim(mc.fontRendererObj.getStringWidth(name));
                }
            }else if(colorMode.getValue() == ColorMode.Novoline_Ayuan) {
             	 Gui.drawRect(x-4, y-1,x-3, y+8,AW.getRGB());
                 Gui.drawRect(RenderUtil.width(), y+8,x-4, y+12,AW.getRGB());
                 Gui.drawRect(RenderUtil.width(), y-1,x-3, y+11, new Color(0,0,0,255).getRGB());
                 mc.fontRendererObj.drawStringWithShadow(m.getName(), x-1, y+1, AW.getRGB());
                 mc.fontRendererObj.drawStringWithShadow(m.getSuffix(),RenderUtil.width() - mc.fontRendererObj.getStringWidth(m.getSuffix())-2, y+1, new Color(180,180,180).getRGB());
            }
            if (++rainbowTick > 50) {
                 rainbowTick = 0;
            }
            y += (colorMode.getValue() == ColorMode.Power || colorMode.getValue() == ColorMode.Power2) ? 9 : 12;
            }
            String text = (Object)((Object)EnumChatFormatting.GRAY) + "X" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posX) + " " + (Object)((Object)EnumChatFormatting.GRAY) + "Y" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posY) + " " + (Object)((Object)EnumChatFormatting.GRAY) + "Z" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posZ);
            int ychat;
            int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 25 : 10;
            int ychat2 = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 35 : 20;
            if (this.info.getValue().booleanValue()) {
                this.mc.fontRendererObj.drawStringWithShadow(text, 4.0f, new ScaledResolution(this.mc).getScaledHeight() - ychat, new Color(11, 12, 17).getRGB());
                this.mc.fontRendererObj.drawStringWithShadow((Object)((Object)EnumChatFormatting.GRAY) + "FPS: " + (Object)((Object)EnumChatFormatting.WHITE) + Minecraft.debugFPS, 4.0f, new ScaledResolution(this.mc).getScaledHeight() - ychat2, -1);
                this.drawPotionStatus(new ScaledResolution(this.mc));
                Client.instance.getClass();
                Client.instance.getClass();
            }
    }

    private void drawPotionStatus(ScaledResolution sr) {
        int y = 0;
        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            int ychat;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
            }
            int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
            this.mc.fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(PType) - 2, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + y - 12 - ychat, potion.getLiquidColor());
            y -= 10;
        }
    }
    
    enum ColorMode {
    	Autumn, Rainbow, ETB, Custom, Autumn_White, Autumn_Custom, Autumn_Custom_2, idk, Youtuber, Youtuber_Custom, Novoline_Ayuan, Power, Power2;
    }
    
    enum SuffixMode {
    	Basic, Novo;
    }
}

