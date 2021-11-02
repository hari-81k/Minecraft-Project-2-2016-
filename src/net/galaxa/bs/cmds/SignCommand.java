package net.galaxa.bs.cmds;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.galaxa.bs.Main;

public class SignCommand implements CommandExecutor
{
	public static HashMap<String, String> signC = new HashMap<String, String>();
	public static ArrayList<String> signR = new ArrayList<String>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can not run this command in console");
			return false;
		}

		Player p = (Player)sender;

		if (cmd.getName().equalsIgnoreCase("bsign")) 
		{
			if(p.isOp() || p.hasPermission("bs.admin"))
			{
				if(args.length == 2)
				{
					if(args[0].equalsIgnoreCase("create"))
					{
						if(!Main.getPlugin().getConfig().contains(args[1] + ""))
						{
							p.sendMessage(ChatColor.RED + "This server doesn't contain in the config!");
							return true;
						}
						else
						{
							p.sendMessage(ChatColor.GREEN + "Place the sign and fill it out, if you want a line blank put 'null'");
							signC.put(p.getName(), args[1]);

							p.getInventory().addItem(new ItemStack(Material.SIGN));
							return true;
						}
					}
				}
				else if(args.length == 1)
				{
					if(args[0].equalsIgnoreCase("reload"))
					{
						Main.getPlugin().reloadConfig();
						
						Main.setupSigns();
						
						p.sendMessage(ChatColor.GREEN + "Successfully reloaded plugin");
						
						return true;
					}
					else if(args[0].equalsIgnoreCase("remove"))
					{
						p.sendMessage(ChatColor.GREEN + "Break the sign you want to remove");
						signR.add(p.getName());
						return true;
					}
				}
			} else p.sendMessage(ChatColor.RED + "You don't have permission to do this!");
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
