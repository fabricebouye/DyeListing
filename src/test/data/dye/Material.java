package test.data.dye;

import javafx.scene.paint.Color;

/**
 * Un matériau d'application de la teinture (tissu, cuir ou métal).
 * @author Fabrice Bouyé
 */
public final class Material {
    int brightness;
    double contrast;
    Color color;

    /**
     * Crée une nouvelle instance vide.
     */
    Material() {
    }

    public int getBrightness() {
        return brightness;
    }

    public double getContrast() {
        return contrast;
    }

    public Color getColor() {
        return color;
    }
    
}
