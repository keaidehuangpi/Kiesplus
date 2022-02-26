/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.api.events.misc;

import cn.KiesPro.api.Event;
import net.minecraft.entity.player.EntityPlayer;

public class EventInventory
extends Event {
    private final EntityPlayer player;

    public EventInventory(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}

