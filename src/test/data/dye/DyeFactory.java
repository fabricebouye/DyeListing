package test.data.dye;

import javafx.scene.paint.Color;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Fabrique à teinture
 * @author Fabrice Bouyé
 */
public enum DyeFactory {

    INSTANCE;

    /**
     * Converti une teinture au format JSON en instance de la classe
     * {@code Dye}.
     * @param jsonObject L'objet source.
     * @return Une instance de la classe {@code Dye}, jamais {@code null}.
     */
    public static Dye createDye(final JsonObject jsonObject) {
        final Dye result = new Dye();
        result.id = jsonObject.getInt("id"); // NOI18N.
        result.name = jsonObject.getString("name"); // NOI18N.
        final JsonArray base_rgb = jsonObject.getJsonArray("base_rgb"); // NOI18N.
        final int red = base_rgb.getInt(0);
        final int green = base_rgb.getInt(1);
        final int blue = base_rgb.getInt(2);
        result.base = Color.rgb(red, green, blue);
        result.cloth = createMaterial(jsonObject.getJsonObject("cloth")); // NOI18N.
        result.leather = createMaterial(jsonObject.getJsonObject("leather")); // NOI18N.
        result.metal = createMaterial(jsonObject.getJsonObject("metal")); // NOI18N.
        return result;
    }

    /**
     * Converti un matériau au format JSON en instance de la classe
     * {@code Dye.Material}.
     * @param jsonObject L'objet source.
     * @return Une instance de la classe {@code Dye.Material}, jamais
     * {@code null}.
     */
    private static Dye.Material createMaterial(final JsonObject jsonObject) {
        final Dye.Material result = new Dye.Material();
        result.brightness = jsonObject.getJsonNumber("brightness").intValue(); // NOI18N.
        result.contrast = jsonObject.getJsonNumber("contrast").doubleValue(); // NOI18N.
        final JsonArray rgb = jsonObject.getJsonArray("rgb"); // NOI18N.
        final int red = rgb.getInt(0);
        final int green = rgb.getInt(1);
        final int blue = rgb.getInt(2);
        result.color = Color.rgb(red, green, blue);
        return result;
    }

}
