/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import cn.KiesPro.command.Command;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.TimerUtil;
import cn.KiesPro.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

public class VClip
extends Command {
    private TimerUtil timer = new TimerUtil();

    public VClip() {
        super("Vc", new String[]{"Vclip", "clip", "verticalclip", "clip"}, "", "Teleport down a specific ammount");
    }

    @Override
    public String execute(String[] args) {
        if (!Helper.onServer("enjoytheban")) {
            if (args.length > 0) {
                if (MathUtil.parsable(args[0], (byte)4)) {
                    float distance = Float.parseFloat(args[0]);
                    Helper.mc.thePlayer.setPosition(Helper.mc.thePlayer.posX, Helper.mc.thePlayer.posY + (double)distance, Helper.mc.thePlayer.posZ);
                    Helper.sendMessage("> Vclipped " + distance + " blocks");
                } else {
                    this.syntaxError((Object)((Object)EnumChatFormatting.GRAY) + args[0] + " is not a valid number");
                }
            } else {
                this.syntaxError((Object)((Object)EnumChatFormatting.GRAY) + "Valid .vclip <number>");
            }
        } else {
            Helper.sendMessage("> You cannot use vclip on the KiesPro Server.");
        }
        return null;
    }
}

