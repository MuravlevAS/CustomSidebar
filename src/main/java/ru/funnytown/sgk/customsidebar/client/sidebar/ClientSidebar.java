package ru.funnytown.sgk.customsidebar.client.sidebar;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ClientSidebar extends Gui{
	private int width;
	private String title;
	
	private static final int pLeft = 5;
	private static final int pRight = 3;
	private static final int mRight = 1;
	private static final int titleMarginB = 3;
	
	private List<String> bufferedLines;
	private List<String> newLines;
	private ReentrantLock locker;
	
	public ClientSidebar()
	{
		title = "§r";
		bufferedLines = Collections.synchronizedList(Lists.newArrayListWithCapacity(16));
		newLines = Collections.synchronizedList(Lists.newArrayListWithCapacity(16));
		locker = new ReentrantLock();
	}
	public ClientSidebar(String title)
	{
		this();
		this.title = title;
	}
	
	public void addLines(String... lines) 
	{
		for (String line : lines)
		{
			newLines.add(ChatFormatting.RESET + line);
		}
	}
	public void setTitle(String title)
	{
		locker.lock();
		this.title = title;
		locker.unlock();
	}
	public void update()
	{
		locker.lock();
		try
		{
			clear();
			if (newLines.size()==0)
			{
				bufferedLines.clear();
				return;
			}
			width = Minecraft.getMinecraft().fontRenderer.getStringWidth(newLines.get(0));
			for (String line : newLines)
			{
				int w;
				if ((w = Minecraft.getMinecraft().fontRenderer.getStringWidth(line)) > width)
					width = w; 
				bufferedLines.add(line);
			}
			newLines.clear();
		}
		finally
		{
			locker.unlock();
		}
	}
	private void clear()
	{
		bufferedLines.clear();
	}
	@SubscribeEvent
	public void clientConnect(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
		
		System.out.println("ClientConnectedToServerEvent");
		newLines.clear();
		update();
		
	}
	@SubscribeEvent
	public void bungeeEvent(FMLNetworkEvent.ClientCustomPacketEvent e)
	{
		if (e.getPacket().channel().equals("BungeeCord"))
		{
			ByteBuf buf = e.getPacket().payload();

			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			try (	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
					DataInputStream in = new DataInputStream(bais))
			{
				String cmd = in.readUTF();
				if (cmd.equalsIgnoreCase("BungeeCord ConnectOther"))
				{
					System.out.println("ConnectOther");
					newLines.clear();
					update();
				}
				else if (cmd.equalsIgnoreCase("Connect"))
				{
					System.out.println("BungeeCord Connect");
					newLines.clear();
					update();
				}
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	@SubscribeEvent
	public void clientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		
		System.out.println("ClientDisconnectionFromServerEvent");
		newLines.clear();
		update();
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent e)
	{
		
		if (e.getType() == ElementType.TEXT)
		{
			locker.lock();
			if (bufferedLines != null && bufferedLines.size() != 0)
			{
				Minecraft mc = Minecraft.getMinecraft();
				FontRenderer renderer = mc.fontRenderer;
				int screenWidth = e.getResolution().getScaledWidth();
				int screenHeight = e.getResolution().getScaledHeight();
				int y = (screenHeight /2) - (((bufferedLines.size()+1)*renderer.FONT_HEIGHT)/2)-titleMarginB;
				title = ChatFormatting.RESET + title;
				int titleWidth = renderer.getStringWidth(title);
				if (titleWidth > width)
				{
					width = titleWidth;
				}
				
				int barLeft = screenWidth-width-pLeft-pRight-mRight;
				int barRight = screenWidth-mRight;
				int center = (barLeft+barRight)/2;

				int barTop = y + renderer.FONT_HEIGHT+titleMarginB;
				int barBottom = barTop + (renderer.FONT_HEIGHT*bufferedLines.size()); 
				drawRect(barLeft, barTop-renderer.FONT_HEIGHT-titleMarginB, barRight, barTop-titleMarginB, 0x30000000);
				drawRect(barLeft, barTop, barRight, barBottom, 0x22000000);
				renderer.drawString(title, center-(titleWidth/2), y, 0xFFFFFFFF);
				y = y + renderer.FONT_HEIGHT+titleMarginB;
				
				for (String line : bufferedLines)
				{
					int lineWidth = renderer.getStringWidth(line);
					int lineX = screenWidth - lineWidth-pRight-mRight;
					
					renderer.drawString(line, lineX, y, 0xFFFFFFFF, true);
					y = y + renderer.FONT_HEIGHT;
				}
			}
			locker.unlock();
		}
	}
	@SubscribeEvent
	public void serverQuit(PlayerLoggedOutEvent e)
	{
		newLines.clear();
		update();
	}

	@SubscribeEvent
	public void serverJoin(PlayerLoggedInEvent e)
	{
		newLines.clear();
		update();
	}
}
