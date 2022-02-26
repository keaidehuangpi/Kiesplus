/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;
import java.util.Random;

import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class TabGui
extends Module {
	public static Option<Boolean> values = new Option<Boolean>("Values", "Values", false);
	public static Option<Boolean> valueString = new Option<Boolean>("ValueString", "ValueString", true);
    public TabGui() {
        super("TabGUI", new String[]{"tabgui"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.setRemoved(true);
        this.addValues(values, valueString);
    }
}