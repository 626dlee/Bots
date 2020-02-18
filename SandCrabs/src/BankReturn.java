import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class BankReturn extends Task<ClientContext> {
    Area firstArea = new Area(new Tile(1736, 3472, 0), new Tile(1734, 3466, 0));
    Area secondArea = new Area(new Tile(1743, 3474, 0), new Tile(1744, 3469, 0));
    Random rand = new Random();
    Tile sandCrabSpot = new Tile(1749,3469,0); //Change 0's to the sand crab spot position
    Tile bankTile = new Tile(1719, 3465, 0);
    private List<Area> bankRoute = new ArrayList<Area>();
    private List<Area> returnRoute = new ArrayList<Area>();


    public BankReturn (ClientContext ctx) {
        super(ctx);
        returnRoute.addAll(Arrays.asList(firstArea, secondArea));

    }

    @Override
    public boolean activate() {
        return ctx.players.local().tile().compareTo(bankTile) == 0 && ctx.inventory.isFull();
    }

    private void goingBack() {
        if (sandCrabSpot.matrix(ctx).inViewport()) {
            ctx.camera.turnTo(sandCrabSpot);
            sandCrabSpot.matrix(ctx).click();
        }
        else if (sandCrabSpot.matrix(ctx).onMap()){
            ctx.camera.turnTo(sandCrabSpot);
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return sandCrabSpot.matrix(ctx).inViewport();
                }

            },800 + rand.nextInt(800), 3);
            sandCrabSpot.matrix(ctx).click();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().tile().compareTo(sandCrabSpot) == 0 && ctx.players.local().healthBarVisible();
                }
            },500 + rand.nextInt(900), 3);
        }

        else {
            ctx.movement.step(sandCrabSpot);
            goingBack();

        }
    }

    @Override
    public void execute() {
        System.out.println("Bank_Return activated");
        for (Area area : returnRoute) {
            if (area.getRandomTile().matrix(ctx).inViewport()) {
                area.getRandomTile().matrix(ctx).click();
            }
            else if (area.getRandomTile().matrix(ctx).onMap()) {
                ctx.movement.step(area.getRandomTile().matrix(ctx));
                ctx.camera.turnTo(area.getRandomTile());
            }
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return area.contains(ctx.players.local().tile()) && !ctx.players.local().inMotion();
                }
            }, 1300 + rand.nextInt(900),4);
        }
        goingBack();
        if (!ctx.players.select().within(1).select(new Filter<Player>() {
            public boolean accept(Player player) {
                //filter out yourself
                return !player.equals(ctx.players.local());
            }
        }).isEmpty()) {
            System.out.println("Crasher alert");
            if (rand.nextBoolean()) {
                ctx.input.send("hey man");
                ctx.input.send("{VK_ENTER}");
            }
            Condition.sleep(200 + rand.nextInt(400));
            ctx.input.send("i was just resetting");
            ctx.input.send("{VK_ENTER}");
            Condition.sleep(100 + rand.nextInt(500));
            ctx.input.send("if you can hop, i'd appreciate it!");
            ctx.input.send("{VK_ENTER}");
            Condition.sleep( rand.nextInt(200));
            ctx.input.send(":)");
            ctx.input.send("{VK_ENTER}");
        }
    }
}
