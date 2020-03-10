package com.sk89q.craftbook.util;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.CraftBookPlayer;
import com.sk89q.craftbook.bukkit.util.CraftBookBukkitUtil;
import com.sk89q.craftbook.mechanics.Elevator;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ElevatorUtil
{
    public static Elevator.Direction isLift(ChangedSign sign) {
        // if you were really feeling frisky this could definitely
        // be optomized by converting the string to a char[] and then
        // doing work

        if (sign.getLine(1).equalsIgnoreCase("[Lift Up]")) return Elevator.Direction.UP;
        if (sign.getLine(1).equalsIgnoreCase("[Lift Down]")) return Elevator.Direction.DOWN;
        if (sign.getLine(1).equalsIgnoreCase("[Lift]")) return Elevator.Direction.RECV;
        return Elevator.Direction.NONE;
    }

    public static boolean isValidLift(ChangedSign start, ChangedSign stop) {

        if (start == null || stop == null) return true;
        if (start.getLine(2).toLowerCase(Locale.ENGLISH).startsWith("to:")) {
            try {
                return stop.getLine(0).equalsIgnoreCase(RegexUtil.COLON_PATTERN.split(start.getLine(2))[0].trim());
            } catch (Exception e) {
                start.setLine(2, "");
                return false;
            }
        } else return true;
    }

    public static Elevator.Direction getVerticalDirection(Location from, Location to)
    {
        if(from.getY() < to.getY())
            return Elevator.Direction.UP;
        else if (from.getY() > to.getY())
            return Elevator.Direction.DOWN;
        else
            return Elevator.Direction.NONE;
    }

    public static boolean isSolidBlockOccludingMovement(Player p, Elevator.Direction direction) {
        int verticalDistance = direction == Elevator.Direction.UP ? 2 : -1;
        return p.getLocation().clone().add(0, verticalDistance, 0).getBlock().getType().isSolid();
    }

    public static void teleportFinish(CraftBookPlayer player, Block destination, BlockFace shift) {
        // Now, we want to read the sign so we can tell the player
        // his or her floor, but as that may not be avilable, we can
        // just print a generic message
        ChangedSign info = null;
        if (!SignUtil.isSign(destination)) {
            if (Tag.BUTTONS.isTagged(destination.getType())) {
                Switch attachable = (Switch) destination.getBlockData();
                if (SignUtil.isSign(destination.getRelative(attachable.getFacing().getOppositeFace(), 2)))
                    info = CraftBookBukkitUtil.toChangedSign(destination.getRelative(attachable.getFacing().getOppositeFace(), 2));
            }
            if (info == null)
                return;
        } else
            info = CraftBookBukkitUtil.toChangedSign(destination);
        String title = info.getLines()[0];
        if (!title.isEmpty()) {
            player.print(player.translate("mech.lift.floor") + ": " + title);
        } else {
            player.print(shift.getModY() > 0 ? "mech.lift.up" : "mech.lift.down");
        }
    }
}
