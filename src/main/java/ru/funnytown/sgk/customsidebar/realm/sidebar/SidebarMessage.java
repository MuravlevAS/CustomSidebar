package ru.funnytown.sgk.customsidebar.realm.sidebar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class SidebarMessage implements IMessage
{
	private String title;
	private List<String> lines;
	
	public SidebarMessage(String title, List<String> lines)
	{
		this.title = title;
		this.lines = Collections.synchronizedList(lines);
	}
	
	public SidebarMessage() {}
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		try (	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream in = new DataInputStream(bais))
		{
			title = in.readUTF();
			int count = in.readInt();
			lines = Collections.synchronizedList(new ArrayList<>(count));
			for (int i = 0; i< count; i++)
			{
				lines.add(in.readUTF());
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		try (	ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(baos))
		{
			
			out.writeUTF(title);
			out.writeInt(lines.size());
			for (String line : lines)
				out.writeUTF(line);
			buf.writeBytes(baos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() 
	{
		return title;
	}

	public List<String> getLines() {
		
		return lines;
	}

	
}
