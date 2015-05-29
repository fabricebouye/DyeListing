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

    private static final String basecode = "https://api.guildwars2.com/v2/colors"; // NOI18N.

    public static List<Integer> list() throws IOException {
        final JsonArray array = queryArray(basecode);
        final List<Integer> result = array.getValuesAs(JsonNumber.class)
                .stream()
                .map(value -> value.intValue())
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    public static Dye dyeInfo(final String languageCode, final int id) throws IOException {
        final JsonObject value = QueryUtils.queryObject(String.format("%s/%d?lang=%s", basecode, id, languageCode)); // NOI18N.
        final Dye result = DyeFactory.createDye(value);
        return result;
    }

    public static List<Dye> dyeInfos(final String languageCode, final int... ids) throws IOException {
        final String idsCode = idsToString(ids);
        final JsonArray array = queryArray(String.format("%s?ids=%s&lang=%s", basecode, idsCode, languageCode)); // NOI18N.
        final List<Dye> result = array.getValuesAs(JsonObject.class)
                .stream()
                .map(value -> DyeFactory.createDye(value))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }
}
