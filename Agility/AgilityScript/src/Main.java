import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name = "Agility", description = "Agility Script", properties = "client=4")

public class Main extends PollingScript<ClientContext> {
    private int startingXP, hours, minutes, totalMinutes = 0;
    private List<Task> taskList = new ArrayList<Task>();
    private double xpGained;
    @Override
    public void start() {
        taskList.addAll(Arrays.asList(new Dismisser(ctx),new Agility(ctx), new AntiBan(ctx)));
        startingXP = ctx.skills.experience(Constants.SKILLS_AGILITY);
    }


    @Override
    public void poll() {
        updateRun();
        displayStats();
        for (Task task : taskList) {
            if ( task.activate()) {
                task.execute();
            }
        }
    }


    private void displayStats() {
        hours =(int) (getRuntime()/ 3600000);
        minutes = (int) (getRuntime()/ 60000)%60;
//        totalMinutes = (int) (getRuntime()/60000);
        xpGained = ctx.skills.experience(Constants.SKILLS_AGILITY) - startingXP;
        if (minutes > totalMinutes%60) {
            totalMinutes++;
            System.out.println("Runtime: " + hours + " hours " + minutes + " minutes");
            System.out.println("XP/hr: " + xpGained * (60/(double)totalMinutes));
        }
    }

    private void updateRun() {
        if (ctx.movement.energyLevel() < 30) {
            if (ctx.movement.running()) {
                ctx.movement.running(false);
            }
        }
        else if (ctx.movement.energyLevel() > 50){
            if (!ctx.movement.running()) {
                ctx.movement.running(true);
            }
        }
    }

}