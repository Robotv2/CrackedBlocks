package lee.code.crackedblocks.listeners;

import com.cryptomorin.xseries.XMaterial;
import lee.code.crackedblocks.CrackedBlocks;
import lee.code.crackedblocks.events.CustomBlockBreakEvent;
import lee.code.crackedblocks.files.defaults.Settings;
import lee.code.crackedblocks.files.defaults.Values;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Arrays;

public class EntityExplodeListener implements Listener {

    private final CrackedBlocks plugin = CrackedBlocks.getPlugin();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExplode(EntityExplodeEvent event) {

        if(event.isCancelled()) {
            return;
        }

        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            return;
        }

        event.blockList().removeIf(block -> plugin.getData().getBlocks().contains(block.getType()));

        final Location location = event.getLocation();
        final World world = location.getWorld();

        if(world == null) {
            return;
        }

        final int r = 1;
        for (int x = r * -1; x <= r; x++) {
            for (int y = r * -1; y <= r; y++) {
                for (int z = r * -1; z <= r; z++) {

                    final Block block = world.getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                    if (plugin.getData().getBlocks().contains(block.getType())) {

                        if (block.getType() == XMaterial.BEDROCK.parseMaterial()) {

                            if (block.getLocation().getBlockY() >= Values.DISABLED_BEDROCK_ROOF.getConfigValue() && plugin.getData().getDisabledBedrockRoofWorlds().contains(world.getName())) {
                                return;
                            }

                            else if (block.getLocation().getBlockY() <= Values.DISABLED_BEDROCK_FLOOR.getConfigValue() && plugin.getData().getDisabledBedrockFloorWorlds().contains(world.getName())) {
                                return;
                            }
                        }

                        if (Settings.WATER_PROTECTION.getConfigValue() && !this.hasWaterProtection(block)) {
                            Bukkit.getServer().getPluginManager().callEvent(new CustomBlockBreakEvent(block));
                        }
                    }
                }
            }
        }
    }

    private boolean hasWaterProtection(Block block) {
        final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN, BlockFace.SELF};
        return Arrays.stream(faces)
                .map(block::getRelative)
                .anyMatch(relative -> relative.getType().equals(XMaterial.WATER.parseMaterial()));
    }
}
