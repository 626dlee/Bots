import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.Area;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.Bank.Amount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class BankFood extends Task<ClientContext> {
    Area firstArea = new Area(new Tile(1734, 3474, 0), new Tile(1736, 3469, 0));
    Area secondArea = new Area(new Tile(1724, 3467, 0), new Tile(1727, 3462, 0));
    BankReturn returnToBank;
    Random rand = new Random();

    private List<Area> bankRoute = new ArrayList<Area>();


    private int[] foodID = {361};

    public BankFood (ClientContext ctx) {
        super(ctx);
        bankRoute.addAll(Arrays.asList(firstArea, secondArea));
        returnToBank = new BankReturn(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(foodID).isEmpty() && ctx.skills.level(Constants.SKILLS_HITPOINTS) <= 50;
    }

    private void getFood() {
        if (ctx.bank.opened()) {
            if (!ctx.inventory.select().isEmpty()) {
                ctx.bank.depositInventory();
            }
            ctx.bank.withdraw(foodID[0], Amount.ALL);
            if (returnToBank.activate()) {
                returnToBank.execute();
            }
        }
        else {
            ctx.bank.open();
            getFood();
        }
    }

    @Override
    public void execute() {
        System.out.println("Bank_Food activated");
        for (Area area : bankRoute) {
            if (area.getRandomTile().matrix(ctx).inViewport()) {
                area.getRandomTile().matrix(ctx).click();
            }
            else if (area.getRandomTile().matrix(ctx).onMap()) {
                ctx.camera.turnTo(area.getRandomTile());
                area.getRandomTile().matrix(ctx).click();
            }
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return area.contains(ctx.players.local().tile()) && !ctx.players.local().inMotion();
                }
            }, 500 + rand.nextInt(900),3);
        }
        if (ctx.bank.nearest().tile().matrix(ctx).onMap()) {
            if (ctx.bank.inViewport()) {
                ctx.bank.open();
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.bank.opened();
                    }
                }, 400 + rand.nextInt(700), 3);
                getFood();
            }
            else {
                ctx.camera.turnTo(ctx.bank.nearest());
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.bank.inViewport();
                    }

                },500 + rand.nextInt(500), 3);
                ctx.bank.open();
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.bank.opened();
                    }
                },500 + rand.nextInt(600), 3);
                getFood();
            }
        }
        else {

            ctx.movement.step(ctx.bank.nearest());
            getFood();
        }
    }



}
