/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cn.KiesPro.command.commands;

import org.lwjgl.input.Keyboard;

import cn.KiesPro.Client;
import cn.KiesPro.command.Command;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.utils.Helper;

public class Bind
        extends Command {
    public Bind() {
        super("Bind", new String[]{"b"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 2) {
            Module m = Client.instance.getModuleManager().getAlias(args[0]);
            if (m != null) {
                int k = Keyboard.getKeyIndex((String) args[1].toUpperCase());
                m.setKey(k);
                Object[] arrobject = new Object[2];
                arrobject[0] = m.getName();
                arrobject[1] = k == 0 ? "none" : args[1].toUpperCase();
                Helper.sendMessage(String.format("Bound %s to %s", arrobject));
            } else {
                Helper.sendMessage("Invalid module name, double check spelling.");
            }
        } else {
            Helper.sendMessage("Correct usage:\u00a77 .bind <module> <key>");
        }
        return null;
    }
}

