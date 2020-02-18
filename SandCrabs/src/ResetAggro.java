import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItems;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.Npc;

import java.util.ArrayList;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ResetAggro extends Task<ClientContext> {
    Area firstArea = new Area(new Tile(1748, 3484, 0), new Tile(1753, 3481, 0)); //fill in the different paths to reset aggro
    Area secondArea = new Area(new Tile(1749, 3499, 0), new Tile(1753, 3496, 0));
    Area thirdArea = new Area(new Tile(1750, 3511, 0), new Tile(1752, 3510, 0));
    Area fourthArea = new Area(new Tile(1745, 3518, 0), new Tile(1749, 3514, 0));
    Area fifthArea = new Area(new Tile(1749, 3500, 0), new Tile(1751, 3498, 0));
    Area sixthArea = new Area(new Tile(1750, 3485, 0), new Tile(1753, 3483, 0));
    Area seventhArea = new Area(new Tile(1754, 3473, 0), new Tile(1750, 3474, 0));
    Tile sandCrabSpot = new Tile(1749,3469,0); //Change 0's to the sand crab spot position
    private List<Area> resetRoute = new ArrayList<Area>();
    EatFood eat;
    BankFood bankTrip;
    private int[] sandCrabIDs = {7206, 5935};
    private int[] sandyRockIDs = {7207,5936};
    Tile unspawnedCrabSpot = new Tile(1748,3470,0);
    Tile unspawnedCrabSpot1 = new Tile(1750,3470,0);
    Tile unspawnedCrabSpot2 = new Tile(1750,3468,0);

    Random rand = new Random();
    public ResetAggro(ClientContext ctx) {
        super(ctx);
        resetRoute.addAll(Arrays.asList(firstArea, secondArea, thirdArea, fourthArea, fifthArea, sixthArea, seventhArea));
        eat = new EatFood(ctx);
        bankTrip = new BankFood(ctx);
    }


    @Override
    public boolean activate() {
        if (!ctx.players.local().healthBarVisible() && ctx.players.local().tile().compareTo(sandCrabSpot) == 0) {
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().healthBarVisible()  && ctx.players.local().tile().compareTo(sandCrabSpot) == 0;
                }
            }, 1500 + rand.nextInt(500), 4);
            if (!ctx.players.local().healthBarVisible() && ctx.players.local().tile().compareTo(sandCrabSpot) == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    private void adjustSpot() {
        if (sandCrabSpot.matrix(ctx).onMap()) {
//            while (ctx.players.local().tile().compareTo(sandCrabSpot) != 0) {

//            }
            if (sandCrabSpot.matrix(ctx).inViewport()) {
                if (ctx.groundItems.select().at(sandCrabSpot).isEmpty()) {
                    sandCrabSpot.matrix(ctx).click();
                }
                else {
                    sandCrabSpot.matrix(ctx).interact("Walk here");
                }

                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.players.local().tile().compareTo(sandCrabSpot) == 0;
                    }
                }, 300 + rand.nextInt(200), 2);
            }
            else {
                while (!sandCrabSpot.matrix(ctx).inViewport()) {
                    ctx.movement.step(seventhArea.getRandomTile());
                    ctx.camera.turnTo(sandCrabSpot);
                }
//                ctx.camera.turnTo(sandCrabSpot);
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return sandCrabSpot.matrix(ctx).inViewport();
                    }

                },300 + rand.nextInt(500), 3);
                if (ctx.groundItems.select().at(sandCrabSpot).isEmpty()) {
                    sandCrabSpot.matrix(ctx).click();
                }
                else {
                    sandCrabSpot.matrix(ctx).interact("Walk here");
                }
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.players.local().tile().compareTo(sandCrabSpot) == 0 && ctx.players.local().healthBarVisible();
                    }
                },300 + rand.nextInt(500), 3);
                if (ctx.players.local().tile().compareTo(sandCrabSpot) != 0 && !ctx.players.local().healthBarVisible()) {
                    adjustSpot();
                }
            }
//            Condition.sleep(500 + rand.nextInt(500))
        }
        else {
            System.out.println("Sand crab spot not on map");
            ctx.movement.step(sandCrabSpot);
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().inMotion();
                }
            }, 300 + rand.nextInt(300), 2);
            adjustSpot();
        }

    }

    public void checkCrash() {
        if (ctx.players.local().tile().compareTo(sandCrabSpot) == 0) {
            if (!ctx.players.select().within(1).select(new Filter<Player>() {
                public boolean accept(Player player) {
                    //filter out yourself
                    return !player.equals(ctx.players.local());
                }
            }).isEmpty()) {
                System.out.println("Crasher alert");
                if (rand.nextBoolean()) {
                    ctx.input.send("hey bro");
                    ctx.input.send("{VK_ENTER}");
                }
                Condition.sleep(200 + rand.nextInt(400));
                ctx.input.send("i was just resetting");
                ctx.input.send("{VK_ENTER}");
                Condition.sleep(100 + rand.nextInt(500));
                ctx.input.send("i'd appreciate it if you could hop");
                ctx.input.send("{VK_ENTER}");
            }
        }
    }


    public void killStragglers() {
        Condition.sleep(300 + rand.nextInt(500));
//        System.out.println(ctx.npcs.select().id(sandyRockIDs).within(2.0).select());
//        if (ctx.npcs.select().id(sandCrabIDs).within(2.0).select().size() == 3) {
//
//        }
//        System.out.println("sandy rocks present?: " + ctx.npcs.select().within(2.0).id(sandyRockIDs).isEmpty());
//        for (Npc crab : ctx.npcs.select().id(sandCrabIDs)) {
//            if (sandCrabSpot.distanceTo(crab.tile()) <= 3) {
//
//            }
//        }
        if (ctx.players.local().tile().compareTo(sandCrabSpot) == 0  && !ctx.players.local().healthBarVisible() && ctx.npcs.select().within(2.0).id(sandyRockIDs).isEmpty()) {
            System.out.println("Attempting to kill straggling crabs.");
            if (!ctx.npcs.select().id(sandCrabIDs).isEmpty()) {
                for (Npc crab : ctx.npcs.select().id(sandCrabIDs)) {
                    if (sandCrabSpot.distanceTo(crab.tile()) <= 3) {
                        if (crab.inViewport()) {
                            crab.click();
                            Condition.wait(new Condition.Check() {
                                @Override
                                public boolean poll() {
                                    return !ctx.players.local().inMotion();
                                }
                            }, 200 + rand.nextInt(300), 3);
                        } else {
                            ctx.camera.turnTo(crab);
                            crab.click();
                            Condition.wait(new Condition.Check() {
                                @Override
                                public boolean poll() {
                                    return !ctx.players.local().inMotion();
                                }
                            }, 1200 + rand.nextInt(300), 13);
                        }
                    }
                }
            }
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().healthBarVisible();
                }
            },300 + rand.nextInt(200),3);
            while (ctx.players.local().tile().compareTo(sandCrabSpot) != 0) {
                sandCrabSpot.matrix(ctx).click();
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.players.local().tile().compareTo(sandCrabSpot) == 0;
                    }
                }, 300 + rand.nextInt(300), 2 );
            }
        }
        else {
            System.out.println("No straggling crabs detected");
        }
    }

    private void checkBank() {
        if (bankTrip.activate()) {
            bankTrip.execute();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return !bankTrip.activate();
                }
            }, 1000 + rand.nextInt(2000),20);
        } else if (eat.activate()) {
            eat.execute();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return !eat.activate();
                }
            }, 500 + rand.nextInt(300), 10);
        }
    }


    @Override
    public void execute() {

        System.out.println("Reset_Aggro activated");
        checkBank();
        if (ctx.players.local().tile().compareTo(sandCrabSpot) == 0 && ctx.npcs.select().id(sandyRockIDs).isEmpty()) {
            killStragglers();
        } else {
//            Condition.wait(new Condition.Check() {
//                @Override
//                public boolean poll() {
//                    return !eat.activate();
//                }
//            }, 500 + rand.nextInt(300), 2);
                for (Area area : resetRoute) {
                    if (area.getRandomTile().matrix(ctx).inViewport()) {
                        if (rand.nextBoolean()) {
                            area.getRandomTile().matrix(ctx).click();
                        } else {
                            ctx.movement.step(area.getRandomTile().matrix(ctx));
                        }

                    } else if (area.getRandomTile().matrix(ctx).onMap()) {
                        ctx.movement.step(area.getRandomTile().matrix(ctx));
                        ctx.camera.turnTo(area.getRandomTile());
                    }
                    if (!ctx.movement.running()) {
                        Condition.wait(new Condition.Check() {
                            @Override
                            public boolean poll() {
                                return area.contains(ctx.players.local().tile()) || ctx.players.local().animation() == -1;
                            }
                        }, 3500 + rand.nextInt(1500), 10);
                    } else {
                        Condition.wait(new Condition.Check() {
                            @Override
                            public boolean poll() {
                                return area.contains(ctx.players.local().tile()) || ctx.players.local().animation() == -1;
                            }
                        }, 1500 + rand.nextInt(1500), 10);
                    }
                    if (!area.contains(ctx.players.local().tile())) {
                        ctx.movement.step(area.getCentralTile());
                    }
                }

            System.out.println("Sandcrab Spot on map?: " + sandCrabSpot.matrix(ctx).onMap());
            adjustSpot();
            checkCrash();


//        if (firstArea.getRandomTile().matrix(ctx).inViewport()) {
//            firstArea.getRandomTile().matrix(ctx).click();
//        }
//        else if (firstArea.getRandomTile().matrix(ctx).onMap()){
//            ctx.movement.step(firstArea.getRandomTile().matrix(ctx));
//            ctx.camera.turnTo(firstArea.getRandomTile());
//        }
//        Condition.wait(new Condition.Check() {
//            @Override
//            public boolean poll() {
//                return firstArea.contains(ctx.players.local().tile());
//            }
//        }, rand.nextInt(1300,2000), 4);
//        if (secondArea.getRandomTile().matrix(ctx).inViewport()) {
//            secondArea.getRandomTile().matrix(ctx).click();
//        }
//        else if (secondArea.getRandomTile().matrix(ctx).onMap()) {
//            ctx.movement.step(secondArea.getRandomTile().matrix(ctx));
//            ctx.camera.turnTo(secondArea.getRandomTile());
//        }
//        Condition.wait(new Condition.Check() {
//            @Override
//            public boolean poll() {
//                return secondArea.contains(ctx.players.local().tile());
//            }
//        }, rand.nextInt(1300,2000), 4);
        }
    }

}
