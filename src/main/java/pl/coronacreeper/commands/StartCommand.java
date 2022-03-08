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

import static pl.coronacreeper.Main.instance;
import static pl.coronacreeper.Main.prefix;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) return false;
        if(Main.isGameRun()) {
            p.sendMessage(prefix+ ChatColor.RED+"Gra jest włączona!");
            return false;
        }
        if(!(Bukkit.getOnlinePlayers().size() ==2)) {
            p.sendMessage(prefix+ChatColor.RED+"Nie ma wystarczającej liczby osób na serwerze");
            return false;
        }

        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(prefix + ChatColor.GREEN+"Gra została rozpoczęta!");
            players.teleport(p.getWorld().getSpawnLocation());
            players.getInventory().clear();
            players.setHealth(20d);
            players.setFoodLevel(20);
        }

        Main.gameRun = true;


        Main.timer.cancelTasks(instance);
        Main.timer.runTaskTimer(instance, new GameTimer(), 0, 20);

        int min = 30;
        int max = 300;

        GameTimer.secondsToSwap = (int)Math.floor(Math.random()*(max-min+1)+min);

        return false;
    }
}
