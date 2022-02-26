/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.EventStep;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.TimerUtil;

public class Step
        extends Module {
    private Mode SMODE = new Mode("Mode", "Mode", (Enum[]) High.values(), (Enum) High.Hypixel);
    private Numbers<Double> STEP = new Numbers<Double>("HEIGHT", "HEIGHT", 1.0, 0.0, 2.5, 0.1);
    private Numbers<Double> NCPHEIGHT = new Numbers<Double>("NCPHEIGHT", "NCPHEIGHT", 1.0, 0.0, 2.5, 0.1);
    private Numbers<Double> TIMER = new Numbers<Double>("TIMER", "TIMER", 0.6, 0.2, 1.0, 0.01);
    private Numbers<Double> DELAY = new Numbers<Double>("DELAY", "DELAY", 0.0, 0.0, 2.0, 0.1);
   // private Option<Boolean> FluxSmooth = new Option<Boolean>("FluxSmooth", "FluxSmooth", false);
    boolean resetTimer;
    public static boolean stepping = false;
    TimerUtil time = new TimerUtil();
    public static TimerUtil lastStep = new TimerUtil();

    public Step() {
        super("Step", new String[]{"Step", "autojump"}, ModuleType.Movement);
        this.addValues(this.SMODE, this.STEP, this.NCPHEIGHT, this.TIMER, this.DELAY);
    }

    enum High {
        Vanilla,
        AAC,
        Flux,
        Hypixel;
    }

    @Override
    public void onEnable() {
        resetTimer = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    @EventTarget
    public void onStep(EventStep es) {

        this.setSuffix(this.SMODE.getValue());
        if (mc.thePlayer == null) {
            return;
        }
        if (SMODE.getValue() == High.Flux) {

            if (es.isPre() && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
                es.setStepHeight(1.0D);
            } else if (!es.isPre() && es.getRealHeight() > 0.5D && es.getStepHeight() > 0.0D && !mc.thePlayer.movementInput.jump && mc.thePlayer.isCollidedVertically) {
                stepping = true;
                if (es.getRealHeight() >= 0.87D) {
                    double realHeight = es.getRealHeight();
                    double height1 = realHeight * 0.42D;
                    double height2 = realHeight * 0.75D;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height1, mc.thePlayer.posZ, mc.thePlayer.onGround));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height2, mc.thePlayer.posZ, mc.thePlayer.onGround));

                }
            }

                stepping = false;

            return;
        }
        double stepValue = 1.5;
        final float timer = ((Double) this.TIMER.getValue()).floatValue();
        final float delay = ((Double) this.DELAY.getValue()).floatValue() * 1000.0f;
        if (((Enum) this.SMODE.getValue()).toString().equalsIgnoreCase("Vanilla") || ((Enum) this.SMODE.getValue()).toString().equalsIgnoreCase("Cubecraft")) {
            stepValue = (double) this.STEP.getValue();
        }
        if (((Enum) this.SMODE.getValue()).toString().equalsIgnoreCase("Hypixel")) {
            stepValue = (double) this.NCPHEIGHT.getValue();
        }
        if (this.resetTimer) {
            this.resetTimer = !this.resetTimer;
            mc.timer.timerSpeed = 1.0f;
        }
        if (!PlayerUtil.isInLiquid()) {
            if (es.isPre()) {
                if (mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown() && this.time.delay(delay)) {
                    es.setStepHeight(stepValue);
                    es.setActive(true);
                }
            } else {
                final double minY = mc.thePlayer.getEntityBoundingBox().minY;
                final double rheight = minY - mc.thePlayer.posY;
                final boolean canStep = rheight >= 0.625;
                final String string;
                switch (string = ((Enum) this.SMODE.getValue()).toString()) {
                    case "Hypixel": {
                        if (canStep) {
                            mc.timer.timerSpeed = timer - ((rheight >= 1.0) ? (Math.abs(1.0f - (float) rheight) * (timer * 0.55f)) : 0.0f);
                            if (mc.timer.timerSpeed <= 0.05f) {
                                mc.timer.timerSpeed = 0.05f;
                            }
                            this.resetTimer = true;
                            this.ncpStep(rheight);
                            break;
                        }
                        break;
                    }
                    case "AAC": {
                        if (canStep) {
                            if (rheight < 1.1) {
                                mc.timer.timerSpeed = 0.5f;
                                this.resetTimer = true;
                            } else {
                                mc.timer.timerSpeed = 1.0f - (float) rheight * 0.57f;
                                this.resetTimer = true;
                            }
                            this.aacStep(rheight);
                            break;
                        }
                        break;
                    }
                    case "Vanilla": {
                    }
                    default:
                        break;
                }
            }
        }

    }

    void cubeStep(final double height) {

        final double posX = mc.thePlayer.posX;

        final double posZ = mc.thePlayer.posZ;

        final double y = mc.thePlayer.posY;
        final double first = 0.42;
        final double second = 0.75;

        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    }

    void ncpStep(final double height) {
        final List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);

        final double posX = mc.thePlayer.posX;

        final double posZ = mc.thePlayer.posZ;

        double y = mc.thePlayer.posY;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height != 1.0) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            if (first == 0.42) {
                first = 0.41999998688698;
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height) {

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
            }
            return;
        }
        if (height < 1.6) {
            for (int i = 0; i < offset.size(); ++i) {
                final double off = offset.get(i);
                y += off;

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        } else if (height < 2.1) {
            final double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            double[] array;
            for (int length = (array = heights).length, j = 0; j < length; ++j) {
                final double off = array[j];

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            final double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            double[] array2;
            for (int length2 = (array2 = heights).length, k = 0; k < length2; ++k) {
                final double off = array2[k];

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }
    }

    void aacStep(final double height) {

        final double posX = mc.thePlayer.posX;

        final double posY = mc.thePlayer.posY;

        final double posZ = mc.thePlayer.posZ;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height > 1.0) {
                first *= height;
                second *= height;
                if (first > 0.4349) {
                    first = 0.4349;
                } else if (first < 0.405) {
                    first = 0.405;
                }
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + first, posZ, false));
            if (posY + second < posY + height) {

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + second, posZ, false));
            }
            return;
        }
        final List<Double> offset = Arrays.asList(0.434999999999998, 0.360899999999992, 0.290241999999991, 0.220997159999987, 0.13786084000003104, 0.055);

        double y = mc.thePlayer.posY;
        for (int i = 0; i < offset.size(); ++i) {
            final double off = offset.get(i);
            final double n;
            y = (n = y + off);

            if (n > mc.thePlayer.posY + height) {

                double x = mc.thePlayer.posX;

                double z = mc.thePlayer.posZ;

                final double forward = mc.thePlayer.movementInput.moveForward;

                final double strafe = mc.thePlayer.movementInput.moveStrafe;

                final float YAW = mc.thePlayer.rotationYaw;
                double speed = 0.3;
                if (forward != 0.0 && strafe != 0.0) {
                    speed -= 0.09;
                }
                x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) * 1.0;
                z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) * 1.0;

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                break;
            }
            if (i == offset.size() - 1) {

                double x = mc.thePlayer.posX;

                double z = mc.thePlayer.posZ;

                final double forward = mc.thePlayer.movementInput.moveForward;

                final double strafe = mc.thePlayer.movementInput.moveStrafe;

                final float YAW = mc.thePlayer.rotationYaw;
                double speed = 0.3;
                if (forward != 0.0 && strafe != 0.0) {
                    speed -= 0.09;
                }
                x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) * 1.0;
                z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) * 1.0;

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            } else {

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        }
    }
}


