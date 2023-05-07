package pl.deathswap;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathSwap extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("deathswap").setExecutor(new GameLogic());
        getServer().getPluginManager().registerEvents(new GameLogic(), this);
        if(!getServer().isHardcore()) {
            Bukkit.broadcast(Component.text(ChatColor.RED+"Plugin wymaga włączonego trybu hardcore!"));
        }
        Bukkit.getScheduler().runTaskTimer(this, new GameLogic(), 20, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
