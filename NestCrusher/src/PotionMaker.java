import org.powerbot.script.rt4.ClientContext;

import org.powerbot.script.Condition;

import org.powerbot.script.rt4.Item;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Random;

public class PotionMaker extends Task<ClientContext> {
    private int[] cleanHerb = {255,257};
    private int vialOfWater = 227;
    Random rand = new Random();
    public PotionMaker(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        if (!ctx.inventory.select().id(cleanHerb).isEmpty() && !ctx.inventory.select().id(vialOfWater).isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void execute() {
        makePotion();
    }

    private void makePotion() {
        while (!ctx.widgets.component(270, 13).visible()) {
            ctx.inventory.select().id(vialOfWater).reverse().poll().click();
//        Condition.sleep(100 + rand.nextInt(100));
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.inventory.selectedItem() == ctx.inventory.select().id(vialOfWater).reverse().poll();
                }
            }, 100 + rand.nextInt(100), 3);
            ctx.inventory.select().id(cleanHerb).poll().click();
//        System.out.println("Widget visible?: " + ctx.widgets.component(270,13).visible());
//        Condition.sleep(500 + rand.nextInt(100));
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.widgets.component(270, 13).visible();
                }
            }, 400 + rand.nextInt(300), 5);
        }
//        ctx.widgets.component(270, 13).click();
//        System.out.println("Widget visible?: " + ctx.widgets.component(270,13).visible());
        if (ctx.widgets.component(270, 13).visible()) {
            ctx.input.send("{VK_SPACE down}");
            Condition.sleep(35 + rand.nextInt(30));
            ctx.input.send("{VK_SPACE up}");
            Condition.sleep(35 + rand.nextInt(30));
        }
        ctx.input.speed(500+ rand.nextInt(200));
        ctx.input.move(ctx.input.getLocation().x - (200+rand.nextInt(100)), ctx.input.getLocation().y - (50 + rand.nextInt(50)) );
        ctx.input.speed(100);
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.inventory.select().id(cleanHerb).isEmpty() && ctx.inventory.select().id(vialOfWater).isEmpty();
            }
        }, 500 + rand.nextInt(300),30);
//        ctx.bank.select().poll().hover();
    }

}
