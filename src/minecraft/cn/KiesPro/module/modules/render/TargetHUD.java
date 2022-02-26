/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;
import cn.KiesPro.module.modules.render.ClickGui.gui;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.render.RenderUtil;

public class TargetHUD
extends Module {
	private Mode mode = new Mode("Mode","Mode",(Enum[])GUI.values(), (Enum)GUI.Basic);
    private static final Color COLOR = new Color(0, 0, 0, 180);
    private final Stopwatch animationStopwatch = new Stopwatch();
    //private EntityOtherPlayerMP target;
    private double healthBarWidth;
    private double hudHeight;

    public TargetHUD() {
        super("TargetHud", new String[]{"targethud"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(mode);
    }
    
    enum GUI {
    	Basic,
    	Novo;
    }
    
    private float width2 = 0.0f;
    
    @EventTarget
    public void on2D(EventRender2D event) {
    	if(mode.getValue() == GUI.Basic) {
		ScaledResolution res = new ScaledResolution(this.mc);	 
		int x = res.getScaledWidth() /2 + 10;
		int y = res.getScaledHeight() - 90;
        final EntityLivingBase player = (EntityLivingBase) Killaura.curTarget;
         if (player != null) {
            GlStateManager.pushMatrix();
            RenderUtil.drawRect(x+0.0, y+0.0, x+113.0, y+36.0, Colors.getColor(0, 150));
            
            mc.fontRendererObj.drawStringWithShadow(player.getName(), x+38.0f, y+2.0f, -1);
       
            BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
    		bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
    		double HEALTH = bigDecimal.doubleValue();
    		
            BigDecimal DT = new BigDecimal((double)mc.thePlayer.getDistanceToEntity(player));
    		DT = DT.setScale(1, RoundingMode.HALF_UP);
    		double Dis = DT.doubleValue();
    		
            final float health = player.getHealth();
            final float[] fractions = { 0.0f, 0.5f, 1.0f };
            final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
            final float progress = health / player.getMaxHealth();
            final Color customColor = (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : Color.RED;
            double width = (double)mc.fontRendererObj.getStringWidth(player.getName());
            width = PlayerUtil.getIncremental(width, 10.0);
            if (width < 50.0) {
                width = 50.0;
            }
            final double healthLocation = width * progress;
            RenderUtil.rectangle(x+37.5, y+11.5, x+38.0 + healthLocation + 0.5, y+14.5, customColor.getRGB());
            RenderUtil.rectangleBordered(x+37.0, y+11.0, x+39.0 + width, y+15.0, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
            for (int i = 1; i < 10; ++i) {
                final double dThing = width / 10.0 * i;
                RenderUtil.rectangle(x+38.0 + dThing, y+11.0, x+38.0 + dThing + 0.5, y+15.0, Colors.getColor(0));
            }
            String COLOR1;
            if (health > 20.0D) {
               COLOR1 = " \2479";
            } else if (health >= 10.0D) {
               COLOR1 = " \247a";
            } else if (health >= 3.0D) {
               COLOR1 = " \247e";
            } else {
               COLOR1 = " \2474";
            }
            //RenderUtil.rectangleBordered(x+1.0, y+1.0, x+35.0, y+35.0, 0.5, Colors.getColor(0, 0), Colors.getColor(255));
            
            GlStateManager.scale(0.5, 0.5, 0.5);
            final String str = "HP: "+ HEALTH + " Dist: " + Dis;
            mc.fontRendererObj.drawStringWithShadow(str, x*2+76.0f, y*2+35.0f, -1);
            final String str2 = String.format("Yaw: %s Pitch: %s ", (int)player.rotationYaw, (int)player.rotationPitch);
            mc.fontRendererObj.drawStringWithShadow(str2, x*2+76.0f, y*2+47.0f, -1);
            final String str3 = String.format("G: %s HURT: %s TE: %s", player.onGround, player.hurtTime, player.ticksExisted);
            mc.fontRendererObj.drawStringWithShadow(str3, x*2+76.0f, y*2+59.0f, -1);

            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
       
            /*if(player instanceof EntityPlayer) {
            final List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)mc.thePlayer.sendQueue.getPlayerInfoMap());
            for (final Object aVar5 : var5) {
                final NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                if (mc.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) == player) {
                    mc.getTextureManager().bindTexture(var6.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
                    if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(x+2, y+2, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
                    }
                    GlStateManager.bindTexture(0);
                    break;
                }
            }
            
            }*/
			
			RenderUtil.drawEntityOnScreen(x + 18, y + 33, 16, 1.0f, 15.0f, player);
            
            GlStateManager.popMatrix();
        }
    	}else if(mode.getValue() == GUI.Novo) {
            int ccolor = 0;
            ScaledResolution res = new ScaledResolution(mc);
            int x = (int) (res.getScaledWidth() / 2 + 20.0);
            int y = (int) (res.getScaledHeight() / 2 + 10.0);
            EntityLivingBase player = Killaura.curTarget;
            if (player != null) {
            	float f = 0;
                GlStateManager.pushMatrix();
                RenderUtil.drawImage(new ResourceLocation("ETB/TargetHud.png"), x - 10, y - 8, 153, 54);
                ccolor = new Color(255, 255, 255).getRGB();
                double wdnmd = 90.796;
                float health2 = player.getHealth() / player.getMaxHealth();
                if ((double)this.width2 < wdnmd * (double)f) {
                    if (wdnmd * (double)health2 - (double)this.width2 < 1.0) {
                        this.width2 = (float)(wdnmd * (double)health2);
                    }
                    this.width2 = (float)((double)this.width2 + 1.6);
                }
                if (wdnmd * (double)health2 - (double)this.width2 > 1.0) {
                    this.width2 = (float)(wdnmd * (double)health2);
                }
                this.width2 = (float)((double)this.width2 - 1.6);
                mc.fontRendererObj.drawString(player.getName(), (double)((int)((float)x + 38.0f)), (double)(y + 1), ccolor);
                BigDecimal DT = new BigDecimal((double)mc.thePlayer.getDistanceToEntity(player));
                DT = DT.setScale(1, RoundingMode.HALF_UP);
                double Dis = DT.doubleValue();
                BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
                bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
                double HEALTH = bigDecimal.doubleValue();
                String str = "HP: " + HEALTH + "  Dis: " + (int)mc.thePlayer.getDistanceToEntity(player);
                mc.fontRendererObj.drawString(str, (double)((float)x + 38.0f), (double)((float)y + 28.0f), ccolor);
                float health = player.getHealth();
                float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
                Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                float progress = health / player.getMaxHealth();
                Color customColor = health2 >= 0.0f ? TargetHUD.blendColors(fractions, colors, progress).brighter() : Color.RED;
                double width = mc.fontRendererObj.getStringWidth(player.getName());
                width = this.getIncremental(width, 18.0);
                if (width < 71.0) {
                    width = 71.0;
                }
                double healthLocation = width * (double)progress;
                Gui.drawRect((double)(x + 37), (double)(y + 14), (double)((float)(x + 37) + this.width2), (double)(y + 25), (int)customColor.getRGB());
                RenderUtil.drawGradientSideways((double)(x + 37), (double)(y + 14), (double)(x + 65 + this.width2), (double)(y + 25), new Color(255, 0, 0).getRGB(), new Color(255, 116, 38).getRGB());
                RenderUtil.rectangleBordered((double)((double)x + 37.0), (double)(y + 14), (double)(x + 57 + 70), (double)((double)y + 25.0), (double)0.5, (int)Colors.getColor((int)0, (int)0), (int)new Color(0, 0, 0).getRGB());
                String string = (double)health > 20.0 ? " \u00a79" : ((double)health >= 10.0 ? " \u00a7a" : ((double)health >= 3.0 ? " \u00a7e" : " \u00a74"));
                    GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                    if (player instanceof EntityPlayer) {
                        List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)mc.thePlayer.sendQueue.getPlayerInfoMap());
                        for (Object aVar5 : var5) {
                            NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                            if (mc.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) != player) continue;
                            mc.getTextureManager().bindTexture(var6.getLocationSkin());
                            Gui.drawScaledCustomSizeModalRect((x - 3), ((int)((double)y + 0.7)), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)36, (int)36, (float)64.0f, (float)64.0f);
                            if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
                            	Gui.drawScaledCustomSizeModalRect((x - 3), ((int)((double)y + 0.7)), (float)40.0f, (float)8.0f, (int)8, (int)8, (int)36, (int)36, (float)64.0f, (float)64.0f);
                            }
                            GlStateManager.bindTexture((int)0);
                            break;
                        }
                    }
                GlStateManager.popMatrix();
            }
    	}
    }
    
	public void prepareScissorBox(float x, float y, float x2, float y2) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int) ((int) (x * (float) factor)),
				(int) ((int) (((float) scale.getScaledHeight() - y2) * (float) factor)),
				(int) ((int) ((x2 - x) * (float) factor)), (int) ((int) ((y2 - y) * (float) factor)));
	}
    
    private double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
	
    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current += factor) : (current -= factor);
        return current;
    }
    
    public static Color getHealthColor(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static int[] getFractionIndices(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = color1.getColorComponents(new float[3]);
        float[] rgb2 = color2.getColorComponents(new float[3]);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        return color3;
    }
    
    enum Colors {
    	BLACK(-16711423),
        BLUE(-12028161),
        DARKBLUE(-12621684),
        GREEN(-9830551),
        DARKGREEN(-9320847),
        WHITE(-65794),
        AQUA(-7820064),
        DARKAQUA(-12621684),
        GREY(-9868951),
        DARKGREY(-14342875),
        RED(-65536),
        DARKRED(-8388608),
        ORANGE(-29696),
        DARKORANGE(-2263808),
        YELLOW(-256),
        DARKYELLOW(-2702025),
        MAGENTA(-18751),
        DARKMAGENTA(-2252579);
        
        public int c;

        private Colors(int co) {
            this.c = co;
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
            color |= blue;
            return color;
        }
    }
}

