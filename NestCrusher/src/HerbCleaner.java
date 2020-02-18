import org.powerbot.script.rt4.ClientContext;

import org.powerbot.script.Condition;

import org.powerbot.script.rt4.Item;

import java.util.Random;

public class HerbCleaner extends Task<ClientContext> {
    private int[] grimyHerbIDs = {207};
    private int vialOfWater = 227;
    Random rand = new Random();
    public HerbCleaner(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        if (!ctx.inventory.select().id(grimyHerbIDs).isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void execute() {
        cleanHerbs();
    }

    private void cleanHerbs() {
        ctx.input.speed(300 + rand.nextInt(117));

        while (!ctx.inventory.select().id(grimyHerbIDs).isEmpty()) {
            for (Item herb : ctx.inventory.select().id(grimyHerbIDs)) {
                herb.click();
                Condition.sleep(50 + rand.nextInt(100));
            }
        }
        ctx.input.speed(100);
    }
}
