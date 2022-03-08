package pl.coronacreeper;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import pl.coronacreeper.commands.StartCommand;
import pl.coronacreeper.commands.StopCommand;
import pl.coronacreeper.events.DeathHadnler;
import pl.coronacreeper.events.JoinHandler;

import java.util.Timer;

public final class Main extends JavaPlugin {

    @Getter
    public static BukkitScheduler timer = Bukkit.getServer().getScheduler();

    @Getter
    public static Plugin instance;

    @Getter
    public static boolean gameRun;

    @Getter
    public static String prefix = ChatColor.GRAY+"["+ChatColor.RED+"Death"+ChatColor.GOLD+"Swap"+ChatColor.GRAY+"] "+ChatColor.RESET;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JoinHandler(), this);
        getServer().getPluginManager().registerEvents(new DeathHadnler(), this);
        getCommand("start").setExecutor(new StartCommand());
        getCommand("end").setExecutor(new StopCommand());
        gameRun = false;
        instance = this;
    }

    @Override
    public void onDisable() {
    }
}
