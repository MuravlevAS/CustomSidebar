package ru.funnytown.sgk.customsidebar.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import ru.funnytown.sgk.customsidebar.common.Main;
import ru.funnytown.sgk.customsidebar.common.network.FunnyTownPacketHandler;
import ru.funnytown.sgk.customsidebar.realm.sidebar.SidebarMessage;
import ru.funnytown.sgk.customsidebar.realm.sidebar.SidebarMessageHandler;

public class MainClient 
{
	private static ClientPlayer player;

	public static int discriminator = 100;
	public MainClient()
	{
		init();
	}
	int i = 0;
	public void init()
	{
		player = new ClientPlayer();
		
		MinecraftForge.EVENT_BUS.register(player.getSidebar());
		
		FunnyTownPacketHandler.INSTANCE.registerMessage(SidebarMessageHandler.class, SidebarMessage.class, discriminator++, Side.CLIENT);
		Main.getLogger().info("Mod has initialized on client side");
	}

	public static ClientPlayer getPlayer()
	{
		return player;
	}
}
