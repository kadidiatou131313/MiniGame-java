import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class RenderEngine extends JPanel {
    private Main main; // Référence à l'instance principale de Main
    public boolean gameOver = false; // Ajout de l'attribut gameOver
    private List<Displayable> renderList = new ArrayList<>(); // Remplacement de renderList

    public RenderEngine(Main main) {
        this.main = main;
    }

    private boolean victory = false; // Indique si le joueur a terminé tous les niveaux


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        boolean isLevelValidated = false;

        if (!gameOver && !main.gameTime.isGameOver()) {
            // Parcourt les objets affichables
            for (Displayable renderObject : renderList) {
                if (renderObject instanceof DynamicSprite) {
                    int level = ((DynamicSprite) renderObject).lifeLevel;
                    isLevelValidated = ((DynamicSprite) renderObject).isLevelValidated;

                    // Vérifie si les points de vie sont épuisés
                    if (level <= 0) {
                        gameOver = true; // Marque une défaite
                    }

                    // Affiche les points de vie et le niveau actuel
                    g.setColor(Color.GREEN);
                    g.setFont(new Font("Arial", Font.BOLD, 15));
                    g.drawString("LifePoint: " + level, 10, 20);
                    g.drawString("Level: " + main.getCurrentLevel(), 10, 40);
                }
                renderObject.draw(g); // Dessine l'objet
            }

            // Affiche le temps restant
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Time: " + main.gameTime.getTimeRemaining() + "s", 700, 30);

            // Passe au niveau suivant ou termine le jeu
            if (isLevelValidated) {
                if (main.getCurrentLevel() == main.getTotalLevels()) {
                    // Si c'était le dernier niveau, le joueur a gagné
                    gameOver = true;
                    victory = true;
                } else {
                    // Charge le niveau suivant
                    main.loadNextLevel();
                    return; // Quitte la méthode pour éviter des affichages incorrects
                }
            }
        } else if (gameOver) {
            // Affiche un message différent selon victoire ou défaite
            g.setColor(victory ? Color.BLUE : Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString(victory ? "VICTORY!" : "GAME OVER", 100, 300);
        }
    }




    public void update() {
        repaint();
    }

    public void clearRenderList() {
        renderList.clear();
    }

    public void addToRenderList(Displayable displayable) {
        renderList.add(displayable);
    }
}
