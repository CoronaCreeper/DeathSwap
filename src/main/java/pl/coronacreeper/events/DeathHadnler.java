package pl.coronacreeper.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.coronacreeper.Main;

import static pl.coronacreeper.Main.prefix;

public class DeathHadnler implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!Main.isGameRun()) return;
        Main.timer.cancelTasks(Main.instance);
        Main.gameRun = false;

        event.setCancelled(true);

        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(prefix + ChatColor.GREEN+"Gra zakończona! "+event.getPlayer().getName()+" przegrał!");
            players.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            players.getInventory().clear();
            players.setHealth(20d);
            players.setFoodLevel(20);
        }
    }
}