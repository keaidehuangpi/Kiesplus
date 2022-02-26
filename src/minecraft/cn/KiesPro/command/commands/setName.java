/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import org.lwjgl.opengl.Display;

import cn.KiesPro.command.Command;
import cn.KiesPro.module.modules.player.FakeName;
import cn.KiesPro.module.modules.render.HUD;
import cn.KiesPro.utils.Helper;

public class setName
extends Command {
    public setName() {
        super("SetName", new String[]{"setname"}, "", "changed your name.");
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
            FakeName.customname = string.toString().trim();
        } else {
            Helper.sendMessage("invalid syntax Valid .setName <your name>");
        }
        return null;
    }
}

