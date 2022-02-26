package cn.KiesPro.ui.notification;

import java.awt.Color;

import cn.KiesPro.ui.notification.Notification.Type;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class Notification {
    private String message;
    private TimerUtil timer;
    private double lastY, posY, width, height, animationX;
    private int color = new Color(38, 46, 52, 255).getRGB(), imageWidth;
    private ResourceLocation image;
    Type NMSL;
    private long stayTime;

    public Notification(String message, Type type) {
        this.message = message;
        timer = new TimerUtil();
        timer.reset();
        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) + 35;
        height = 20;
        animationX = width;
        stayTime = 2000;
        imageWidth = 13;
        NMSL = type;
        posY = -1;
        image = new ResourceLocation("ETB/Icon/" + type.name().toLowerCase() + ".png");
    }

    public void draw(double getY, double lastY) {
        this.lastY = lastY;
        animationX = getAnimationState(animationX, isFinished() ? width : 0, Math.max(isFinished() ? 200 : 30, Math.abs(animationX - (isFinished() ? width : 0)) * 5));
        if(posY == -1)
            posY = getY;
        else
            posY = getAnimationState(posY, getY, 200);
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int x1 = (int) (res.getScaledWidth() - width + animationX), x2 = (int) (res.getScaledWidth() + animationX), y1 = (int) posY, y2 = (int) (y1 + height);
        Gui.drawRect(x1, y1, x2, y2, new Color(0,0,0,150).getRGB());
        //RenderUtil.drawGradientSideways(x1, y1, x2, y2, new Color(0,255,0,140).getRGB(),new Color(0,255,0, 30).getRGB());
        if(NMSL == Type.SUCCESS) {
        	RenderUtil.drawGradientSideways(x1, (double)y1 + 19, (double)x2, y2, new Color(0,255,0).getRGB(),new Color(0,255,0,40).getRGB());
        }else if(NMSL == Type.ERROR) {
        	RenderUtil.drawGradientSideways(x1, (double)y1 + 19, (double)x2, y2, new Color(255,0,0).getRGB(),new Color(255,0,0,40).getRGB());
        }else if(NMSL == Type.INFO) {
        	RenderUtil.drawGradientSideways(x1, (double)y1 + 19, (double)x2, y2, new Color(56,149,216).getRGB(),new Color(22,70,137).getRGB());
        }else if(NMSL == Type.WARNING) {
        	RenderUtil.drawGradientSideways(x1, (double)y1 + 19, (double)x2, y2, new Color(255,255,0).getRGB(),new Color(255,255,0,40).getRGB());
        }
        
        /*if(NMSL == Type.SUCCESS) {
        	RenderUtil.drawGradientSideways(x1, y1, x2, y2, new Color(0,255,0,140).getRGB(),new Color(0,255,0,30).getRGB());
        }else if(NMSL == Type.ERROR) {
        	RenderUtil.drawGradientSideways(x1, y1, x2, y2, new Color(255,0,0,140).getRGB(),new Color(255,0,0,30).getRGB());
        }else if(NMSL == Type.INFO) {
        	RenderUtil.drawGradientSideways(x1, y1, x2, y2, new Color(56,149,216,140).getRGB(),new Color(22,70,137,30).getRGB());
        }else if(NMSL == Type.WARNING) {
        	RenderUtil.drawGradientSideways(x1, y1, x2, y2, new Color(255,255,0,140).getRGB(),new Color(255,255,0,30).getRGB());
        }*/
        
        //Gui.drawRect(x1, y1 + 19, x2, y2, new Color(255,0,0).getRGB());
        //Gui.drawRect(x1, y2, x2, y2 + 0.5F,  new Color(38, 46, 52, 255).getRGB());
        //Gui.drawRect(x1, y2, x2, y2 + 0.5F,  new Color(26, 31, 41, 255).getRGB());

        //Gui.drawRect(x1, y1, (int) (x1 + height), y2, reAlpha(-1, 0.1F));
        //RenderUtil.drawImage(image, (int)(x1 + (height - imageWidth) / 2F), y1 + (int)((height - imageWidth) / 2F), imageWidth, imageWidth);

        	//10 -1
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString(message, (float)(x1 + width / 2F) + 7, (float)(y1 + height / 3.5F), -1);
    }

    private double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState)
                animation += add;
            else
                animation = finalState;
        } else {
            if (animation - add > finalState)
                animation -= add;
            else
                animation = finalState;
        }
        return animation;
    }
    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = ((float) 1 / 255) * c.getRed();
        float g = ((float) 1 / 255) * c.getGreen();
        float b = ((float) 1 / 255) * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public boolean shouldDelete() {
        return isFinished() && animationX >= width;
    }

    private boolean isFinished() {
        return timer.reach(stayTime) && posY == lastY;
    }

    public double getHeight() {
        return height;
    }

    public enum Type {
        SUCCESS, INFO, WARNING, ERROR
    }
    
    class TimerUtil {
    	   private long time;

    	    public TimerUtil() {
    	        time = System.nanoTime() / 1000000L;
    	    }

    	    public boolean reach(final long time) {
    	        return time() >= time;
    	    }

    	    public void reset() {
    	        time = System.nanoTime() / 1000000L;
    	    }

    	    public boolean sleep(final long time) {
    	        if (time() >= time) {
    	            reset();
    	            return true;
    	        }
    	        return false;
    	    }

    	    public long time() {
    	        return System.nanoTime() / 1000000L - time;
    	    }
    }
}
