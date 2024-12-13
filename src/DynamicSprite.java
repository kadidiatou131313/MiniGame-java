import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite{
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 250;
    private boolean isWalking =true;
    private final int spriteSheetNumberOfColumn = 10;
    // Add life level
    protected int lifeLevel = 300;
    protected boolean autoMove = false;
    protected boolean isLevelValidated = false;

    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }
    public DynamicSprite(double x, double y, Image image, double width, double height, boolean autoMove) {
        super(x, y, image, width, height);
        this.autoMove = autoMove;
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        Rectangle2D.Double moved = new Rectangle2D.Double();
        switch (direction) {
            case EAST:
                moved.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:
                moved.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY(),
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case NORTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() - speed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:
                moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() + speed,
                        super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment) {
            if ((s instanceof SolidSprite) && (s != this)) {
                if (((SolidSprite) s).intersect(moved)) {
                    // Modifier la vie uniquement si l'objet est dangereux
                    if (((SolidSprite) s).lifeModifierCount != 0) {
                        this.lifeLevel += ((SolidSprite) s).lifeModifierCount;
                    }

                    // Valider le niveau si nécessaire
                    if (((SolidSprite) s).doesValidateLevel) {
                        this.isLevelValidated = true;
                    }

                    return false; // Collision détectée
                }
            }
        }
        return true; // Pas de collision, mouvement possible
    }


    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move(){
        switch (direction){
            case NORTH -> {
                this.y-=speed;
            }
            case SOUTH -> {
                this.y+=speed;
            }
            case EAST -> {
                this.x+=speed;
            }
            case WEST -> {
                this.x-=speed;
            }
        }
    }
    private Direction getRandomNewDirection() {
        Direction candidateDirection;
        do {
            candidateDirection = Direction.values()[(int) (Math.random() * Direction.values().length)];
        } while (candidateDirection == this.direction);
        return candidateDirection;
    }

    public void moveIfPossible(ArrayList<Sprite> environment){
        if (isMovingPossible(environment)){
            move();
        } else {
            if(this.autoMove) {
                // Choisir une direction random différente
                this.setDirection(getRandomNewDirection());
                move();
            }
        }
    }


    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.isLevelValidated = false;
    }



    @Override
    public void draw(Graphics g) {
        int index= (int) (System.currentTimeMillis()/timeBetweenFrame%spriteSheetNumberOfColumn);

        g.drawImage(image,(int) x, (int) y, (int) (x+width),(int) (y+height),
                (int) (index*this.width), (int) (direction.getFrameLineNumber()*height),
                (int) ((index+1)*this.width), (int)((direction.getFrameLineNumber()+1)*this.height),null);
    }
}
