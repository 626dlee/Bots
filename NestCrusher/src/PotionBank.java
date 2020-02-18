import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;

public class PotionBank extends Task<ClientContext> {

    Random rand = new Random();
//    final int[] bounds = {0, 20, -64, 0, -32, 32};
//    final int[] bounds = {144, 116, -100, -24, 224, 108};
//final int[] bounds = {144, 116, -92, -24, 148, 36};
final int[] bounds = {-36, 32, -100, -40, 12, 116};
    private int cleanedHerbID = 257;//ranarr
//    private int cleanedHerbID = 255;//harralander
    private int vialOfWater = 227;
    private int[] grimyHerb = {205};

//    private int finishedPotionID= 97;
private int finishedPotionID= 99;
    private int geBooth = 10060;

    public PotionBank(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        if (ctx.inventory.select().id(cleanedHerbID).isEmpty() || ctx.inventory.select().id(vialOfWater).isEmpty()) {
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
            if(rand.nextBoolean()) {
                ctx.bank.depositInventory();
            }
            else {
//                ctx.inventory.select().id(cleanedHerbID).shuffle().poll().click();
                ctx.inventory.select().id(finishedPotionID).shuffle().poll().click();
            }

            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.inventory.select().count() == 1;
                }
            },200 + rand.nextInt(300),3);
//            ctx.bank.withdraw(uncrushedNestID, Bank.Amount.ALL);
            ctx.bank.select().id(vialOfWater).poll().click();
            Condition.sleep(35 + rand.nextInt(37));
            ctx.bank.select().id(cleanedHerbID).poll().click();
//            ctx.bank.select().id(cleanedHerbID).poll().click();
//            Condition.sleep(35 + rand.nextInt(37));
//            ctx.bank.select().id(vialOfWater).poll().click();
//            ctx.bank.select().id(grimyHerb).poll().click();
//            ctx.bank.select().id(goatHornID).poll().click();
//            ctx.bank.withdraw(goatHornID, Bank.Amount.ALL);
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.inventory.select().count() == 28;
                }
            },200 + rand.nextInt( 300), 10);
        }
//        if (!ctx.inventory.select().id(grimyHerb).isEmpty() && ctx.bank.opened()) {
        if (!ctx.inventory.select().id(cleanedHerbID).isEmpty() && !ctx.inventory.select().id(vialOfWater).isEmpty() && ctx.bank.opened()) {
            ctx.input.speed(400 + rand.nextInt(150));
            ctx.bank.close();
            ctx.input.speed(100);
        }
        else if (ctx.bank.select().id(cleanedHerbID).isEmpty() || ctx.bank.select().id(vialOfWater).isEmpty()){
//        else if (ctx.inventory.select().id(grimyHerb).isEmpty()){
            System.out.println("Not enough birds' nests, stopping script now");
            ctx.controller.stop();
        }
        ctx.input.speed(100);

    }


}
