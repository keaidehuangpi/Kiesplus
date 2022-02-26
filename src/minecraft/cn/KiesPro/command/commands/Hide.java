/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import cn.KiesPro.Client;
import cn.KiesPro.command.Command;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.utils.Helper;
import net.minecraft.util.EnumChatFormatting;

public class Hide
extends Command {
    public Hide() {
        super("Hide", new String[]{"hide", "invisible"}, "", "Hide Toggles a specified Module");
    }

    @Override
    public String execute(String[] args) {
        String modName = "";
        if (args.length > 1) {
            modName = args[1];
        } else if (args.length < 1) {
            Helper.sendMessage("invalid syntax Valid .hide <module>");
        }
        boolean found = false;
        Module m = Client.instance.getModuleManager().getAlias(args[0]);
        if (m != null) {
            if (!m.wasRemoved()) {
                m.setRemoved(true);
            } else {
                m.setRemoved(false);
            }
            found = true;
            if (m.wasRemoved()) {
                Helper.sendMessage("> " + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.GREEN) + " invisible");
            } else {
                Helper.sendMessage("> " + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.RED) + " no invisible");
            }
        }
        if (!found) {
            Helper.sendMessage("> Module name " + (Object)((Object)EnumChatFormatting.RED) + args[0] + (Object)((Object)EnumChatFormatting.GRAY) + " is invalid");
        }
        return null;
    }
}

