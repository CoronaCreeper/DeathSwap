package pl.coronacreeper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.TimerTask;

import static pl.coronacreeper.Main.prefix;

public class GameTimer extends TimerTask {

    public static int secondsToSwap;

    @Override
    public void run() {
        System.out.println(secondsToSwap);
        if(secondsToSwap<=10) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(prefix+ChatColor.RED+secondsToSwap+"s do końca");
            }
        }
        secondsToSwap = secondsToSwap-1;
        if(secondsToSwap == 0) makeSwap();
    }

    void makeSwap() {
        List<? extends Player> playerList = Bukkit.getOnlinePlayers().stream().toList();
        Player first = playerList.get(0);
        Player second = playerList.get(1);

        Location firstLoc = first.getLocation();
        Location secondLoc = second.getLocation();

        first.teleport(secondLoc);
        second.teleport(firstLoc);

        int min = 30;
        int max = 300;

        GameTimer.secondsToSwap = (int)Math.floor(Math.random()*(max-min+1)+min);
    }
}
