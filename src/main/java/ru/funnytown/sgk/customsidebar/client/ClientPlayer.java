package ru.funnytown.sgk.customsidebar.client;

import ru.funnytown.sgk.customsidebar.client.sidebar.ClientSidebar;

public class ClientPlayer 
{
	private ClientSidebar sidebar;
	
	public ClientPlayer() 
	{
		this.sidebar = new ClientSidebar();
	}
	
	public ClientSidebar getSidebar()
	{
		
		return sidebar;
	}
	// UPDATING SIDEBAR
//	Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
//		sidebar.setTitle("testing");
//		sidebar.addLines(ChatFormatting.values()[i % ChatFormatting.values().length]+ "Hello world"+ i++);
//		sidebar.addLines("�aHello world"+ i);
//		sidebar.addLines("Hello world"+ i);
//		sidebar.addLines("Hello world"+ i);
//		sidebar.addLines("Hello world"+ i);
//		sidebar.addLines("Hello world"+ i);
//		sidebar.update();
//		
//	}, 0, 50, TimeUnit.MILLISECONDS);
}
