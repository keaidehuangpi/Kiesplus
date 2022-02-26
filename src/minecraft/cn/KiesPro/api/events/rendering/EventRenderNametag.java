package cn.KiesPro.api.events.rendering;

import cn.KiesPro.api.Event;
//import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class EventRenderNametag extends Event {
    private Entity entity;
    private double x, y, z;

    public EventRenderNametag(Entity entity, double x, double y, double z){
        this.entity = entity;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {

        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
