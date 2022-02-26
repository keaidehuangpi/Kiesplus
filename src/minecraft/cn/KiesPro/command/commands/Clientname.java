/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import org.lwjgl.opengl.Display;

import cn.KiesPro.command.Command;
import cn.KiesPro.module.modules.render.HUD;
import cn.KiesPro.utils.Helper;

public class Clientname
extends Command {
    public Clientname() {
        super("ClientName", new String[]{"clientname", "rename"}, "", "changed client name.");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 2) {
            StringBuilder string = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                String tempString = args[i];
                tempString = tempString.replace('&', '\u00a7');
                string.append(tempString).append(" ");
            }
            HUD.clientName = string.toString().trim();
        } else {
            Helper.sendMessage("invalid syntax Valid .clientname <message>");
        }
        return null;
    }
}

