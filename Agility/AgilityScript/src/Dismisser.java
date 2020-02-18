import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.Random;
//import org.powerbot.script

public class Dismisser extends Task<ClientContext> {
    Random rand;
    public Dismisser(ClientContext ctx) {
        super(ctx);
    }
    private Npc randomNpc;
    @Override
    public boolean activate() {
        randomNpc = ctx.npcs.select().within(3.0).select(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                return npc.overheadMessage().contains(ctx.players.local().name());
            }

        }).poll();

        /* a random npc is present, dismiss them */
        if (randomNpc.valid()) {

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void execute() {
        System.out.println("Dismissing random NPC.");
        String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
        ctx.input.speed(320);
        Condition.sleep(1400 + rand.nextInt(1350));
        if (randomNpc.interact(action)) {
            ctx.input.speed(100);
            Condition.sleep(3000);
        }

    }
}
