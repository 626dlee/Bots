
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import java.util.Random;

public class EatFood extends Task<ClientContext> {
    Random rand = new Random();
    private int oldHP;
    private int[] foodID = {361};
    public EatFood(ClientContext ctx) {
        super(ctx);

    }

    @Override
    public boolean activate() {
        return (ctx.skills.level(Constants.SKILLS_HITPOINTS) <= 50 && !ctx.inventory.select().id(foodID).isEmpty()) || (ctx.skills.level(Constants.SKILLS_HITPOINTS) <= 20 && ctx.inventory.select().id(foodID).count() > 5);

    }

    @Override
    public void execute() {
        System.out.println("Eat_Food activated");
        if (!ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        oldHP = ctx.skills.level(Constants.SKILLS_HITPOINTS);
        while (ctx.inventory.select().id(foodID).count() >= 1 && ctx.skills.level(Constants.SKILLS_HITPOINTS) < ctx.skills.realLevel(Constants.SKILLS_HITPOINTS) - 9) {
            Item food = ctx.inventory.select().id(foodID).shuffle().poll();
            food.click();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.skills.level(Constants.SKILLS_HITPOINTS) > oldHP;
                }
            },200 + rand.nextInt(200), 3);
        }

    }

}
