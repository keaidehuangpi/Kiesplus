/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;

import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class NoRender
extends Module {
    public NoRender() {
        super("NoRender", new String[]{"noitems"}, ModuleType.Render);
        this.setColor(new Color(166, 185, 123).getRGB());
    }
}

