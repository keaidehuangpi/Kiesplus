package cn.KiesPro.module.modules.render;

import java.awt.Color;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class EnchantEffect extends Module {
    /**
     * RenderItem.java ♥
     */
    public static Option<Boolean> Rainbow = new Option<Boolean>("Rainbow", "Rainbow", false);
    public static Numbers<Double> r = new Numbers<Double>("R", "R", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> g = new Numbers<Double>("G", "G", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> b = new Numbers<Double>("B", "B", 255.0, 0.0, 255.0, 1.0);
    private Option<Boolean> breathe = new Option<Boolean>("Breathe", "Breathe", false);

    public EnchantEffect() {
        super("EnchantEffect", new String[]{"EnchantEffect", "EnchantEffect", "FluxColor"}, ModuleType.Render);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.addValues(r, g, b,breathe,Rainbow);
    }
    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (this.breathe.getValue() ==  true) {
            if (r.getValue().doubleValue() >= 255) {
                r.setValue(74.0);
            }
            if (b.getValue().doubleValue() >= 255) {
                b.setValue(84.0);
            }
            if (g.getValue().doubleValue() >= 255) {
                g.setValue(94.0);
            }
            double rint = r.getValue().doubleValue() + 1;
            double gint = g.getValue().doubleValue() + 1;
            double bint = b.getValue().doubleValue() + 1;
            r.setValue(rint);
            g.setValue(gint);
            b.setValue(bint);
        }
    }
}

