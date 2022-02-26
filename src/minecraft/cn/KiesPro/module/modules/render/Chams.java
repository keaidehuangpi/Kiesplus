/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventPostRenderPlayer;
import cn.KiesPro.api.events.rendering.EventPreRenderPlayer;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class Chams
extends Module {
    public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])ChamsMode.values(), (Enum)ChamsMode.Textured);

    public Chams() {
        super("Chams", new String[]{"seethru", "cham"}, ModuleType.Render);
        this.addValues(this.mode);
        this.setColor(new Color(159, 190, 192).getRGB());
    }

    @EventTarget
    private void preRenderPlayer(EventPreRenderPlayer e) {
        GL11.glEnable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
    }

    @EventTarget
    private void postRenderPlayer(EventPostRenderPlayer e) {
        GL11.glDisable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);
    }

    public static enum ChamsMode {
        Textured,
        Normal;
    }

}

