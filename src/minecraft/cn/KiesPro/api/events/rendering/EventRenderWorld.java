package cn.KiesPro.api.events.rendering;

//import com.darkmagician6.eventapi.events.Event;


import cn.KiesPro.api.Event;

public class EventRenderWorld extends Event {
    private float partialTicks;
    public EventRenderWorld(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
