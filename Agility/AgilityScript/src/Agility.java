import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;

public class Agility extends Task<ClientContext> {

    Random rand = new Random();
    private int[] agilityObjectIDs = {};


    public Agility (ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (!ctx.objects.select().id(agilityObjectIDs).isEmpty() && !ctx.players.local().inMotion()) {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public void execute() {
        useObstacle();
    }

    private void useObstacle() {
        GameObject agilObstacle = ctx.objects.select().id(agilityObjectIDs).nearest().poll();
        if (agilObstacle.valid()) {

            if (agilObstacle.inViewport()) {
                agilObstacle.click();
            }
            else if (agilObstacle.tile().matrix(ctx).onMap()) {
                ctx.camera.turnTo(agilObstacle.tile().matrix(ctx));
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return agilObstacle.inViewport();
                    }
                },500 + rand.nextInt(1500),3);
                agilObstacle.click();
            }
            else {
                ctx.movement.step(agilObstacle.tile().matrix(ctx));
            }
        }
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return !ctx.players.local().inMotion();
            }
        }, 500 + rand.nextInt(1000), 10);
    }
}
