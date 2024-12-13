import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {
    JFrame displayZoneFrame;

    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;
    GameTime gameTime;

    private String[] levels = {"./data/level1.txt", "./data/level2.txt", "./data/level3.txt"};
    public int getTotalLevels() {
        return levels.length; // Par exemple, utilisez la taille de votre liste de niveaux
    }
    private int currentLevelIndex = 0;
    private DynamicSprite hero;

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Java Labs");
        displayZoneFrame.setSize(850, 600);
        displayZoneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hero = new DynamicSprite(200, 300,
                ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

        gameTime = new GameTime(60);
        renderEngine = new RenderEngine(this);
        physicEngine = new PhysicEngine();
        gameEngine = new GameEngine(hero);

        Timer renderTimer = new Timer(50, (time) -> renderEngine.update());
        Timer gameTimer = new Timer(50, (time) -> gameEngine.update());
        Timer physicTimer = new Timer(50, (time) -> physicEngine.update());
        Timer gameTimeTimer = new Timer(1000, (time) -> gameTime.update());

        renderTimer.start();
        gameTimer.start();
        physicTimer.start();
        gameTimeTimer.start();

        displayZoneFrame.getContentPane().add(renderEngine);
        displayZoneFrame.setVisible(true);

        Playground level = new Playground(levels[currentLevelIndex]);
        for (Displayable displayable : level.getSpriteList()) {
            renderEngine.addToRenderList(displayable);
        }
        renderEngine.addToRenderList(hero);
        physicEngine.addToMovingSpriteList(hero);
        physicEngine.setEnvironment(level.getSolidSpriteList());

        displayZoneFrame.addKeyListener(gameEngine);
    }

    public void loadNextLevel() {
        if (currentLevelIndex < levels.length - 1) {
            currentLevelIndex++;
            Playground level = new Playground(levels[currentLevelIndex]);

            // Réinitialisation des listes
            renderEngine.clearRenderList();
            physicEngine.setEnvironment(level.getSolidSpriteList());

            // Ajout des nouveaux éléments à l'environnement
            for (Displayable displayable : level.getSpriteList()) {
                renderEngine.addToRenderList(displayable);
            }

            // Réinitialisation du héros
            hero.reset(200, 300);
            hero.isLevelValidated = false;

            // Ré-ajout du héros dans les listes nécessaires
            renderEngine.addToRenderList(hero);
            physicEngine.addToMovingSpriteList(hero);

            // Réinitialisation du timer
            gameTime = new GameTime(60);
        } else {
            System.out.println("Vous avez terminé tous les niveaux !");
            renderEngine.gameOver = true;
        }
    }

    public int getCurrentLevel() {
        return currentLevelIndex + 1; // Ajout de 1 pour que l'index commence à 1 au lieu de 0
    }




    public static void main(String[] args) throws Exception {
        new Main();
    }
}
