/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.Color;

import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[]{"eagle", "parkour"}, ModuleType.World);
        this.setColor(new Color(198, 253, 191).getRGB());
    }
}

