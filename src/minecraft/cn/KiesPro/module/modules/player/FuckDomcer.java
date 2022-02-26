/* Made by ImFl0wow */
package cn.KiesPro.module.modules.player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import cn.KiesPro.module.ModuleType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
//import me.imflowow.peaceclient.Client;
//import cn.KiesPro.api.events.player.UpdateEvent;
//import cn.KiesPro.api.events.render.Render2DEvent;
//import cn.KiesPro.api.events.world.LoadWorldEvent;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.module.Module;
import cn.KiesPro.utils.TimerUtil;
import cn.KiesPro.utils.netease.domcer.FakePacket;
import cn.KiesPro.utils.netease.domcer.HttpUtils;
//import net.minecraft.src.Config;

@SuppressWarnings("LossyEncoding")
public class FuckDomcer extends Module {

    private static Executor executor = Executors.newCachedThreadPool();

    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    byte[] uuid = UUID.randomUUID().toString().getBytes();

   /* public FuckDomcer() {
        super("Disabler", Category.Player);
    }*/
    public FuckDomcer() {
        super("FuckDomcer", new String[]{"Fdomcer"}, ModuleType.Player);
        //this.addValues(mode, BhoPSpeed, StairCheck, Lagback,timerspeed);
    }
    public void onEnable() {
        this.setSuffix("DoMCer");
        uuid = UUID.randomUUID().toString().getBytes();
    }

    @EventTarget
    public void onPacket(final EventPacketSend event) {
        this.setSuffix("DoMCer");
        if (event.getPacket() instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload packet = (S3FPacketCustomPayload) event.getPacket();
            byte[] data = new byte[packet.getBufferData().readableBytes()];
            packet.getBufferData().readBytes(data);
            if ("REGISTER".equals(packet.getChannelName())) {
                String salutation = Joiner.on('\0')
                        .join(Arrays.asList("FML|HS", "FML", "FML|MP", "CustomSkinLoader", "UView"));
                C17PacketCustomPayload proxy = new C17PacketCustomPayload("REGISTER",
                        new PacketBuffer(Unpooled.wrappedBuffer(salutation.getBytes(Charsets.UTF_8))));
                mc.getNetHandler().addToSendQueue(proxy);
                if (new String(data).contains("CustomSkinLoader")) {
                    mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("CustomSkinLoader",
                            new PacketBuffer(Unpooled.wrappedBuffer(UUID.randomUUID().toString().getBytes()))));
                    System.out.println("Bypass DoMCer1");
                }
                if (new String(data).contains("UView")) {
                    FakePacket.send();
                    System.out.println("Bypass DoMCer2");
                }
            }
            if ("CustomSkinLoader".equals(packet.getChannelName())) {

                mc.addScheduledTask(() -> {
                    String id = new String(packet.getBufferData().array());

                    int width = 1;
                    int height = 1;
                    Framebuffer buffer = mc.getFramebuffer();

                    int l = width * height;

                    if (pixelBuffer == null || pixelBuffer.capacity() < l) {
                        pixelBuffer = BufferUtils.createIntBuffer(l);
                        pixelValues = new int[l];
                    }
                    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    pixelBuffer.clear();

                    if (OpenGlHelper.isFramebufferEnabled()) {
                        GlStateManager.bindTexture(buffer.framebufferTexture);
                        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                                pixelBuffer);
                    } else {
                        GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                                pixelBuffer);
                    }
                    pixelBuffer.get(pixelValues);
                    TextureUtil.processPixelValues(pixelValues, width, height);
                    BufferedImage bufferedimage = null;

                    if (OpenGlHelper.isFramebufferEnabled()) {
                        bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                        int i1 = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                        for (int j1 = i1; j1 < buffer.framebufferTextureHeight; ++j1) {
                            for (int k1 = 0; k1 < buffer.framebufferWidth; ++k1) {
                                bufferedimage.setRGB(k1, j1 - i1,
                                        pixelValues[j1 * buffer.framebufferTextureWidth + k1]);
                            }
                        }
                    } else {
                        bufferedimage = new BufferedImage(width, height, 1);
                        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
                    }
                    BufferedImage finalBufferedimage = bufferedimage;

                    //��ͼһ������

                    executor.execute(() -> {
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            boolean foundWriter = ImageIO.write((RenderedImage) finalBufferedimage, "jpg", baos);
                            assert (foundWriter);
                            byte[] bytes = baos.toByteArray();
                            String url = "https://upload.server.domcer.com:25566/uploadJpg?key=0949a0d0-bc98-4535-9f5e-086835123f75&type="
                                    + id;
                            HashMap<String, byte[]> map = new HashMap<String, byte[]>();
                            map.put("file", bytes);
                            map.put("check", FakePacket.getMD5List().getBytes());//��MD5�б�
                            HttpUtils.HttpResponse response = HttpUtils.postFormData(url, map, null, null);
                            String result = response.getContent();
                            ByteBuf buf = Unpooled.wrappedBuffer(
                                    (id + ":" + ((JsonObject) new Gson().fromJson(result, JsonObject.class)).get("data")
                                            .getAsString()).getBytes());
                            C17PacketCustomPayload proxyPacket = new C17PacketCustomPayload("CustomSkinLoader",
                                    new PacketBuffer(buf));
                            mc.getNetHandler().addToSendQueue(proxyPacket);
                            System.out.println("Bypassed DoMCer Final");
                        } catch (IOException e) {

                        }
                    });
                });

            }
        }
    }public void onPacket(final EventPacketRecieve event) {
        this.setSuffix("DoMCer");
        if (event.getPacket() instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload packet = (S3FPacketCustomPayload) event.getPacket();
            byte[] data = new byte[packet.getBufferData().readableBytes()];
            packet.getBufferData().readBytes(data);
            if ("REGISTER".equals(packet.getChannelName())) {
                String salutation = Joiner.on('\0')
                        .join(Arrays.asList("FML|HS", "FML", "FML|MP", "CustomSkinLoader", "UView"));
                C17PacketCustomPayload proxy = new C17PacketCustomPayload("REGISTER",
                        new PacketBuffer(Unpooled.wrappedBuffer(salutation.getBytes(Charsets.UTF_8))));
                mc.getNetHandler().addToSendQueue(proxy);
                if (new String(data).contains("CustomSkinLoader")) {
                    mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("CustomSkinLoader",
                            new PacketBuffer(Unpooled.wrappedBuffer(UUID.randomUUID().toString().getBytes()))));
                    System.out.println("Bypass DoMCer1");
                }
                if (new String(data).contains("UView")) {
                    FakePacket.send();
                    System.out.println("Bypass DoMCer2");
                }
            }
            if ("CustomSkinLoader".equals(packet.getChannelName())) {

                mc.addScheduledTask(() -> {
                    String id = new String(packet.getBufferData().array());

                    int width = 1;
                    int height = 1;
                    Framebuffer buffer = mc.getFramebuffer();

                    int l = width * height;

                    if (pixelBuffer == null || pixelBuffer.capacity() < l) {
                        pixelBuffer = BufferUtils.createIntBuffer(l);
                        pixelValues = new int[l];
                    }
                    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    pixelBuffer.clear();

                    if (OpenGlHelper.isFramebufferEnabled()) {
                        GlStateManager.bindTexture(buffer.framebufferTexture);
                        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                                pixelBuffer);
                    } else {
                        GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                                pixelBuffer);
                    }
                    pixelBuffer.get(pixelValues);
                    TextureUtil.processPixelValues(pixelValues, width, height);
                    BufferedImage bufferedimage = null;

                    if (OpenGlHelper.isFramebufferEnabled()) {
                        bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
                        int i1 = buffer.framebufferTextureHeight - buffer.framebufferHeight;

                        for (int j1 = i1; j1 < buffer.framebufferTextureHeight; ++j1) {
                            for (int k1 = 0; k1 < buffer.framebufferWidth; ++k1) {
                                bufferedimage.setRGB(k1, j1 - i1,
                                        pixelValues[j1 * buffer.framebufferTextureWidth + k1]);
                            }
                        }
                    } else {
                        bufferedimage = new BufferedImage(width, height, 1);
                        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
                    }
                    BufferedImage finalBufferedimage = bufferedimage;

                    //��ͼһ������

                    executor.execute(() -> {
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            boolean foundWriter = ImageIO.write((RenderedImage) finalBufferedimage, "jpg", baos);
                            assert (foundWriter);
                            byte[] bytes = baos.toByteArray();
                            String url = "https://upload.server.domcer.com:25566/uploadJpg?key=0949a0d0-bc98-4535-9f5e-086835123f75&type="
                                    + id;
                            HashMap<String, byte[]> map = new HashMap<String, byte[]>();
                            map.put("file", bytes);
                            map.put("check", FakePacket.getMD5List().getBytes());//��MD5�б�
                            HttpUtils.HttpResponse response = HttpUtils.postFormData(url, map, null, null);
                            String result = response.getContent();
                            ByteBuf buf = Unpooled.wrappedBuffer(
                                    (id + ":" + ((JsonObject) new Gson().fromJson(result, JsonObject.class)).get("data")
                                            .getAsString()).getBytes());
                            C17PacketCustomPayload proxyPacket = new C17PacketCustomPayload("CustomSkinLoader",
                                    new PacketBuffer(buf));
                            mc.getNetHandler().addToSendQueue(proxyPacket);
                            System.out.println("Bypassed DoMCer Final");
                        } catch (IOException e) {

                        }
                    });
                });

            }
        }
    }
}
