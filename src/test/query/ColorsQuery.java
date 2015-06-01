package test.query;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import test.data.dye.Dye;
import test.data.dye.DyeFactory;
import static test.query.QueryUtils.*;

/**
 * Permet de faire des requêtes sur l'endpoint Colors.
 * @author Fabrice Bouyé
 */
public enum ColorsQuery {

    INSTANCE;

    private static final String BASECODE = "https://api.guildwars2.com/v2/colors"; // NOI18N.

    /**
     * Récupère la liste des indenfiants des teintures.
     * @return Une {@code List<Integer>}, jamais {@code null}.
     * @throws IOException En cas d'erreur.
     */
    public static List<Integer> list() throws IOException {
        final JsonArray array = queryArray(BASECODE);
        final List<Integer> result = array.getValuesAs(JsonNumber.class)
                .stream()
                .map(value -> value.intValue())
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Récupère l'objet teinture.
     * @param languageCode Le code du language.
     * @param id L'identifiant de la teinture.
     * @return Une instance de {@code Dye}, jamais {@code null}.
     * @throws IOException En cas d'erreur.
     */
    public static Dye dyeInfo(final String languageCode, final int id) throws IOException {
        final JsonObject value = QueryUtils.queryObject(String.format("%s/%d?lang=%s", BASECODE, id, languageCode)); // NOI18N.
        final Dye result = DyeFactory.createDyeFromJSON(value);
        return result;
    }

    /**
     * Récupère plusieurs objets teintures.
     * @param languageCode Le code du language.
     * @param ids Les identifiants des teintures. Si aucun indentfiant est fourni, toutes les teintures connues seront retournées.
     * @return Une {@code List<Dye>}, jamais {@code null}.
     * @throws IOException En cas d'erreur.
     */
    public static List<Dye> dyeInfos(final String languageCode, final int... ids) throws IOException {
        final String idsCode = idsToString(ids);
        final JsonArray array = queryArray(String.format("%s?ids=%s&lang=%s", BASECODE, idsCode, languageCode)); // NOI18N.
        final List<Dye> result = array.getValuesAs(JsonObject.class)
                .stream()
                .map(value -> DyeFactory.createDyeFromJSON(value))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }
}
