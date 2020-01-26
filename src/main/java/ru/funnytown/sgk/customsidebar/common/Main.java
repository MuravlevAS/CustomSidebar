package ru.funnytown.sgk.customsidebar.common;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.funnytown.sgk.customsidebar.client.MainClient;

@Mod(modid= Main.MODID, name = Main.NAME, version = Main.VERSION, acceptableRemoteVersions="*", clientSideOnly=true)
public class Main 
{
	public static final String MODID = "customsidebar";
	public static final String NAME = "Custom Sidebar";
	public static final String VERSION = "1.0";
	
	private static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		if (e.getSide().isClient())
		{
//			MainClient client = 
					new MainClient();
		}
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
}
