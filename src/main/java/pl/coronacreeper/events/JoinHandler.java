package pl.coronacreeper.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.coronacreeper.Main;

import static pl.coronacreeper.Main.prefix;

public class JoinHandler implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(Main.isGameRun()) {
            event.getPlayer().kickPlayer(Main.prefix+ChatColor.RED+" nie można dołączyć podczas trwania gry!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(Main.isGameRun()) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(prefix + ChatColor.RED + "Gra została zakończona poniważ gracz opuścił serwer!");
                players.teleport(players.getWorld().getSpawnLocation());
                players.getInventory().clear();
                players.setHealth(20d);
                players.setFoodLevel(20);
            }
            Main.gameRun = false;
        }
    }
}