/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.render.RenderUtil;

public class MiniMap
extends Module {
	   private boolean dragging;
	    float hue;
	    private Option<Boolean> rect = new Option<Boolean>("Rect", "Rect", true);
	    private Numbers<Double> scale = new Numbers<Double>("Scale", "Scale", 2.0,1.0,5.0,0.1);
	    private Numbers<Double> x = new Numbers<Double>("X", "X", 500.0, 1.0, 1920.0, 5.0);
	    private Numbers<Double> y = new Numbers<Double>("Y", "Y", 2.0,1.0,1080.0, 5.0);
	    private Numbers<Double> size = new Numbers<Double>("Size", "Size", 125.0,50.0, 500.0, 5.0);
	    private Mode mode = new Mode("Mode", "mode", (Enum[])MobsMode.values(), (Enum)MobsMode.Player);
    public MiniMap() {
        super("MiniMap", new String[]{"Radar"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(mode, scale, x, y, size, rect);
    }
    
    @EventTarget
    public void onGui(EventRender2D e) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int size1 = ((Double)this.size.getValue()).intValue();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        float playerOffsetX = (float)Minecraft.getMinecraft().thePlayer.posX;
        float playerOffSetZ = (float)Minecraft.getMinecraft().thePlayer.posZ;
        int var141 = sr.getScaledWidth();
        int var151 = sr.getScaledHeight();
        int mouseX = Mouse.getX() * var141 / this.mc.displayWidth;
        int mouseY = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
        if ((float)mouseX >= xOffset && (float)mouseX <= xOffset + (float)size1 && (float)mouseY >= yOffset - 3.0f && (float)mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0) {
            this.dragging = !this.dragging;
            boolean bl = this.dragging;
        }
        if (this.dragging && this.mc.currentScreen instanceof GuiChat) {
            Object newValue = StringConversions.castNumber((String)Double.toString((double)(mouseX - size1 / 2)), (Object)5);
            this.x.setValue((Double)((Double)newValue));
            Object newValueY = StringConversions.castNumber((String)Double.toString((double)(mouseY - 2)), (Object)5);
            this.y.setValue((Double)((Double)newValueY));
        } else {
            this.dragging = false;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor((float)(h / 255.0f), (float)0.9f, (float)1.0f);
        Color color332 = Color.getHSBColor((float)(h2 / 255.0f), (float)0.9f, (float)1.0f);
        Color color333 = Color.getHSBColor((float)(h3 / 255.0f), (float)0.9f, (float)1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        int rainbowTick = 0;
        this.hue = (float)((double)this.hue + 0.1);
        //RenderUtil.rectangleBordered((double)xOffset, (double)yOffset, (double)(xOffset + (float)size1), (double)(yOffset + (float)size1), (double)0.5, (int)Colors2.getColor((int)0), (int)Colors2.getColor((int)61));
        //RenderUtil.rectangleBordered((double)(xOffset + 1.0f), (double)(yOffset + 1.0f), (double)(xOffset + (float)size1 - 1.0f), (double)(yOffset + (float)size1 - 1.0f), (double)1.0, (int)Colors2.getColor((int)0), (int)Colors2.getColor((int)61));
        RenderUtil.rectangleBordered((double)((double)xOffset + 2.5), (double)((double)yOffset + 2.5), (double)((double)(xOffset + (float)size1) - 2.5), (double)((double)(yOffset + (float)size1) - 2.5), (double)0.5, (int)getColor((int)0, 170), (int)getColor((int)0, 170));
        Color moonrainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.3f, 1.0f));
        RenderUtil.rectangleBordered((double)(xOffset + 3.0f), (double)(yOffset + 3.0f), (double)(xOffset + (float)size1 - 3.0f), (double)(yOffset + (float)size1 - 3.0f), (double)0.5, (int)getColor(25, 170), (int)getColor((int)25,170));
        //RenderUtil.drawGradientSideways((double)(xOffset + 3.0f), (double)(yOffset + 3.0f), (double)(xOffset + (float)(size1 / 2)), (double)((double)yOffset + 4), (int)color1, (int)color2);
        //RenderUtil.drawGradientSideways((double)(xOffset + (float)(size1 / 2)), (double)(yOffset + 3.0f), (double)(xOffset + (float)size1 - 3.0f), (double)((double)yOffset + 4), (int)color2, (int)color3);
        
        if(rect.getValue().booleanValue()) {
        RenderUtil.drawGradientSideways((double)(xOffset + 3.0f), (double)(yOffset + 3.0f), (double)(xOffset + (float)(size1 / 2)), (double)((double)yOffset +3.4),(int)color1, (int)color2);
        RenderUtil.drawGradientSideways((double)(xOffset + (float)(size1 / 2)), (double)(yOffset + 3.0f), (double)(xOffset + (float)size1 - 3.0f), (double)((double)yOffset + 3.4), (int)color2, (int)color3);
        }
        RenderUtil.rectangle((double)((double)xOffset + ((double)(size1 / 2) - 0.5)), (double)((double)yOffset + 3.5), (double)((double)xOffset + ((double)(size1 / 2) + 0.5)), (double)((double)(yOffset + (float)size1) - 3.5), (int)getColor((int)255, (int)50));
        RenderUtil.rectangle((double)((double)xOffset + 3.5), (double)((double)yOffset + ((double)(size1 / 2) - 0.5)), (double)((double)(xOffset + (float)size1) - 3.5), (double)((double)yOffset + ((double)(size1 / 2) + 0.5)), (int)getColor((int)255, (int)50));
        
        if(++rainbowTick > 50) {
        	rainbowTick = 0;
        }
        
        for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
        	if(mode.getValue() == MobsMode.Player) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !(ent = (EntityPlayer)o).isEntityAlive() || ent == Minecraft.getMinecraft().thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer((EntityPlayer)Minecraft.getMinecraft().thePlayer)) continue;
            float pTicks = this.mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (Double)this.scale.getValue());
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * (Double)this.scale.getValue());
            int color = Minecraft.getMinecraft().thePlayer.canEntityBeSeen((Entity)ent) ? ColorManager.getEnemyVisible().getColorInt() : ColorManager.getEnemyInvisible().getColorInt();
            float cos = (float)Math.cos((double)((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295));
            float sin = (float)Math.sin((double)((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295));
            float rotY = (- posZ) * cos - posX * sin;
            float rotX = (- posX) * cos + posZ * sin;
            if (rotY > (float)(size1 / 2 - 5)) {
                rotY = (float)(size1 / 2) - 5.0f;
            } else if (rotY < (float)((- size1) / 2 - 5)) {
                rotY = (- size1) / 2 - 5;
            }
            if (rotX > (float)(size1 / 2) - 5.0f) {
                rotX = size1 / 2 - 5;
            } else if (rotX < (float)((- size1) / 2 - 5)) {
                rotX = - (float)(size1 / 2) - 5.0f;
            }
            RenderUtil.rectangleBordered((double)((double)(xOffset + (float)(size1 / 2) + rotX) - 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) - 1.5), (double)((double)(xOffset + (float)(size1 / 2) + rotX) + 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) + 1.5), (double)0.5, (int)color, (int)getColor((int)46));
        	}else {
        	EntityMob ent;
            if (!(o instanceof EntityMob) || !(ent = (EntityMob)o).isEntityAlive() || ent.isInvisible()) continue;
            float pTicks = this.mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (Double)this.scale.getValue());
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * (Double)this.scale.getValue());
            int color = Minecraft.getMinecraft().thePlayer.canEntityBeSeen((Entity)ent) ? ColorManager.getEnemyVisible().getColorInt() : ColorManager.getEnemyInvisible().getColorInt();
            float cos = (float)Math.cos((double)((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295));
            float sin = (float)Math.sin((double)((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295));
            float rotY = (- posZ) * cos - posX * sin;
            float rotX = (- posX) * cos + posZ * sin;
            if (rotY > (float)(size1 / 2 - 5)) {
                rotY = (float)(size1 / 2) - 5.0f;
            } else if (rotY < (float)((- size1) / 2 - 5)) {
                rotY = (- size1) / 2 - 5;
            }
            if (rotX > (float)(size1 / 2) - 5.0f) {
                rotX = size1 / 2 - 5;
            } else if (rotX < (float)((- size1) / 2 - 5)) {
                rotX = - (float)(size1 / 2) - 5.0f;
            }
            RenderUtil.rectangleBordered((double)((double)(xOffset + (float)(size1 / 2) + rotX) - 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) - 1.5), (double)((double)(xOffset + (float)(size1 / 2) + rotX) + 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) + 1.5), (double)0.5, (int)color, (int)getColor((int)46));
        	}
        }
    }
    
    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }
    
    static class ColorManager {
        private List<ColorObject> colorObjectList = new CopyOnWriteArrayList();
        public static ColorObject fTeam = new ColorObject(0, 255, 0, 255);
        public static ColorObject eTeam = new ColorObject(255, 0, 0, 255);
        public static ColorObject fVis = new ColorObject(0, 0, 255, 255);
        public static ColorObject fInvis = new ColorObject(0, 255, 0, 255);
        public static ColorObject eVis = new ColorObject(255, 0, 0, 255);
        public static ColorObject eInvis = new ColorObject(255, 255, 0, 255);
        public static ColorObject hudColor = new ColorObject(0, 192, 255, 255);
        public static ColorObject xhair = new ColorObject(0, 255, 0, 200);

        public List<ColorObject> getColorObjectList() {
            return colorObjectList;
        }

        public ColorObject getFriendlyVisible() {
            return fVis;
        }

        public ColorObject getFriendlyInvisible() {
            return fInvis;
        }

        public static ColorObject getEnemyVisible() {
            return eVis;
        }

        public static ColorObject getEnemyInvisible() {
            return ColorManager.eInvis;
        }

        public ColorObject getHudColor() {
            return hudColor;
        }

        public ColorManager() {

        }
    }
    
    static class ColorObject {
        public int red;
        public int green;
        public int blue;
        public int alpha;

        public ColorObject(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public int getRed() {
            return this.red;
        }

        public int getGreen() {
            return this.green;
        }

        public int getBlue() {
            return this.blue;
        }

        public int getAlpha() {
            return this.alpha;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public int getColorInt() {
            return getColor((int)this.red, (int)this.green, (int)this.blue, (int)this.alpha);
        }

        public void updateColors(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
    
    static class StringConversions {
        public static Object castNumber(String newValueText, Object currentValue) {
            if (newValueText.contains((CharSequence)".")) {
                if (newValueText.toLowerCase().contains((CharSequence)"f")) {
                    return Float.valueOf((float)Float.parseFloat((String)newValueText));
                }
                return Double.parseDouble((String)newValueText);
            }
            if (StringConversions.isNumeric(newValueText)) {
                return Integer.parseInt((String)newValueText);
            }
            return newValueText;
        }

        public static boolean isNumeric(String text) {
            try {
                Integer.parseInt((String)text);
                return true;
            }
            catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
    }
    
    enum MobsMode {
    	Player,
    	Mobs;
    }
}

