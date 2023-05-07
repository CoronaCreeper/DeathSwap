package pl.deathswap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class GameLogic implements CommandExecutor, Listener, Runnable {

    static int leftTime = new Random().nextInt(250) + 50;
    static boolean isEnabled = false;
    static int currentSwap = 0;
    static String prefix = ChatColor.GRAY+"["+ChatColor.GOLD+"DeathSwap"+ChatColor.GRAY+"] ";


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        isEnabled = !isEnabled;
        Player p = (Player) commandSender;
        p.sendMessage(ChatColor.GRAY+"DeathSwap jest " + (isEnabled ?  ChatColor.GREEN+"włączony" : ChatColor.RED+"wyłączony"));
        return false;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(v->v.sendActionBar(Component.text(ChatColor.GOLD+"Plugin wykonał "+ChatColor.RED+"CoronaCreeper#4884"+ChatColor.GOLD+" dla "+ChatColor.DARK_PURPLE+"StressRoom")));
        if(!isEnabled) return;
        updateBossbar();
        leftTime--;
        System.out.println(leftTime);
        if (leftTime == 0) {
            swap();
            return;
        }
        if(leftTime < 10) {
            Bukkit.broadcast(Component.text(prefix+ChatColor.RED+"Pozostało "+ChatColor.GOLD+leftTime+ChatColor.RED+" sekund!"));
            Bukkit.getOnlinePlayers().forEach(v->v.playSound(v.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1));
        }
    }

    void swap() {
        List<Player> players = leftPlayers();
        Collections.shuffle(players);
        HashMap<Player, Location> swaps = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            swaps.put(players.get(i), players.get((i + 1) % players.size()).getLocation());
        }

        System.out.println(swaps);
        swaps.forEach(Entity::teleport);
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GRAY+"Zmiana!");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }

        leftTime = new Random().nextInt(250) + 50;
        currentSwap++;
        if(currentSwap == 4) {
            Bukkit.getOnlinePlayers().forEach(v->v.showTitle(Title.title(Component.text(ChatColor.GOLD+"Ogłoszenie!"), Component.text(ChatColor.GRAY+"Od teraz można robić pułapki!"))));
        }
        Bukkit.getOperators().stream().filter(v->v.isOnline()).forEach(v->v.getPlayer().sendMessage(ChatColor.GRAY+"Nastepna zmiana za "+ChatColor.RED+leftTime+ChatColor.GRAY+" sekund!"));


        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            assert onlinePlayer.getWorldBorder() != null;
            onlinePlayer.getWorldBorder().setSize(onlinePlayer.getWorldBorder().getSize()-50);
        }
    }

    void updateBossbar() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setPlayerListHeaderFooter("  "+ChatColor.GREEN+"Pozostało "+ChatColor.RED+leftPlayers().size()+ChatColor.GREEN+" graczy!", "");
            p.playerListName(Component.text((leftPlayers().contains(p) ? ChatColor.GREEN : ChatColor.GRAY)+p.getName()));
        }
    }

    static List<Player> leftPlayers() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> !p.getGameMode().equals(GameMode.SPECTATOR) && !p.isDead()).collect(Collectors.toList());
    }

    void tryEndGame(Player pl ) {
        if(!isEnabled) return;
        int leftPlayers = leftPlayers().size();
        Bukkit.broadcast(Component.text(prefix+ChatColor.RED+pl.getName()+ChatColor.GOLD+" wyeliminowany! Pozostało "+ChatColor.RED+leftPlayers+ChatColor.GOLD+" graczy!"));
        if(leftPlayers == 1) {
            Player p = leftPlayers().get(0);
            if(p == null) return;
            Bukkit.broadcast(Component.text(prefix+ChatColor.GOLD+"Wygrał gracz "+ChatColor.RED+p.getName()));
            Bukkit.getOnlinePlayers().forEach(v->v.playSound(v.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1));
            Bukkit.getOnlinePlayers().forEach(v->v.showTitle(Title.title(Component.text(ChatColor.GOLD+"Wygrał gracz "+ChatColor.RED+p.getName()), Component.text(ChatColor.GRAY+"Gratulacje!"))));
            Bukkit.getOnlinePlayers().forEach(v->v.sendActionBar(Component.text(ChatColor.GOLD+"Wygrał gracz "+ChatColor.RED+p.getName())));
            Bukkit.getOnlinePlayers().forEach(v->v.setGameMode(GameMode.SPECTATOR));
            isEnabled = false;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getPlayer().getLocation().getWorld().spawn(event.getPlayer().getLocation(), LightningStrike.class);
        tryEndGame(event.getPlayer());
        event.deathMessage(Component.text(""));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(!isEnabled) return;
        event.getPlayer().sendMessage(ChatColor.GRAY+"Zginąłeś! Zostajesz w trybie "+ChatColor.GOLD+"obserwatora");
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;
        event.setCancelled(true);
        event.getDamager().sendMessage(ChatColor.GRAY+"Nie możesz zadawać obrażeń graczom!");
    }

    @EventHandler
    public void onLoginTry(AsyncPlayerPreLoginEvent event) {
        if(!isEnabled) return;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Gra już się rozpoczęła!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text(ChatColor.GRAY+"["+ChatColor.AQUA+"+"+ChatColor.GRAY+"] "+ChatColor.GOLD+event.getPlayer().getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(ChatColor.GRAY+"["+ChatColor.RED+"-"+ChatColor.GRAY+"] "+ChatColor.GOLD+event.getPlayer().getName()));
        tryEndGame(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.GOLD+event.getPlayer().getName()+ChatColor.GRAY+" > "+ChatColor.WHITE+event.getMessage());
    }
}
