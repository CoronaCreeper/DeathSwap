package pl.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathSwap extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("deathswap").setExecutor(new GameLogic());
        getServer().getPluginManager().registerEvents(new GameLogic(), this);
        Bukkit.getScheduler().runTaskTimer(this, new GameLogic(), 20, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
