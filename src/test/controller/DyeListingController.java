package test.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import test.data.dye.Dye;
import test.query.ColorsQuery;

/**
 * Contrôleur pour l'IU DyeListing.
 * @author Fabrice Bouyé
 */
public final class DyeListingController implements Initializable {

    @FXML
    private FlowPane colorFlow;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private HBox materialBox;
    @FXML
    private ToggleGroup materialSelectionGroup;
    @FXML
    private ToggleButton clothToggle;
    @FXML
    private ToggleButton leatherToggle;
    @FXML
    private ToggleButton metalToggle;
    @FXML
    private HBox languageBox;
    @FXML
    private ToggleGroup languageSelectionGroup;
    @FXML
    private ToggleButton enToggle;
    @FXML
    private ToggleButton frToggle;
    @FXML
    private ToggleButton deToggle;
    @FXML
    private ToggleButton esToggle;
    @FXML
    private GridPane colorBox;
    @FXML
    private Label colorLabel;
    @FXML
    private Rectangle mainColorCell;
    @FXML
    private Rectangle clothCell;
    @FXML
    private Rectangle leatherCell;
    @FXML
    private Rectangle metalCell;
    @FXML
    private Text clothText;
    @FXML
    private Text leatherText;
    @FXML
    private Text metalText;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        //
        enToggle.setUserData("en"); // NOI18N.
        frToggle.setUserData("fr"); // NOI18N.
        deToggle.setUserData("de"); // NOI18N.
        esToggle.setUserData("es"); // NOI18N.
        languageSelectionGroup.selectedToggleProperty().addListener(languageInvalidationListener);
        //
        clothToggle.setUserData("cloth"); // NOI18N.
        leatherToggle.setUserData("leather"); // NOI18N.        
        metalToggle.setUserData("metal"); // NOI18N.        
        materialSelectionGroup.selectedToggleProperty().addListener(materialInvalidationListener);
        //
        colorBox.setVisible(false);
        //
        final Toggle selectedMaterialToggle = materialSelectionGroup.getSelectedToggle();
        final String materialCode = (selectedMaterialToggle == null) ? null : (String) selectedMaterialToggle.getUserData();
        final Toggle selectedLanguageToggle = languageSelectionGroup.getSelectedToggle();
        final String languageCode = (selectedLanguageToggle == null) ? null : (String) selectedLanguageToggle.getUserData();
        loadDyes(languageCode, materialCode);
    }

    private void loadDyes(final String languageCode, final String materialCode) {
        if (languageCode == null) {
            return;
        }
        displayDye(null);
        progressBar.setVisible(true);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        colorFlow.setVisible(false);
        colorFlow.getChildren().clear();
        final Service<List<Node>> query = new Service<List<Node>>() {

            @Override
            protected Task<List<Node>> createTask() {
                return new Strategy3Task(languageCode, materialCode);
            }
        };
        query.setOnSucceeded(workerStateEvent -> {
            final List<Node> cellList = query.getValue();
            progressBar.setVisible(false);
            colorFlow.getChildren().setAll(cellList);
            colorFlow.setVisible(true);
            if (!cellList.isEmpty()) {
                final Dye dye = (Dye) cellList.get(0).getUserData();
                displayDye(dye);
            }
        });
        query.setOnFailed(workerStateEvent -> {
            System.out.println(query.getException());
        });
        progressBar.progressProperty().bind(query.progressProperty());
        query.start();
    }

    private final InvalidationListener languageInvalidationListener = observable -> {
        final Toggle selectedMaterialToggle = materialSelectionGroup.getSelectedToggle();
        final String material = (selectedMaterialToggle == null) ? null : (String) selectedMaterialToggle.getUserData();
        final Toggle selectedLanguageToggle = languageSelectionGroup.getSelectedToggle();
        final String language = (selectedLanguageToggle == null) ? null : (String) selectedLanguageToggle.getUserData();
        if (language != null) {
            loadDyes(language, material);
        }
    };

    private final InvalidationListener materialInvalidationListener = observable -> {
        final Toggle selectedMaterialToggle = materialSelectionGroup.getSelectedToggle();
        final String material = (selectedMaterialToggle == null) ? null : (String) selectedMaterialToggle.getUserData();
        if (material != null) {
            colorFlow.getChildren()
                    .stream()
                    .map(node -> (Rectangle) node)
                    .forEach(colorCell -> {
                        final Dye dye = (Dye) colorCell.getUserData();
                        final Color fill = colorForMaterial(material, dye);
                        colorCell.setFill(fill);
                    });
            //
            final Dye dyeOnDisplay = (Dye) colorBox.getUserData();
            displayDye(dyeOnDisplay);
        }
    };

    private Color colorForMaterial(final String materialCode, final Dye dyeCode) {
        Color result = null;
        switch (materialCode) {
            case "cloth": {
                result = dyeCode.getCloth().getColor();
            }
            break;
            case "leather": {
                result = dyeCode.getLeather().getColor();
            }
            break;
            case "metal":
            default: {
                result = dyeCode.getMetal().getColor();
            }
        }
        return result;
    }

    private void displayDye(final Dye dye) {
        colorBox.setVisible(dye != null);
        colorBox.setUserData(dye);
        if (dye != null) {
            final Toggle selectedMaterialToggle = materialSelectionGroup.getSelectedToggle();
            final String material = (selectedMaterialToggle == null) ? null : (String) selectedMaterialToggle.getUserData();
            final Color mainColor = (material == null) ? null : colorForMaterial(material, dye);
            final Color clothColor = dye.getCloth().getColor();
            final Color leatherColor = dye.getLeather().getColor();
            final Color metalColor = dye.getMetal().getColor();
            mainColorCell.setFill(mainColor);
            clothCell.setFill(clothColor);
            leatherCell.setFill(leatherColor);
            metalCell.setFill(metalColor);
            colorLabel.setText(dye.getName());
            final String rgbPattern = resources.getString("rgb.pattern"); // NOI18N.
            clothText.setText(String.format(rgbPattern,
                    (int) (255 * clothColor.getRed()),
                    (int) (255 * clothColor.getGreen()),
                    (int) (255 * clothColor.getBlue()),
                    dye.getCloth().getContrast(),
                    dye.getCloth().getBrightness()));
            leatherText.setText(String.format(rgbPattern,
                    (int) (255 * leatherColor.getRed()),
                    (int) (255 * leatherColor.getGreen()),
                    (int) (255 * leatherColor.getBlue()),
                    dye.getLeather().getContrast(),
                    dye.getLeather().getBrightness()));
            metalText.setText(String.format(rgbPattern,
                    (int) (255 * metalColor.getRed()),
                    (int) (255 * metalColor.getGreen()),
                    (int) (255 * metalColor.getBlue()),
                    dye.getMetal().getContrast(),
                    dye.getMetal().getBrightness()));
        }
    }

    private Node createCellForDye(final Dye dye, final String materialCode) {
        final Rectangle result = new Rectangle(16, 16);
        result.getStyleClass().add("color-cell"); // NOI18N.
        final Color fill = colorForMaterial(materialCode, dye);
        result.setFill(fill);
        final Tooltip colorTip = new Tooltip(dye.getName());
        Tooltip.install(result, colorTip);
        result.setUserData(dye);
        result.setOnMouseClicked(mouseEvent -> displayDye(dye));
        return result;
    }

    /**
     * Cette tâche récupère la liste des identifiants de teintures puis ensuite
     * recupère chaque teinture une par une.
     * @author Fabrice Bouyé
     */
    private final class Strategy1Task extends Task<List<Node>> {

        private final String languageCode;
        private final String materialCode;

        public Strategy1Task(final String languageCode, final String materialCode) {
            this.languageCode = languageCode;
            this.materialCode = materialCode;
        }

        @Override
        protected List<Node> call() throws Exception {
            final long startTime = System.currentTimeMillis();
            int currentProgress = 0;
            int totalProgress = 1;
            updateProgress(currentProgress, totalProgress);
            // Recupérer la liste des identifiants.
            final long startListTime = System.currentTimeMillis();
            final List<Integer> dyeIdList = ColorsQuery.list();
            final long endListTime = System.currentTimeMillis();
            totalProgress = dyeIdList.size() * 2 + 1;
            currentProgress++;
            updateProgress(currentProgress, totalProgress);
            if (isCancelled()) {
                return null;
            }
            // Recupérer chaque teinture séparément.
            final List<Node> result = new ArrayList<>(dyeIdList.size());
            long meanDyeTime = 0;
            long peakDyeTime = 0;
            for (final int dyeId : dyeIdList) {
                // Récupération de la teinture.
                final long startDyeTime = System.currentTimeMillis();
                final Dye dye = ColorsQuery.dyeInfo(languageCode, dyeId);
                final long endDyeTime = System.currentTimeMillis();
                meanDyeTime += endDyeTime - startDyeTime;
                peakDyeTime = Math.max(peakDyeTime, endDyeTime - startDyeTime);
                currentProgress++;
                updateProgress(currentProgress, totalProgress);
                if (isCancelled()) {
                    return null;
                }
                // Création du noeud à l'écran.
                final Node colorCell = createCellForDye(dye, materialCode);
                result.add(colorCell);
                currentProgress++;
                updateProgress(currentProgress, totalProgress);
                if (isCancelled()) {
                    return null;
                }
            }
            meanDyeTime = (long) Math.ceil(meanDyeTime / (double) dyeIdList.size());
            // Diagnostique.
            final long endTime = System.currentTimeMillis();
            System.out.printf("Stategy 1: %dms (list: %dms, mean dye: %dms - peak dye: %dms) queries: %d dyes: %d", endTime - startTime, endListTime - startListTime, meanDyeTime, peakDyeTime, 1 + dyeIdList.size(), result.size()).println();
            return result;
        }
    }

    /**
     * Cette tâche récupère toutes les teintures d'un seul coup.
     * @author Fabrice Bouyé
     */
    private final class Strategy2Task extends Task<List<Node>> {

        private final String languageCode;
        private final String materialCode;

        public Strategy2Task(final String languageCode, final String materialCode) {
            this.languageCode = languageCode;
            this.materialCode = materialCode;
        }

        @Override
        protected List<Node> call() throws Exception {
            final long startTime = System.currentTimeMillis();
            int currentProgress = 0;
            int totalProgress = 1;
            updateProgress(currentProgress, totalProgress);
            // Récupération de toutes les teintures.
            final long startQueryTime = System.currentTimeMillis();
            final List<Dye> dyeList = ColorsQuery.dyeInfos(languageCode);
            final long endQueryTime = System.currentTimeMillis();
            totalProgress = dyeList.size() + 1;
            currentProgress++;
            updateProgress(currentProgress, totalProgress);
            if (isCancelled()) {
                return null;
            }
            // Création des noeuds à l'écran.
            final List<Node> result = new ArrayList<>(dyeList.size());
            for (final Dye dye : dyeList) {
                currentProgress++;
                updateProgress(currentProgress, totalProgress);
                final Node colorCell = createCellForDye(dye, materialCode);
                result.add(colorCell);
                if (isCancelled()) {
                    return null;
                }
            }
            // Diagnostique.
            final long endTime = System.currentTimeMillis();
            System.out.printf("Stategy 2: %dms (query: %dms) queries: %d dyes: %d", endTime - startTime, endQueryTime - startQueryTime, 1, result.size()).println();
            return result;
        }
    }

    private final class Strategy3Task extends Task<List<Node>> {

        private final String languageCode;
        private final String materialCode;
        private final int bufferSize = 100;

        public Strategy3Task(final String languageCode, final String materialCode) {
            this.languageCode = languageCode;
            this.materialCode = materialCode;
        }

        @Override
        protected List<Node> call() throws Exception {
            final long startTime = System.currentTimeMillis();
            int currentProgress = 0;
            int totalProgress = 1;
            updateProgress(currentProgress, totalProgress);
            // Recupérer la liste des identifiants.
            final long startListTime = System.currentTimeMillis();
            final List<Integer> dyeIdList = ColorsQuery.list();
            final long endListTime = System.currentTimeMillis();
            final int queryNumber = (int) Math.ceil(dyeIdList.size() / (float) bufferSize);
            totalProgress = queryNumber + dyeIdList.size() + 1;
            currentProgress++;
            updateProgress(currentProgress, totalProgress);
            if (isCancelled()) {
                return null;
            }
            // Recupérer chaque teinture séparément.
            final List<Node> result = new ArrayList<>(dyeIdList.size());
            long meanDyeTime = 0;
            long peakDyeTime = 0;
            for (int queryIndex = 0; queryIndex < queryNumber; queryIndex++) {
                final int startIdndex = queryIndex * bufferSize;
                final int endIndex = Math.min(dyeIdList.size(), startIdndex + bufferSize);
                final int[] idsSubArray = dyeIdList
                        .subList(startIdndex, endIndex)
                        .stream()
                        .mapToInt(value -> value)
                        .toArray();
                // Récupération des teintures.
                final long startDyeTime = System.currentTimeMillis();
                final List<Dye> dyeList = ColorsQuery.dyeInfos(languageCode, idsSubArray);
                final long endDyeTime = System.currentTimeMillis();
                meanDyeTime += endDyeTime - startDyeTime;
                peakDyeTime = Math.max(peakDyeTime, endDyeTime - startDyeTime);
                currentProgress++;
                updateProgress(currentProgress, totalProgress);
                if (isCancelled()) {
                    return null;
                }
                // Création du noeud à l'écran.
                for (final Dye dye : dyeList) {
                    final Node colorCell = createCellForDye(dye, materialCode);
                    result.add(colorCell);
                    currentProgress++;
                    updateProgress(currentProgress, totalProgress);
                    if (isCancelled()) {
                        return null;
                    }
                }
            }
            meanDyeTime = (long) Math.ceil(meanDyeTime / (double) dyeIdList.size());
            // Diagnostique.
            final long endTime = System.currentTimeMillis();
            System.out.printf("Stategy 3: %dms (list: %dms, mean dye: %dms - peak dye: %dms) queries: %d dyes: %d", endTime - startTime, endListTime - startListTime, meanDyeTime, peakDyeTime, 1 + queryNumber, result.size()).println();
            return result;
        }
    }
}
