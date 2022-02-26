/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.command.commands;

import org.lwjgl.opengl.Display;

import cn.KiesPro.command.Command;
import cn.KiesPro.utils.Helper;

public class Title
extends Command {
    public Title() {
        super("Title", new String[]{"title"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 1) {
            StringBuilder string = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                String tempString = args[i];
                tempString = tempString.replace('&', '\u00a7');
                string.append(tempString).append(" ");
                Display.setTitle(string.toString().trim());
            }
        } else {
            Helper.sendMessage("invalid syntax Valid .Title <message>");
        }
        return null;
    }
}

