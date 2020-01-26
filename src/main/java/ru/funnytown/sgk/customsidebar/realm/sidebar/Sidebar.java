package ru.funnytown.sgk.customsidebar.realm.sidebar;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayerMP;
import ru.funnytown.sgk.customsidebar.common.network.FunnyTownPacketHandler;

public class Sidebar 
{
	private String title;
	private List<String> newLines;
	private EntityPlayerMP owner;
	
	public Sidebar setTitle(String title)
	{
		this.title = "§r" + title;
		return this;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Sidebar addLine(String text) 
	{
		newLines.add(text);
		return this;
	}
	
	public void update()
	{
		SidebarMessage message = new SidebarMessage(title, newLines);
		FunnyTownPacketHandler.INSTANCE.sendTo(message, owner);
		clear();
	}
	
	public Sidebar clear()
	{
		newLines.clear();
		return this;
	}
	
	public Sidebar() 
	{
		setTitle("");
		newLines = Collections.synchronizedList(Lists.newArrayListWithCapacity(16));
	}
	
	public Sidebar setOwner(EntityPlayerMP player) {
		this.owner = player;
		return this;
	}
}
