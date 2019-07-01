package me.xmao.GroupFixedLogin;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	@EventHandler(priority=EventPriority.HIGHEST)
	 public void onPlayerJoin(PlayerJoinEvent event)
	 {
		
		if(event.getPlayer().isOp()) return;
		ArrayList positions = (ArrayList) GroupFixedLogin.config.getList("localtion");
		if(positions == null) {
			 positions = new ArrayList();
		}
		
		for(Object v:positions) {
			Map value = (Map) v;
			String per = "fixedlogin." + value.get("group");
			if(event.getPlayer().hasPermission(per)) {
				double x =(double) value.get("X");
				double y =(double) value.get("Y");
				double z =(double) value.get("Z");
				
				World world = Bukkit.getWorld((String) value.get("world"));
				
				float Yaw =  Float.parseFloat(String.valueOf(value.get("Yaw")));
				float Pitch = Float.parseFloat(String.valueOf(value.get("Pitch"))) ;
				Location localtion = new Location(world,x,y,z, Yaw, Pitch);
				event.getPlayer().teleport(localtion);
				return;
			}
		}
	 }
}
