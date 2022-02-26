/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;
import java.util.Random;

import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class ViewClip
extends Module {
    public ViewClip() {
        super("ViewClip", new String[]{"viewclip"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }
}

