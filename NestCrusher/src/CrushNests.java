
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Random;

public class CrushNests extends Task<ClientContext> {

    private int[] uncrushedNestIDs = {5075, 9735};
    private int pestleAndMortarId = 233;
    Random rand = new Random();

    public CrushNests(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        if (!ctx.inventory.select().id(pestleAndMortarId).isEmpty() && !ctx.inventory.select().id(uncrushedNestIDs).isEmpty() && !ctx.bank.opened()) {
            System.out.println("Crushing nests");
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void execute() {
        crushNests();
    }

    private void crushNests() {
        Item lastNest = ctx.inventory.select().id(uncrushedNestIDs).reverse().poll();
        Item pestle = ctx.inventory.select().id(pestleAndMortarId).poll();
        ctx.input.speed(350);
        while (!ctx.inventory.select().id(uncrushedNestIDs).isEmpty()) {
            lastNest.click();
//            Condition.wait(new Condition.Check() {
//                @Override
//                public boolean poll() {
//                    return ctx.inventory.selectedItem() == lastNest;
//                }
//            }, 50 + rand.nextInt(100), 3);
            pestle.click();
//            Condition.wait(new Condition.Check() {
//                @Override
//                public boolean poll() {
//                    return ctx.players.local().animation() == -1;
//                }
//            }, 500 + rand.nextInt(500), 3);
        }
        ctx.input.speed(100);
    }


}
