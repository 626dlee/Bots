import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;

public class UseBank extends Task<ClientContext> {

    Random rand = new Random();
    final int[] bounds = {0, 20, -64, 0, -32, 32};
    private int uncrushedNestID = 5075;
    private int crushedNestID = 6693;
    private int goatHornID = 9735;
    private int geBooth = 10060;

    public UseBank(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        if (ctx.inventory.select().id(uncrushedNestID).isEmpty()) {
//        if (ctx.inventory.select().id(goatHornID).isEmpty()) {
            System.out.println("Using bank");
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void execute() {

        useBank();

    }

    private void useBank() {
        ctx.input.speed(520);
        while (!ctx.bank.opened()) {
            GameObject bank = ctx.objects.select().id(geBooth).nearest().poll();
            bank.bounds(bounds);
            bank.click();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.bank.opened();
                }
            }, 300 + rand.nextInt(500), 3);
        }
        if (ctx.bank.opened()) {
            if (rand.nextBoolean()) {
                ctx.bank.depositInventory();
            }
            else {
                ctx.inventory.select().id(crushedNestID).shuffle().poll().click();
            }

            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.inventory.select().count() == 1;
                }
            },200 + rand.nextInt(300),3);
//            if (ctx.bank.)
//            ctx.bank.withdraw(uncrushedNestID, Bank.Amount.ALL);
            ctx.bank.select().id(uncrushedNestID).poll().click();
//            ctx.bank.select().id(goatHornID).poll().click();
//            ctx.bank.withdraw(goatHornID, Bank.Amount.ALL);
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.inventory.select().count() == 28;
                }
            },200 + rand.nextInt( 300), 10);
        }
        if (!ctx.inventory.select().id(uncrushedNestID).isEmpty() && ctx.bank.opened()) {
            ctx.bank.close();
            ctx.input.speed(100);
        }
        else if (ctx.inventory.select().id(uncrushedNestID).isEmpty()){
            System.out.println("Not enough birds' nests, stopping script now");
            ctx.controller.stop();
        }
        ctx.input.speed(100);

    }


}
