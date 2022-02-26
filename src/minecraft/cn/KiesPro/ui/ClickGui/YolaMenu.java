package cn.KiesPro.ui.ClickGui;
import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.render.RenderUtil;

public class YolaMenu extends GuiScreen {
   public static int x;
   public static int y;
   int movex;
   int movey;
   int drawy;
   int valuewheely;
   int wheely;
   int animawheely;
   int animavaluewheely;
   boolean move;
   boolean click;
   public static String category;
   public static String modname;
   public static String valuename;
   public Module cheat;
   public Value value;

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
       if (this.move) {
           if (!Mouse.isButtonDown((int)0)) {
               this.move = false;
           }
       }
      ScaledResolution sr = new ScaledResolution(this.mc);
      Gui.drawRect(0.0D, 0.0D, sr.getScaledWidth_double(), sr.getScaledHeight_double(), (new Color(0, 0, 0, 30)).getRGB());
      boolean x2 = true;
      int valueY = y;
      //202020
      RenderUtil.drawRoundRect((double)(YolaMenu.x + 55), (double)(y + 28), (double)(YolaMenu.x + 450), (double)(y + 299), 6.0F, (new Color(255, 255, 255)).getRGB()); //30 20 / 255, 255, 255
      if(category == "Combat") {
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 53), (double)(YolaMenu.x + 137), (double)(y + 80), (new Color(210, 210, 210, 150)).getRGB());
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 53), (double)(YolaMenu.x + 57), (double)(y + 80), (new Color(0, 158, 255)).getRGB());
      }
      RenderUtil.drawImage(new ResourceLocation("ETB/ClickGui/combat.png"), YolaMenu.x + 69, y + 58, 16, 16);
      mc.fontRendererObj.drawStringWithShadow("Combat", (float)(YolaMenu.x + 88), (float)(y + 63), (new Color(255,255,255)).getRGB());
      if(category == "Player") {
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 92), (double)(YolaMenu.x + 137), (double)(y + 119), (new Color(210, 210, 210, 150)).getRGB());
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 92), (double)(YolaMenu.x + 57), (double)(y + 119), (new Color(0, 158, 255)).getRGB());
      }    
      RenderUtil.drawImage(new ResourceLocation("ETB/ClickGui/player.png"), YolaMenu.x + 67, y + 98, 16, 16);
      mc.fontRendererObj.drawStringWithShadow("Player", (float)(YolaMenu.x + 88), (float)(y + 102), (new Color(255,255,255)).getRGB());  
      if(category == "Movement") {
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 132), (double)(YolaMenu.x + 137), (double)(y + 159), (new Color(210, 210, 210, 150)).getRGB());
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 132), (double)(YolaMenu.x + 57), (double)(y + 159), (new Color(0, 158, 255)).getRGB());
      }
      RenderUtil.drawImage(new ResourceLocation("ETB/ClickGui/move.png"), YolaMenu.x + 67, y + 138, 16, 16);
      mc.fontRendererObj.drawStringWithShadow("Movement", (float)(YolaMenu.x + 88), (float)(y + 142), (new Color(255,255,255)).getRGB());
      if(category == "Render") {
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 172), (double)(YolaMenu.x + 137), (double)(y + 199), (new Color(210, 210, 210, 150)).getRGB());
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 172), (double)(YolaMenu.x + 57), (double)(y + 199), (new Color(0, 158, 255)).getRGB());
      }
      RenderUtil.drawImage(new ResourceLocation("ETB/ClickGui/render.png"), YolaMenu.x + 67, y + 178, 16, 16);
      mc.fontRendererObj.drawStringWithShadow("Render", (float)(YolaMenu.x + 88), (float)(y + 182), (new Color(255,255,255)).getRGB());
      if(category == "World") {
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 213), (double)(YolaMenu.x + 137), (double)(y + 239), (new Color(210, 210, 210, 150)).getRGB());
         RenderUtil.drawRect2((double)(YolaMenu.x + 55), (double)(y + 213), (double)(YolaMenu.x + 57), (double)(y + 239), (new Color(0, 158, 255)).getRGB());
      }
      RenderUtil.drawImage(new ResourceLocation("ETB/ClickGui/world.png"), YolaMenu.x + 67, y + 218, 16, 16);
      mc.fontRendererObj.drawStringWithShadow("World", (float)(YolaMenu.x + 88), (float)(y + 222), (new Color(255,255,255)).getRGB());

      if(category == null) {
         category = "Combat";
      }

      mc.fontRendererObj.drawString("ClickGui", (double)(YolaMenu.x + 62), (double)(y + 37), (new Color(0, 158, 255)).getRGB());
      mc.fontRendererObj.drawString("KiesProGC/" + modname, YolaMenu.x + 62, y + 283, (new Color(0, 158, 255)).getRGB());
      if(this.move) {
         YolaMenu.x = mouseX - this.movex;
         y = mouseY - this.movey;
      }

      int y2 = y;
      //40, 40, 40, 150
      RenderUtil.drawBorderedRect((float)(YolaMenu.x + 137), (float)(y + 28), (float)(YolaMenu.x + 223), (float)(y + 299), 1.0F, (new Color(210, 210, 210, 150)).getRGB(), (new Color(210, 210, 210, 150)).getRGB());
      Iterator var9 = ModuleManager.getModules().iterator();

      while(true) {
         Module c2;
         do {
            if(!var9.hasNext()) {
               super.drawScreen(mouseX, mouseY, partialTicks);
               return;
            }

            c2 = (Module)var9.next();
         } while(c2.getType().toString() != category);

         this.cheat = c2;
         y2 += 27;
         if(mouseX > YolaMenu.x + 137 && mouseX < YolaMenu.x + 223 && mouseY > y + 23 && mouseY < y + 310) {
            this.drawy = Mouse.getDWheel();
            if(this.drawy != 0) {
               if(this.drawy == 120) {
                  this.animawheely += 10;
               }

               if(this.drawy == -120) {
                  this.animawheely -= 10;
               }

               if(this.animawheely >= 0) {
                  this.animawheely = 0;
               }
            }
         }

         if(this.wheely < this.animawheely && this.wheely <= 0) {
            ++this.wheely;
         }

         if(this.wheely > this.animawheely && this.wheely <= 0) {
            --this.wheely;
         }

         if(this.wheely >= 0) {
            this.wheely = 0;
         }

         GL11.glPushMatrix();
         GL11.glEnable(3089);
         RenderUtil.doGlScissor(YolaMenu.x + 54, y + 28, 200, 270);
         RenderUtil.drawCircle((float)(YolaMenu.x + 147), (float)(y2 + this.wheely + 16), 2.0F, this.cheat.isEnabled()?(new Color(61, 141, 255)).getRGB():(new Color(180, 180, 180)).getRGB());
         String modnameshowing = c2.getName().replace(" ", "");
         if(c2.isEnabled()) {
				RenderUtil.drawFilledCircle(x + 150, y2 + this.wheely + 17, 2,
						new Color(0, 255, 0).getRGB(), 5);
        	 mc.fontRendererObj.drawString(modnameshowing, (double)(YolaMenu.x + 155), (double)(y2 + this.wheely + 13), (new Color(0, 255, 0)).getRGB());
         }else {
				RenderUtil.drawFilledCircle(x + 150, y2 + this.wheely + 17, 2,
						new Color(255, 0, 0).getRGB(), 5);
        	 mc.fontRendererObj.drawString(modnameshowing, (double)(YolaMenu.x + 155), (double)(y2 + this.wheely + 13), (new Color(255, 0, 0)).getRGB());
         }
         GL11.glDisable(3089);
         GL11.glPopMatrix();
         Iterator iterator = this.cheat.getValues().iterator();

         while(iterator.hasNext()) {
            this.value = (Value)iterator.next();
            if(this.cheat.getName() == modname) {
               if(this.value instanceof Mode) {
                  valuename = ((Mode)this.value).getModeAsString();
                  valueY += 34;
               }

               if(this.value instanceof Option) {
                  valueY += 34;
               }

               GL11.glPushMatrix();
               GL11.glEnable(3089);
               RenderUtil.doGlScissor(YolaMenu.x + 160, y + 28, 275, 267);
               if(mouseX > YolaMenu.x + 160 && mouseX < YolaMenu.x + 410 && mouseY > y + 26 && mouseY < y + 280) {
                  this.drawy = Mouse.getDWheel();
                  if(this.drawy != 0) {
                     if(this.drawy == 120) {
                        this.valuewheely += 20;
                     }

                     if(this.drawy == -120) {
                        this.valuewheely -= 20;
                     }

                     if(this.valuewheely >= -10) {
                        this.valuewheely = -8;
                     }
                  }
               }

               int x = YolaMenu.x + 65;
               if(this.value instanceof Numbers) {
                  Numbers v1 = (Numbers)this.value;
                  valuename = "" + (v1.isInteger()?(double)((Number)v1.getValue()).intValue():((Number)v1.getValue()).doubleValue());
                  int render = (int)(115.0F * (((Number)v1.getValue()).floatValue() - v1.getMinimum().floatValue()) / (v1.getMaximum().floatValue() - v1.getMinimum().floatValue()));
                  double var10000 = (double)(x + 250);
                  double var10001 = (double)(valueY + this.valuewheely + 52);
                  double var10002 = (double)(x + 370);
                  valueY += 28;
                  RenderUtil.drawRect2(var10000, var10001, var10002, (double)(valueY + this.valuewheely + 25), (new Color(0, 158, 255)).getRGB());
                  RenderUtil.drawRect2((double)(x + 250), (double)(valueY + this.valuewheely + 24), (double)(x + 250 + render), (double)(valueY + this.valuewheely + 25), (new Color(0, 158, 255)).getRGB());
                  RenderUtil.drawRoundRect((double)(x + 250 + render), (double)(valueY + this.valuewheely + 22), (double)(x + 255 + render), (double)(valueY + this.valuewheely + 27), 2.0F, (new Color(0, 158, 255)).getRGB());
                  mc.fontRendererObj.drawString(valuename, (double)(x + 250 + render), (double)(valueY + this.valuewheely + 10), (new Color(40, 40, 40)).getRGB());
                  if(mouseX > x + 250 && mouseX < x + 370 && mouseY > valueY + this.valuewheely + 22 && mouseY < valueY + this.valuewheely + 27 && Mouse.isButtonDown(0)) {
                     /*render = (int)v1.getMinimum().doubleValue();
                     double max = v1.getMaximum().doubleValue();
                     double min = v1.getIncrement().doubleValue();
                     double valAbs = (double)(mouseX - (x + 250));
                     double perc = valAbs / 77.2D;
                     perc = Math.min(Math.max(0.0D, perc), 1.0D);
                     double valRel = (max - (double)render) * perc;
                     double val = (double)render + valRel;
                     val = (double)Math.round(val * (1.0D / min)) / (1.0D / min);
                     v1.setValue(Double.valueOf(val));*/
						render = (int) ((Numbers) value).getMinimum().doubleValue();
						double max = ((Numbers) value).getMaximum().doubleValue();
						double inc = ((Numbers) value).getIncrement().doubleValue();
						double valAbs = (double) mouseX - ((double) x + 250D);
						double perc = valAbs / 68.0D;
						perc = Math.min(Math.max(0.0D, perc), 1.0D);
						double valRel = (max - render) * perc;
						double val = render + valRel;
						val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
						((Numbers) value).setValue(Double.valueOf(val));
                  }
               }

               if(this.value instanceof Mode) {
                  RenderUtil.drawRoundRect((double)(x + 250), (double)(valueY + this.valuewheely + 8), (double)(x + 370), (double)(valueY + this.valuewheely + 29), 2.0F, (new Color(0, 158, 255)).getRGB());
                  mc.fontRendererObj.drawString(valuename, (int)(x + 255), (int)(valueY + this.valuewheely + 14), (new Color(255, 255, 255)).getRGB());
               }

               if(this.value instanceof Option) {
                  RenderUtil.drawRoundedRect((float)(x + 360), (float)(valueY + this.valuewheely + 14), (float)(x + 370), (float)(valueY + this.valuewheely + 24), 1.8F, ((Boolean)this.value.getValue()).booleanValue()?(new Color(0, 158, 255)).getRGB():(new Color(200, 200, 200)).getRGB());
               }

               mc.fontRendererObj.drawString(this.value.getName(), (double)(x + 170), (double)(valueY + this.valuewheely + 14), (new Color(40, 40, 40)).getRGB());
               GL11.glDisable(3089);
               GL11.glPopMatrix();
            }
         }
      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if(mouseX > YolaMenu.x + 55 && mouseX < YolaMenu.x + 137 && mouseY > y + 53 && mouseY < y + 80 && mouseButton == 0) {
         category = "Combat";
         modname = "";
      }

      if(mouseX > YolaMenu.x + 55 && mouseX < YolaMenu.x + 137 && mouseY > y + 92 && mouseY < y + 119 && mouseButton == 0) {
         category = "Player";
         modname = "";
      }

      if(mouseX > YolaMenu.x + 55 && mouseX < YolaMenu.x + 137 && mouseY > y + 132 && mouseY < y + 159 && mouseButton == 0) {
         category = "Movement";
         modname = "";
      }

      if(mouseX > YolaMenu.x + 55 && mouseX < YolaMenu.x + 137 && mouseY > y + 172 && mouseY < y + 199 && mouseButton == 0) {
         category = "Render";
         modname = "";
      }

      if(mouseX > YolaMenu.x + 55 && mouseX < YolaMenu.x + 137 && mouseY > y + 213 && mouseY < y + 239 && mouseButton == 0) {
         category = "World";
         modname = "";
      }

      if(mouseX > YolaMenu.x + 54 && mouseX < YolaMenu.x + 130 && mouseY > y + 27 && mouseY < y + 48) {
         if(mouseButton == 0) {
        	this.move = true;
            this.movex = mouseX - YolaMenu.x;
            this.movey = mouseY - y;
         } else {
        	 move = false;
         }
      }

      int y2 = y;
      int valueY = y;
      Iterator var7 = ModuleManager.getModules().iterator();

      while(true) {
         Module c2;
         do {
            if(!var7.hasNext()) {
               super.mouseClicked(mouseX, mouseY, mouseButton);
               return;
            }

            c2 = (Module)var7.next();
         } while(c2.getType().toString() != category);

         this.cheat = c2;
         y2 += 27;
         if(y2 > y + 350) {
            this.drawy = Mouse.getDWheel();
            if(mouseX > YolaMenu.x + 52 && mouseX < YolaMenu.x + 150 && mouseY > y + 23 && mouseY < y + 350 && this.drawy != 0) {
               if(this.drawy == 120) {
                  this.wheely += 10;
               }

               if(this.drawy == -120) {
                  this.wheely -= 10;
               }
            }
         }

         if(mouseX > YolaMenu.x + 137 && mouseX < YolaMenu.x + 223 && mouseY > y2 + this.wheely + 6 && mouseY < y2 + this.wheely + 26) {
            if(mouseButton == 0) {
               this.cheat.setEnabled(!this.cheat.isEnabled());
               Mouse.destroy();

               try {
                  Mouse.create();
               } catch (LWJGLException var15) {
                  var15.printStackTrace();
               }
            }

            if(mouseButton == 1) {
               modname = this.cheat.getName();
               this.valuewheely = 0;
            }
         }

         Iterator var9 = this.cheat.getValues().iterator();

         while(var9.hasNext()) {
            Value v2 = (Value)var9.next();
            int x = YolaMenu.x + 65;
            this.value = v2;
            if(this.cheat.getName() == modname) {
               if(this.value instanceof Numbers) {
                  valueY += 28;
               }

               if(this.value instanceof Mode) {
                  valueY += 34;
               }

               if(this.value instanceof Option) {
                  valueY += 34;
               }

               if(mouseX > x + 360 && mouseX < x + 370 && mouseY > valueY + this.valuewheely + 14 && mouseY < valueY + this.valuewheely + 24 && this.value instanceof Option && mouseButton == 0) {
                  this.value.setValue(Boolean.valueOf(!((Boolean)this.value.getValue()).booleanValue()));
                  Mouse.destroy();

                  try {
                     Mouse.create();
                  } catch (LWJGLException var16) {
                     var16.printStackTrace();
                  }
               }

               if(mouseX > x + 250 && mouseX < x + 370 && mouseY > valueY + this.valuewheely + 10 && mouseY < valueY + this.valuewheely + 28 && this.value instanceof Mode && mouseButton == 0) {
                  Mode m = (Mode)this.value;
                  Enum current = (Enum)m.getValue();
                  int next = current.ordinal() + 1 >= m.getModes().length?0:current.ordinal() + 1;
                  this.value.setValue(m.getModes()[next]);
                  Mouse.destroy();

                  try {
                     Mouse.create();
                  } catch (LWJGLException var17) {
                     var17.printStackTrace();
                  }
               }
            }
         }
      }
   }
}
