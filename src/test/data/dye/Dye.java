package test.data.dye;

import javafx.scene.paint.Color;

/**
 * une teinture dans l'API GW2.
 * @author Fabrice Bouyé
 */
public final class Dye {


    int id;
    String name;
    Color base;
    Material cloth;
    Material leather;
    Material metal;

    /**
     * Crée une nouvelle instance vide.
     */
    Dye() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getBase() {
        return base;
    }

    public Material getCloth() {
        return cloth;
    }

    public Material getLeather() {
        return leather;
    }

    public Material getMetal() {
        return metal;
    }
}
