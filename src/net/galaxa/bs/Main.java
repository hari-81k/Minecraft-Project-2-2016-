package net.galaxa.bs;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import net.galaxa.bs.cmds.SignCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Main extends JavaPlugin implements PluginMessageListener
{
	public static Plugin p;
	
	public static ServerListPing17 c;
	
	public static ArrayList<Sign> sign2 = new ArrayList<Sign>();
	public static HashMap<Location, String> sign22 = new HashMap<Location, String>();
	public static HashMap<Location, String> sign222 = new HashMap<Location, String>();

	public void onEnable()
	{
		
		p = this;

		c = new ServerListPing17();

		getServer().getPluginManager().registerEvents(new SignEvent(), this);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		getCommand("bsign").setExecutor(new SignCommand());

		saveDefaultConfig();

		setupSigns();

		refresh();
	}

	public static void setupSigns()
	{
		sign2.clear();
		sign22.clear();
		sign222.clear();

		for (String s : getPlugin().getConfig().getStringList("SignLocations"))
		{
			String[] s2 = s.split("!");

			Location loc = new Location(Bukkit.getWorld(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2]), Integer.parseInt(s2[3]));

			if ((Bukkit.getServer().getWorld(s2[0]).getBlockAt(loc).getState() instanceof Sign))
			{
				Sign sign1 = (Sign)Bukkit.getServer().getWorld(s2[0]).getBlockAt(loc).getState();
				
				sign2.add(sign1);
				sign22.put(loc, s2[4]);
				sign222.put(loc, s2[5]);
			}
		}
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		subchannel.equals("SomeSubChannel");
	}

	public static void sendPlayerToServer(Player p, String serverName)
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{
			out.writeUTF("Connect");
			out.writeUTF(serverName);
			p.sendPluginMessage(Main.getPlugin(), "BungeeCord", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void refresh()
	{
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				if (Bukkit.getOnlinePlayers().length != 0)
				{
					for (Sign sign : Main.sign2)
					{
						String line0 = Main.sign222.get(sign.getLocation());

						String[] ipDetails = Main.getPlugin().getConfig().getString(line0).split(":");

						Block a = Main.getAttachedBlock(sign.getBlock());

						String ip = ipDetails[0];
						int port = Integer.parseInt(ipDetails[1]);

						Main.c.setAddress(new InetSocketAddress(ip, port));
						
						
						
						try	
						{
							String serverMotd = Main.c.fetchData().getDescription();

							int online = Main.c.fetchData().getPlayers().getOnline();
							int max = Main.c.fetchData().getPlayers().getMax();

							String lD = Main.sign22.get(sign.getLocation());
							String lD2 = lD.replaceAll("%motd%", serverMotd);
							String lD3 = lD2.replaceAll("%cp%", "" + online);
						    String lD4 = lD3.replaceAll("%mp%", "" + max);
							String lD5 = lD4.replaceAll("null", " ");
							String lD6 = lD5.replaceAll("&", "§");
						
							
							String[] linesD;
							
							for (int i = 0; i < 4; i++)
							{
								linesD = lD6.split(",");

								if (linesD[i].contains("null")) System.out.println(""); else {
									sign.setLine(i, linesD[i]);
								}
								sign.update();
							}

							for (String s : Main.getPlugin().getConfig().getStringList("MOTD"))
							{
								String[] s2 = s.split(" ");

								if (serverMotd.contains(s2[0]))
								{
									if (s2[1].contains(":"))
									{
										String[] sitmID = s2[1].split(":");

										int itmID = Integer.parseInt(sitmID[0]);
										int itmD = Integer.parseInt(sitmID[1]);

										a.setType(Material.getMaterial(itmID));
										a.setData((byte)itmD);
										break;
									}

									int itmID = Integer.parseInt(s2[1]);
									a.setType(Material.getMaterial(itmID));
									break;
								}
							}
						} catch (ConnectException e) {
							System.out.println(line0 + " is offline!");
						} catch (UnknownHostException e) {
							System.out.println("Can not ping " + line0);
						} catch (IOException e) {
							System.out.println("Can not ping " + line0);
						}
					}
				}
			}
		}
		, 30L, 30L);
	}

	public static Block getAttachedBlock(Block b)
	{
		MaterialData m = b.getState().getData();
		BlockFace face = BlockFace.DOWN;
		if ((m instanceof Attachable))
		{
			face = ((Attachable)m).getAttachedFace();
		}
		return b.getRelative(face);
	}

	public static Plugin getPlugin() {
		return p;
	}
}