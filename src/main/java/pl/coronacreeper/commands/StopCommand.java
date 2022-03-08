package pl.coronacreeper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.coronacreeper.GameTimer;
import pl.coronacreeper.Main;

import static pl.coronacreeper.Main.gameRun;
import static pl.coronacreeper.Main.prefix;

public class StopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player)sender;
        if(!Main.isGameRun()) {
            p.sendMessage(prefix+ ChatColor.RED+"Gra jest wyłączona!");
            return false;
        }

        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(prefix + ChatColor.RED + "Wymuszono zakończenie gry przez administratora " + ChatColor.GREEN + p.getName());
            players.teleport(p.getWorld().getSpawnLocation());
            players.getInventory().clear();
            players.setHealth(20d);
            players.setFoodLevel(20);
        }

        Main.timer.cancelTasks(Main.instance);
        gameRun = false;

        return false;
    }
}
