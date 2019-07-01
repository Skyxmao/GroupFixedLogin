package me.xmao.GroupFixedLogin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GroupFixedLogin extends JavaPlugin{
	public static FileConfiguration config = null;
	 public void onEnable()
	 {
		 saveDefaultConfig();
		 GroupFixedLogin.config = getConfig();
		 Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		 Bukkit.getLogger().info("载入固定地点 - 作者:XMAO");
	 }
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	 {
	    if (cmd.getName().equalsIgnoreCase("fixedlogin"))
	    {
	      if (!((sender.isOp()) || (sender.hasPermission("fixedlogin.admin"))))
	      {
	    	  sender.sendMessage("§b[§e固定上线地点§b]§d你没有权限这样做");
	    	  return true;
	      }
	      if (args.length == 0) {
	          sender.sendMessage("§a§l§m---§b§l§m---§c§l§m---§b§l FixedLogin v1.0 §c§l§m---§b§l§m---§a§l§m---");
	          sender.sendMessage("§b§l作者:XMAO,_YanHuang");
	          sender.sendMessage("§b§l/fixedlogin set <权限组> §7- §b§l设置权限组的登陆地点为当前位置");
	          sender.sendMessage("§b§l/fixedlogin remove <ID> §7- §b§l删除某个地点");
	          sender.sendMessage("§b§l/fixedlogin list §7- §b§l查看所有设置的地点列表");
	          sender.sendMessage("§b§l/fixedlogin reload §7- §b§l重新载入配置");
	          return true;
	      }
	      if (args.length == 1) {
	    	  if(args[0].equalsIgnoreCase("list")) {
	    		  ArrayList positions = (ArrayList) GroupFixedLogin.config.getList("localtion");
	    		  if(positions == null) {
	    			  sender.sendMessage("§b[§e固定上线地点§b]§d还没有设置地点");
	    		  }else {
	    			  int i = 0;
	    			  for(Object v:positions) {
	    				  i++;
	    				  Map value = (Map) v;
	    				  sender.sendMessage("§b==========[§e" + i + "§b]==========");
	    				  sender.sendMessage("§b权限: §dfixedlogin." + value.get("group"));
	    				  sender.sendMessage("§bX: §d" + value.get("X"));
	    				  sender.sendMessage("§bY: §d" + value.get("Y"));
	    				  sender.sendMessage("§bY: §d" + value.get("Z"));
	    				  sender.sendMessage("§bYaw: §d" + value.get("Yaw"));
	    				  sender.sendMessage("§bPitch: §d" + value.get("Pitch"));
	    			  }
	    		  }
	    	  }else if(args[0].equalsIgnoreCase("reload")) {
	    		  reloadConfig();
		    	  GroupFixedLogin.config = getConfig();
		    	  sender.sendMessage("§b[§e固定上线地点§b]§d重新载入成功");
	    	  }else {
	    		  sender.sendMessage("§b[§e固定上线地点§b]§d你输入的指令不正确");
	    	  }
	    	  return true;
	      }
	      
	      if (args.length == 2) {
	    	  if(args[0].equalsIgnoreCase("set")) {
	    		  Player player = (Player) sender;
	    		  Location position = player.getLocation();
	    		  String group = args[1];
	    		  
	    		 ArrayList positions = (ArrayList) GroupFixedLogin.config.getList("localtion");
	    		 if(positions == null) {
	    			 positions = new ArrayList();
	    		 }
	    		 
	    		 
	    		 boolean isAdded = false;
	    		 for(Object v:positions) {
	    			 Map value = (Map) v;
	    			 String group1 =  (String) value.get("group");
	    			 if((group1.equalsIgnoreCase(args[1]))) {
	    				 isAdded = true;
	    				 sender.sendMessage("§b[§e固定上线地点§b]§d这个权限已经存在了,请删除后重新添加");
	    				 return true;
	    			 }
	    		 }
	    		 if(isAdded) {
	    			 sender.sendMessage("§b[§e固定上线地点§b]§d这个权限已经存在了,请删除后重新添加");
	    			 return true;
	    		 }
	    		 
	    		 Map nowPlayerLocal =  new HashMap();
	    		 
	    		 nowPlayerLocal.put("group",args[1]);
	    		 nowPlayerLocal.put("world", position.getWorld().getName());
	    		 nowPlayerLocal.put("X", Double.valueOf(position.getX()));
	    		 nowPlayerLocal.put("Y", Double.valueOf(position.getY()));
	    		 nowPlayerLocal.put("Z", Double.valueOf(position.getZ()));
	    		 
	    		 nowPlayerLocal.put("Pitch", Float.valueOf(position.getPitch()));
	    		 nowPlayerLocal.put("Yaw", Float.valueOf(position.getYaw()));
	    	
	    		 
	    		 positions.add(nowPlayerLocal);
	    		 config.set("localtion", positions);
	    		 saveConfig(); 
	    		 
	    		 reloadConfig();
	    		 GroupFixedLogin.config = getConfig();
	    		 
	    		 sender.sendMessage("§b[§e固定上线地点§b]§d设置成功,请给玩家或者权限组 §bfixedlogin." + args[1]  +  "§d  的权限");
	    		 
	    	  }else if(args[0].equalsIgnoreCase("remove")){
	    		  int id = Integer.valueOf(args[1]);
	    		  ArrayList positions = (ArrayList) GroupFixedLogin.config.getList("localtion");
	    		  if(id<1 || id>positions.size()) {
	    			  sender.sendMessage("§b[§e固定上线地点§b]§d这个ID不存在");
	    			  return true;
	    		  }
	    		  
	    		  positions.remove(id - 1);
	    		  config.set("localtion", positions);
		    	  saveConfig(); 
		    	  reloadConfig();
		    	  GroupFixedLogin.config = getConfig();
		    	  sender.sendMessage("§b[§e固定上线地点§b]§d删除成功");
	    	  }else {
	    		  sender.sendMessage("§b[§e固定上线地点§b]§d你输入的指令不正确");
	    	  }
	    	  return true;
	      }
	    }
		return true;
	 }
}
