import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A greep is an alien creature that likes to collect tomatoes. These greeps 
 * also stay with their food to help other greeps to collect it and release an
 * orange pheromone to show other greeps where the food source is. 
 * 
 * @author Adrian Schrader
 * @version 0.2
 */
public class Greep extends Creature
{   
    public static final int GROUP1 = 1;
    public static final boolean IS_GROUPED = true;
    
    /**
     * Default constructor for testing purposes.
     */
    public Greep()
    {
        this(null);
    }

    
    /**
     * Create a greep with its home space ship.
     * @param ship The ship from which the creature is released in an arbitrary direction
     */
    public Greep(Ship ship)
    {
        super(ship);
        this.setFlag(GROUP1, !IS_GROUPED ? true : this.randomChance(50));
    }
    

    /**
     * Runs at the beginning of the greeps turn. It is only allowed to move
     * once within its turn. 
     * @see Creature:act
     */
    @Override
    public void act()
    {
        super.act();
        
        if (this.carryingTomato()) {
            // Greep is on his way home
            if (this.atShip()) {
                // Greep is already at the ship
                dropTomato();
            } else {
                if (!checkObstacles(30)) {
                    // Greep turns into the direction of the ship
                    this.turnHome();
                }
                
                // Marks the way to the food source with paint
                markPaint();
            }
        } else {
            // Greep is searching for tomato pile
            if (!checkObstacles(90)) {
                // Greep turns into the direction of the ship
                checkPaint();
            }
            
            if (checkFood()) {
                this.loadTomato();
                
                if (this.randomChance(97))
                    return;
            }
        }
        
        
        move();
    }

    /**
     * Check if food is available. If so, try to load it onto another greep. 
     */
    private boolean checkFood()
    {
        TomatoPile tomatoes = (TomatoPile) this.getOneIntersectingObject(TomatoPile.class);
        return tomatoes != null;
    }
    
    /**
     * Mark the way to the nearest tomato pile with colorful saliva. 
     */
    private void markPaint() {
        if (this.randomChance(80)) {
            this.spit(this.getFlag(GROUP1) ? "orange" : "red");
        }
    }
    
    /**
     * Check if another greep has already marked the way to a tomato pile. 
     */
    private void checkPaint() {
        if (this.seePaint(this.getFlag(GROUP1) ? "orange" : "red")) {
            this.turnHome();
            this.turn(180);
        }
    }
    
    /**
     * Saves the greep from being stuck in walls (most of the time). 
     */
    private boolean checkObstacles(int angle) {
        if ((this.atWorldEdge() || this.atWater())) {
            this.turn(angle);
            return true;
        }
        return false;
    }
    
    /**
     * This method specifies the name of the author (for display on the result board).
     * @return Full name of the autor
     */
    public static String getAuthorName() {
        return "Adrian Schrader";
    }


    /**
     * This method specifies the image we want displayed at any time. (No need 
     * to change this for the competition)
     * @return Image path in assets directory
     */
    @Override
    public String getCurrentImage() {
        if(carryingTomato())
            return "greep-with-food.png";
        else
            return "greep.png";
    }
}