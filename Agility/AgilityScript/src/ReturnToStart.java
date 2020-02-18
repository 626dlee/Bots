import com.sun.org.apache.bcel.internal.generic.GotoInstruction;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.TileMatrix;

import java.util.Random;

public class ReturnToStart extends Task<ClientContext> {

    Random rand = new Random();
    Area startArea = new Area(new Tile(0,0,0), new Tile(0,0,0)); //create area by obstacle start

    public ReturnToStart(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (ctx.players.local().tile().floor() == 0 && (double) ctx.skills.level(Constants.SKILLS_HITPOINTS)/ctx.skills.realLevel(Constants.SKILLS_HITPOINTS) > 50.0) {
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void execute() {
        GoToStart();
    }

    private void GoToStart() {
        TileMatrix start = startArea.getRandomTile().matrix(ctx);
        if (!startArea.contains(ctx.players.local().tile())) {
            if (start.inViewport()) {
                start.click();
            }
            else if (start.onMap()) {
                ctx.camera.turnTo(start);
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return start.inViewport();
                    }
                }, 500 + rand.nextInt(500), 5);
            }
            else {
                ctx.movement.step(start);
            }
        }
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return !ctx.players.local().inMotion();
            }
        }, 500 + rand.nextInt(500), 5);
    }


}
