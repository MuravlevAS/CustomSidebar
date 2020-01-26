package ru.funnytown.sgk.customsidebar.realm.sidebar;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.funnytown.sgk.customsidebar.client.MainClient;

public class SidebarMessageHandler implements IMessageHandler<SidebarMessage, IMessage>
{
	public SidebarMessageHandler() {
	}
	@Override
	public IMessage onMessage(SidebarMessage message, MessageContext ctx) 
	{
		if (ctx.side.isClient())
		{
			MainClient.getPlayer().getSidebar().setTitle(message.getTitle());
			for (String line : message.getLines())
				MainClient.getPlayer().getSidebar().addLines(line);
			MainClient.getPlayer().getSidebar().update();
		}
		return null;
	}
	
}
