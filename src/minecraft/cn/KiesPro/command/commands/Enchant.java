/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import cn.KiesPro.command.Command;
import cn.KiesPro.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Enchant
extends Command {
    public Enchant() {
        super("Enchant", new String[]{"e"}, "", "enchanth");
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 1) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/give " + Minecraft.getMinecraft().thePlayer.getName() + " diamond_sword 1 0 {ench:[{id:16,lvl:127}]}");
        } else {
            Helper.sendMessage("invalid syntax Valid .enchant");
        }
        return null;
    }
}

