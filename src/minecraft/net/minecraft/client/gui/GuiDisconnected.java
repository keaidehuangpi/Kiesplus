package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
            	//China Hypixel
            	if(s.contains("You are temporarily banned for ")) {
            		s = s.replace("You are temporarily banned for ", "你被此服务器封禁！还有");
            		s = s.replace(" from this server!", "即可解禁。");
            	}
            	if(s.contains("You are permanently banned from this server")) {
            		s = "§c你已被此服务器永久封禁！";
            	}
            	if(s.contains("Find out more")) {
            		s = "§7了解更多：§b§nhttps://goo.gl/i662gc";
            	}
            	//ban boolean
            	if(s.contains("Cheating")) {
            		//s = "§7原因：§f操你妈为什么开纪 纪狗 气死我了 [Mod]";
            		s = "§7原因：§f使用外挂破坏玩家游戏平衡。";
            	}
            	if(s.contains("Reason")) {
            		//s = "§7原因：§f操你妈为什么狗ban 气死我了 垃圾纪狗 [WATCHDOG]";
            		//s = "§7原因：§fWATCHDOG CHEAT DELECTION";
            		s = s.replace("Reason:", "原因:");
            		s = s.replace("GG", "NE");
            	}
            	if(s.contains("Discussing")) {
            		s = "§7原因：§f大哥别嘴臭了好不好 停下你的嘴臭行为 走出黑社会 成为一名正义人士";
            	}
            	if(s.contains("account")) {
            		//s = "§7原因：§f为什么还来开纪 纪狗原来这么厉害啊 呵呵 [安全警报]";
            		s = "§7原因：§f你的账号有安全警报，请进行保护并申诉。";
            	}
            	if(s.contains("Ban ID:")) {
            		s = s.replace("Ban ID:", "封禁ID:");
            	}
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
