package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

import cn.KiesPro.Client;
import cn.KiesPro.ui.login.GuiAltManager;
import cn.KiesPro.utils.render.RenderUtil;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import static cn.KiesPro.api.AALAPI.getUsername;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    public static int animation;
    private int anim;
    private int anim1;
    private int anim2;
    private int anim3;
    private int anim4;
    private int anim5;
    private int anim6;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object threadLock;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;

    static {
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
        field_96138_a = "Please click " + (Object)EnumChatFormatting.UNDERLINE + "here" + (Object)EnumChatFormatting.RESET + " for more information.";
    }

    public GuiMainMenu() {
            BufferedReader bufferedreader;
            this.anim = 0;
            this.anim1 = 0;
            this.anim2 = 0;
            this.anim3 = 0;
            this.anim4 = 0;
            this.anim5 = 0;
            this.anim6 = 0;
            this.field_175375_v = true;
            this.threadLock = new Object();
            this.openGLWarning2 = field_96138_a;
            this.splashText = "missingno";
            bufferedreader = null;
                try {
                    String s;
                    List<String> list = Lists.<String>newArrayList();
                    bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
                    while ((s = bufferedreader.readLine()) != null)
                    {
                        s = s.trim();

                        if (!s.isEmpty())
                        {
                            list.add(s);
                        }
                    }

                    if (!list.isEmpty())
                    {
                        while (true)
                        {
                            this.splashText = (String)list.get(RANDOM.nextInt(list.size()));

                            if (this.splashText.hashCode() != 125780783)
                            {
                                break;
                            }
                        }
                    }
                }
                catch (IOException var12)
                {
                    ;
                }
                finally
                {
                    if (bufferedreader != null)
                    {
                        try
                        {
                            bufferedreader.close();
                        }
                        catch (IOException var11)
                        {
                            ;
                        }
                    }
                }
        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format((String)"title.oldgl1", (Object[])new Object[0]);
            this.openGLWarning2 = I18n.format((String)"title.oldgl2", (Object[])new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    public void updateScreen() {
        ++this.panoramaTimer;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void initGui() {
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        ISaveFormat isaveformat;
        WorldInfo worldinfo;
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12 && (worldinfo = (isaveformat = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            GuiYesNo guiyesno = GuiSelectWorld.func_152129_a((GuiYesNoCallback)this, (String)worldinfo.getWorldName(), (int)12);
            this.mc.displayGuiScreen((GuiScreen)guiyesno);
        }
    }

    private void switchToRealms() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(null);
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen((GuiScreen)this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI(this.openGLWarningLink)});
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen((GuiScreen)this);
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        int i = 8;
        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float)(j % i) / (float)i - 0.5f) / 64.0f;
            float f1 = ((float)(j / i) / (float)i - 0.5f) / 64.0f;
            float f2 = 0.0f;
            GlStateManager.translate((float)f, (float)f1, (float)f2);
            GlStateManager.rotate((float)(MathHelper.sin((float)(((float)this.panoramaTimer + p_73970_3_) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(-((float)this.panoramaTimer + p_73970_3_) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();
                if (k == 1) {
                    GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 2) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 3) {
                    GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 4) {
                    GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (k == 5) {
                    GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0f;
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;
        for (int j = 0; j < i; ++j) {
            float f = 1.0f / (float)(j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float)(j - i / 2) / 256.0f;
            worldrenderer.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos((double)k, 0.0, (double)this.zLevel).tex((double)(1.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, 0.0, (double)this.zLevel).tex((double)(1.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, (double)l, (double)this.zLevel).tex((double)(0.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport((int)0, (int)0, (int)256, (int)256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        float f = this.width > this.height ? 120.0f / (float)this.width : 120.0f / (float)this.height;
        float f1 = (float)this.height * f / 256.0f;
        float f2 = (float)this.width * f / 256.0f;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)j, (double)this.zLevel).tex((double)(0.5f - f1), (double)(0.5f + f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos((double)i, (double)j, (double)this.zLevel).tex((double)(0.5f - f1), (double)(0.5f - f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos((double)i, 0.0, (double)this.zLevel).tex((double)(0.5f + f1), (double)(0.5f - f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, (double)this.zLevel).tex((double)(0.5f + f1), (double)(0.5f + f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }

    
    float hue;
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution scaledRes;
        boolean isOverExit = mouseX > this.width - 27 && mouseX < this.width - 27 + 13 && mouseY > 10 && mouseY < 24;
        boolean isOverSingleplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 - 25 && mouseY < this.height / 2 - 5;
        boolean isOverMultiplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 && mouseY < this.height / 2 + 20;
        boolean isOverAltManager = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 + 25 && mouseY < this.height / 2 + 45;
        boolean isOverSettings = mouseX > this.width - 100 && mouseX < this.width - 82 + mc.fontRendererObj.getStringWidth("Settings") && mouseY > 10 && mouseY < 22;
        boolean isOverLanguage = mouseX > this.width - 180 && mouseX < this.width - 162 + mc.fontRendererObj.getStringWidth("Language") && mouseY > 10 && mouseY < 22;
        int size = 130;
        GlStateManager.pushMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ScaledResolution sr = scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String time2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.mc.getTextureManager().bindTexture(new ResourceLocation("ETB/background.png"));
        Gui.drawScaledCustomSizeModalRect(0, 0, (float)0.0f, (float)0.0f, (int)scaledRes.getScaledWidth(), (int)scaledRes.getScaledHeight(), (int)scaledRes.getScaledWidth(), (int)scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
        mc.fontRendererObj.drawString("KiesPro Client", (double)(this.width / 2 - mc.fontRendererObj.getStringWidth("ETB Client") / 2), (double)(this.height / 2 - 70), new Color(255, 255, 255, 180).getRGB());
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2 - 25), (float)(this.width / 2 + 45), (float)(this.height / 2 - 5), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverSingleplayer ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2), (float)(this.width / 2 + 45), (float)(this.height / 2 + 20), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverMultiplayer ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        RenderUtil.drawRoundedRect((float)(this.width / 2 - 45), (float)(this.height / 2 + 25), (float)(this.width / 2 + 45), (float)(this.height / 2 + 45), (int)new Color(255, 255, 255, 100).getRGB(), (int)(isOverAltManager ? new Color(255, 255, 255, 160).getRGB() : new Color(255, 255, 255, 100).getRGB()));
        mc.fontRendererObj.drawString("SinglePlayer", (double)(this.width / 2 - mc.fontRendererObj.getStringWidth("SinglePlayer") / 2), (double)(this.height / 2 - 19), new Color(255, 255, 255).getRGB());
        mc.fontRendererObj.drawString("MultiPlayer", (double)(this.width / 2 - mc.fontRendererObj.getStringWidth("MultiPlayer") / 2), (double)(this.height / 2 + 6), new Color(255, 255, 255).getRGB());
        mc.fontRendererObj.drawString("AltManager", (double)(this.width / 2 - mc.fontRendererObj.getStringWidth("AltManager") / 2), (double)(this.height / 2 + 31), new Color(255, 255, 255).getRGB());
        if (isOverSettings) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 99), (float)11.0f, (int)13, (int)13, (ResourceLocation)new ResourceLocation("ETB/Menu/settings.png"));
        mc.fontRendererObj.drawString("Settings", (double)(this.width - 82), 14.0, isOverSettings ? new Color(255, 255, 255, 229).getRGB() : new Color(255, 255, 255, 178).getRGB());
        if (isOverLanguage) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 180), (float)10.0f, (int)15, (int)15, (ResourceLocation)new ResourceLocation("ETB/Menu/World.png"));
        mc.fontRendererObj.drawString("Language", (double)(this.width - 162), 14.0, isOverLanguage ? new Color(255, 255, 255, 229).getRGB() : new Color(255, 255, 255, 178).getRGB());
        if (isOverExit) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.7f);
        }
        RenderUtil.drawIcon((float)(this.width - 27), (float)10.5f, (int)13, (int)13, (ResourceLocation)new ResourceLocation("ETB/Menu/Close.png"));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.fontRendererObj.drawString("Welcome, "+getUsername(), (double)(this.width - mc.fontRendererObj.getStringWidth("Welcome, "+getUsername()) - 2), (double)(this.height - 10), new Color(255, 255, 255, 178).getRGB());
        mc.fontRendererObj.drawString("KiesPro Client newwww", 2.0, (double)(this.height - 10), new Color(255, 255, 255, 178).getRGB());
        //mc.fontRendererObj.drawString("Coderight by @Mojang", 2.0, (double)(this.height - 10), new Color(255, 255, 255, 178).getRGB());
        GlStateManager.popMatrix();
    }
    
    public static void fillHorizontalGrad(double x, double y, double x2, double y2, ColorContainer ColorContainer, ColorContainer c2) {
        float a1 = (float)c2.getAlpha() / 255.0F;
        float r1 = (float)c2.getRed() / 255.0F;
        float g1 = (float)c2.getGreen() / 255.0F;
        float b1 = (float)c2.getBlue() / 255.0F;
        float a2 = (float)ColorContainer.getAlpha() / 255.0F;
        float r2 = (float)ColorContainer.getRed() / 255.0F;
        float g2 = (float)ColorContainer.getGreen() / 255.0F;
        float b2 = (float)ColorContainer.getBlue() / 255.0F;
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        wr.startDrawingQuads();
        wr.setColorRGBA(r1, g1, b1, a1);
        wr.addVertex(x + x2, y + y2, 0.0D);
        wr.addVertex(x + x2, y, 0.0D);
        wr.setColorRGBA(r2, g2, b2, a2);
        wr.addVertex(x, y, 0.0D);
        wr.addVertex(x, y + y2, 0.0D);
        tess.draw();
        GlStateManager.enableTexture2D();
     }
    
	static class Colors {
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
			int color = 0 | alpha << 24;
			color |= red << 16;
			color |= green << 8;
			color |= blue;
			return color;
		}
	}
	
	class ColorManager {
		private List<ColorObject> colorObjectList = new CopyOnWriteArrayList<ColorObject>();
		public ColorObject fTeam = new ColorObject(0, 255, 0, 255);
		public ColorObject eTeam = new ColorObject(255, 0, 0, 255);
		public ColorObject fVis = new ColorObject(0, 0, 255, 255);
		public ColorObject fInvis = new ColorObject(0, 255, 0, 255);
		public ColorObject eVis = new ColorObject(255, 0, 0, 255);
		public  ColorObject eInvis = new ColorObject(255, 255, 0, 255);
		public ColorObject hudColor = new ColorObject(0, 192, 255, 255);
		public ColorObject xhair = new ColorObject(0, 255, 0, 200);

		public List<ColorObject> getColorObjectList() {
			return colorObjectList;
		}

		public  ColorObject getFriendlyVisible() {
			return fVis;
		}

		public  ColorObject getFriendlyInvisible() {
			return fInvis;
		}

		public  ColorObject getEnemyVisible() {
			return eVis;
		}

		public  ColorObject getEnemyInvisible() {
			return eInvis;
		}

		public ColorObject getHudColor() {
			return hudColor;
		}

		public ColorManager() {
			colorObjectList.add(fVis);
			colorObjectList.add(fInvis);
			colorObjectList.add(eVis);
			colorObjectList.add(eInvis);
			colorObjectList.add(hudColor);
			colorObjectList.add(fTeam);
			colorObjectList.add(eTeam);
			colorObjectList.add(xhair);
		}
	}
	
	class ColorObject {
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
			return Colors.getColor(this.red, this.green, this.blue, this.alpha);
		}

		public void updateColors(int red, int green, int blue, int alpha) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}
	}

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean isOverLanguage;
        boolean isOverExit = mouseX > this.width - 27 && mouseX < this.width - 27 + 13 && mouseY > 10 && mouseY < 24;
        boolean isOverSingleplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 - 25 && mouseY < this.height / 2 - 5;
        boolean isOverMultiplayer = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 && mouseY < this.height / 2 + 20;
        boolean isOverAltManager = mouseX > this.width / 2 - 45 && mouseX < this.width / 2 + 45 && mouseY > this.height / 2 + 25 && mouseY < this.height / 2 + 45;
        boolean isOverSettings = mouseX > this.width - 100 && mouseX < this.width - 82 + mc.fontRendererObj.getStringWidth("Settings") && mouseY > 10 && mouseY < 22;
        boolean bl = isOverLanguage = mouseX > this.width - 180 && mouseX < this.width - 162 + mc.fontRendererObj.getStringWidth("Language") && mouseY > 10 && mouseY < 22;
        if (mouseButton == 0 && isOverSingleplayer) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (mouseButton == 0 && isOverMultiplayer) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (mouseButton == 0 && isOverAltManager) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiAltManager());
        }
        if (mouseButton == 0 && isOverSettings) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverExit) {
            this.mc.shutdown();
        }
        if (mouseButton == 0 && isOverLanguage) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
            this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.mcLanguageManager));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    class GuiMenuButton extends GuiButton {
    	   float scale = 1.0F;
    	   float targ;

    	   public GuiMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
    	      super(buttonId, x, y, widthIn, heightIn, buttonText);
    	   }

    	   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    	      if (this.visible) {
    	         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition - 2 && mouseX < this.xPosition + this.width && mouseY < this.yPosition + mc.fontRendererObj.FONT_HEIGHT + 2;
    	         this.targ = this.hovered ? 1.8F : 1.0F;
    	         float diff = (this.targ - this.scale) * 0.6F;
    	         this.scale = 1.0F + diff;
    	         GlStateManager.pushMatrix();
    	         GlStateManager.scale(this.scale, this.scale, this.scale);
    	         this.mouseDragged(mc, mouseX, mouseY);
    	         ColorManager sb = new ColorManager();
    	         int text = this.hovered ? Colors.getColor(sb.hudColor.red, sb.hudColor.green, sb.hudColor.blue, 255) : -1;
    	         GlStateManager.pushMatrix();
    	         float offset = (float)(this.xPosition + this.width / 2) / this.scale;
    	         GlStateManager.translate(offset, (float)this.yPosition / this.scale, 1.0F);
    	         RenderUtil.drawFancy(-31.0D, -2.0D, 31.0D, 14.0D, Colors.getColor(21));
    	         RenderUtil.drawFancy(-30.0D, -1.0D, 30.0D, 13.0D, Colors.getColor(28));
    	         RenderUtil.rectangle(-29.0D, (double)(-1.0F / this.scale), 31.0D, (double)(-0.5F / this.scale), Colors.getColor(sb.hudColor.red, sb.hudColor.green, sb.hudColor.blue, 255));
    	         mc.fontRendererObj.drawStringWithShadow(this.displayString, -(mc.fontRendererObj.getStringWidth(this.displayString) / 2.0F), 2F, text);
    	         GlStateManager.popMatrix();
    	         GlStateManager.popMatrix();
    	      }

    	   }
    }
    
    class ColorContainer {
    	@Expose
    	private int red;
    	@Expose
    	private int green;
    	@Expose
    	private int blue;
    	@Expose
    	private int alpha;

    	public ColorContainer(int r, int g, int b, int a) {
    		this.red = r;
    		this.green = g;
    		this.blue = b;
    		this.alpha = a;
    	}

    	public ColorContainer(int r, int g, int b) {
    		this(r, g, b, 225);
    	}

    	public ColorContainer(int b) {
    		this(b, b, b);
    	}

    	public ColorContainer(int b, int a) {
    		this(b, b, b, a);
    	}

    	public void setColor(int r, int g, int b, int a) {
    		this.red = r;
    		this.green = g;
    		this.blue = b;
    		this.alpha = a;
    	}

    	public void setRed(int red) {
    		this.red = red;
    		this.setColor(red, this.green, this.blue, this.alpha);
    	}

    	public void setGreen(int green) {
    		this.green = green;
    		this.setColor(this.red, green, this.blue, this.alpha);
    	}

    	public void setBlue(int blue) {
    		this.blue = blue;
    		this.setColor(this.red, this.green, blue, this.alpha);
    	}

    	public void setAlpha(int alpha) {
    		this.alpha = alpha;
    		this.setColor(this.red, this.green, this.blue, alpha);
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

    	public int getIntCol() {
    		return Colors.getColor(this.red, this.green, this.blue, this.alpha);
    	}
    }
}
