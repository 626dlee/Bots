import javafx.scene.effect.Light;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Script.Manifest(name = "proj2", description = "sand crabs", properties = "client=4")

public class Main extends PollingScript<ClientContext>{
    private int hour;
    private int minute;
    Random rand = new Random();
    private int currentMinute = 0;
    private int startingXP;
    private int endHour = 1+rand.nextInt(2);
    private int endMinute = 10 + rand.nextInt(30);
    private int[] sandyRockIDs = {7207,5936};
    Tile sandCrabSpot = new Tile(1749,3469,0); //Change 0's to the sand crab spot position
    private List<Task> taskList = new ArrayList<Task>();
    @Override
    public void start() {
        taskList.addAll(Arrays.asList(new Dismisser(ctx), new EatFood(ctx), new BankReturn(ctx), new ResetAggro(ctx), new AntiBan(ctx)));
        startingXP = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        ctx.input.speed(350);

    }

    @Override
    public void poll() {
//        System.out.println("sandy rocks present?: " + !ctx.npcs.select().within(2.0).id(sandyRockIDs).isEmpty());
        hour = (int) (getRuntime() / 3600000);
        minute = (int) ((getRuntime()% 3600000 )/ 60000);
        if (minute != currentMinute%60) {
            currentMinute = (int) (getRuntime()/60000);
            System.out.println("Script has ran for " + hour + " hours and " + minute + " minutes.");
            if (currentMinute != 0) {
                System.out.println("Estimated XP/hr = " + (double) ((ctx.skills.experience(Constants.SKILLS_STRENGTH) - startingXP) * (60.0 / (double) currentMinute)) + " per hour");
            }

        }
        if ( hour >= endHour && minute >= endMinute) {
            EatFood eatFood = new EatFood(ctx);
            System.out.println("Eating up before ending script.");
            eatFood.execute();
            System.out.println("Script will now end");
            ctx.controller.stop();
        }
        if (ctx.movement.energyLevel() >= 80) {
            ctx.movement.running(true);
        }
        if (ctx.players.local().tile().compareTo(sandCrabSpot) != 0 && sandCrabSpot.distanceTo(ctx.players.local().tile()) <= 4) {
            System.out.println("Readjusting spot");
            sandCrabSpot.matrix(ctx).click();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().tile().compareTo(sandCrabSpot) == 0;
                }
            }, 492, 3);
        }
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

}
