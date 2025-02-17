package lee.code.crackedblocks.listeners;

import com.cryptomorin.xseries.XMaterial;
import lee.code.crackedblocks.CrackedBlocks;
import lee.code.crackedblocks.files.defaults.Lang;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractCrackedBlockListener implements Listener {

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        CrackedBlocks plugin = CrackedBlocks.getPlugin();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            Material handItem = plugin.getPU().getHandItemType(e.getPlayer());
            Material checkerItem = plugin.getData().getCheckerItem();

            if (!handItem.equals(checkerItem)) return;
            e.setCancelled(true);

            //click delay
            if (plugin.getData().getPlayerClickDelay(e.getPlayer().getUniqueId())) return;
            else plugin.getPU().addPlayerClickDelay(e.getPlayer().getUniqueId());

            Block block = e.getClickedBlock();
            if (block != null) {
                if (plugin.getData().getBlocks().contains(block.getType())) {
                    int maxDurability = plugin.getData().getBlockMaxDurability(e.getClickedBlock().getType());
                    if (e.getClickedBlock().hasMetadata("hits")) {
                        int currentDurability = e.getClickedBlock().getMetadata("hits").get(0).asInt();
                        e.getPlayer().sendMessage(Lang.PREFIX.getConfigValue(null) + Lang.CHECKER_INFO_MESSAGE.getConfigValue(new String[] {
                                plugin.getPU().formatMatFriendly(XMaterial.matchXMaterial(e.getClickedBlock().getType()).parseItem()), String.valueOf(maxDurability - currentDurability), String.valueOf(maxDurability) }));
                    } else {
                        e.getPlayer().sendMessage(Lang.PREFIX.getConfigValue(null) + Lang.CHECKER_INFO_MESSAGE.getConfigValue(new String[] {
                                plugin.getPU().formatMatFriendly(XMaterial.matchXMaterial(e.getClickedBlock().getType()).parseItem()), String.valueOf(maxDurability), String.valueOf(maxDurability) }));
                    }
                }
            }
        }
    }
}
