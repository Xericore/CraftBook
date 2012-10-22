package com.sk89q.craftbook.gates.logic;

import com.sk89q.craftbook.ic.*;
import org.bukkit.Server;
import org.bukkit.block.Sign;

/**
 * @author Silthus
 */
public class LowPulser extends Pulser {

    public LowPulser(Server server, Sign block, ICFactory factory) {

        super(server, block, factory);
    }

    @Override
    public String getTitle() {

        return "Low Pulser";
    }

    @Override
    public String getSignTitle() {

        return "LOW PULSER";
    }

    @Override
    protected boolean getInput(ChipState chip) {

        return !chip.getInput(0);
    }

    public static class Factory extends AbstractICFactory implements RestrictedIC {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(Sign sign) {

            return new LowPulser(getServer(), sign, this);
        }
    }
}
