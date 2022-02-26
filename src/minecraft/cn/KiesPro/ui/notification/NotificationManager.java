package cn.KiesPro.ui.notification;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {

    private static ArrayList<Notification> notifications = new ArrayList<>();


    public void sendClientMessage(String message, Notification.Type type) {
        notifications.add(new Notification(message, type));
    }

	public static void drawNotifications() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		double startY = res.getScaledHeight() - 25;
		final double lastY = startY;
		for (int i = 0; i < notifications.size(); i++) {
			Notification not = notifications.get(i);
			if (not.shouldDelete())
				notifications.remove(i);
			not.draw(startY, lastY);
			startY -= not.getHeight() + 1;
		}
	}
    
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
}
