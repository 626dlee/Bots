import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import java.awt.*;
import java.util.Random;

public class AntiBan extends Task<ClientContext> {
    int mouseX;
    int mouseY;

    Random rand = new Random();

    int rand_int = -50 + rand.nextInt(100);
    int rand_int1 = -50 + rand.nextInt(100);
    int rand_time;
    int rand_chance;


    public AntiBan(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (ctx.players.local().animation() != -1) { //rewrite custom entry condition
//            System.out.println("idle");
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void execute() {
        rand_int = (50 + rand.nextInt(100)) * (int)(Math.pow(-1, rand.nextInt(2)));
        rand_int1 = (50 + rand.nextInt(100))* (int)(Math.pow(-1, rand.nextInt(2)));
        rand_time = 750 + rand.nextInt(1320);
        rand_chance = rand.nextInt(15);
        mouseX = (int)ctx.input.getLocation().getX();
        mouseY = (int)ctx.input.getLocation().getY();
        if (mouseX + rand_int < ctx.widgets.component(122,0).width() && mouseX + rand_int > 0) {
            mouseX += rand_int;
        }
        else if (mouseX + rand_int < mouseX && mouseX + rand_int> 0){
            mouseX += rand_int;
        }
        if (mouseY + rand_int1 < ctx.widgets.component(122,0).height() && mouseY + rand_int1 > 0) {
            mouseY += rand_int1;
        }
        else if (mouseY + rand_int1 < mouseY && mouseY + rand_int1 > 0) {
            mouseY += rand_int1;
        }


        if (rand.nextBoolean()) {
            if (rand_chance <= 1) {
                if (rand.nextInt(3) == 0) {
                    ctx.input.send("{VK_LEFT down}");
                    Condition.sleep(570 + rand.nextInt(300));
                    ctx.input.send("{VK_LEFT up}");
                }
                if (rand.nextInt(3) == 1) {
                    ctx.input.send("{VK_UP down}");
                    Condition.sleep(570 + rand.nextInt(300));
                    ctx.input.send("{VK_UP up}");
                }
                if (rand.nextInt(12) == 2) {
                    ctx.input.send("{VK_DOWN down}");
                    Condition.sleep(57 + rand.nextInt(200));
                    ctx.input.send("{VK_DOWN up}");
                }
                if (rand.nextInt(3) == 3) {
                    ctx.input.send("{VK_RIGHT down}");
                    Condition.sleep(570 + rand.nextInt(300));
                    ctx.input.send("{VK_RIGHT up}");
                }
                ctx.input.click(mouseX, mouseY, false);
                Condition.sleep(rand_time);
            } else if (rand_chance == 2) {
                if (rand.nextInt(1) == 1) {
                    ctx.game.tab(Game.Tab.STATS);
                    ctx.widgets.component(320, 19).hover();
                    Condition.sleep(1000 + rand.nextInt(3000));
                    if (rand.nextInt(2) <= 1) {
                        ctx.game.tab(Game.Tab.INVENTORY);
                    } else {
                        ctx.game.tab(Game.Tab.EQUIPMENT);
                        ctx.game.tab(Game.Tab.INVENTORY);
                    }
                } else {
                    ctx.input.send("{VK_F2}");
//                ctx.input.send("{VK_F2 up}");
                    ctx.widgets.component(320, 19).hover();
                    if (rand.nextInt(4) == 1) {
                        Condition.sleep(750 + rand.nextInt(500));
                        ctx.widgets.component(320, 9).hover();
                    }
                    Condition.sleep(1000 + rand.nextInt(3000));
                    if (rand.nextInt(2) < 1) {
                        ctx.game.tab(Game.Tab.INVENTORY);
                    } else {
                        ctx.input.send("{VK_ESCAPE}");
//                    ctx.input.send("{VK_ESCAPE up}");
                    }
                }
//            if (rand.nextInt(2) <= 1) {
//                ctx.game.tab(Game.Tab.INVENTORY);
//            }
//            else {
//                ctx.game.tab(Game.Tab.EQUIPMENT);
//                ctx.game.tab(Game.Tab.INVENTORY);
//            }
            } else if (rand_chance <= 6) {
                ctx.input.move(mouseX, mouseY);
                Condition.sleep(400 + rand.nextInt(83));
            }
            else if (rand_chance == 8) {
                if (!ctx.players.select().within(25).isEmpty()) {
                    ctx.players.shuffle().poll().hover();
                    Condition.sleep(200 + rand.nextInt(500));
                    ctx.input.click(false);
                    Condition.sleep(1200 + rand.nextInt(500));
                }
            }
            else {
                Condition.sleep(200 + rand_time);
            }
        }
    }
}
