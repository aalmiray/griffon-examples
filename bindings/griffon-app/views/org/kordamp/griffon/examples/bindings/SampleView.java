package org.kordamp.griffon.examples.bindings;

import eu.hansolo.medusa.Gauge;
import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.javafx.collections.MappingObservableList;
import griffon.javafx.scene.control.DefaultTableFormat;
import griffon.javafx.scene.control.DefaultTableViewModel;
import griffon.javafx.scene.control.TableViewFormat;
import griffon.javafx.scene.control.TableViewModel;
import griffon.javafx.util.ToStringOnlyStringConverter;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.Collections;

import static griffon.javafx.beans.binding.MappingBindings.mapAsBoolean;
import static griffon.javafx.beans.binding.UIThreadAwareBindings.uiThreadAwareBind;
import static griffon.javafx.beans.binding.UIThreadAwareBindings.uiThreadAwareObjectProperty;
import static griffon.javafx.collections.GriffonFXCollections.uiThreadAwareObservableList;
import static griffon.javafx.support.JavaFXUtils.setI18nKey;
import static java.util.Arrays.asList;

@ArtifactProviderFor(GriffonView.class)
public class SampleView extends AbstractJavaFXGriffonView {
    @MVCMember private SampleController controller;
    @MVCMember private SampleModel model;

    @FXML private Gauge minAmountGauge;
    @FXML private Gauge avgAmountGauge;
    @FXML private Gauge maxAmountGauge;
    @FXML private Label minNameLabel;
    @FXML private Label maxNameLabel;
    @FXML private Label minTimestampLabel;
    @FXML private Label maxTimestampLabel;
    @FXML private TableView<Measurement> measurementsTableView;
    @FXML private Label totalLabel;
    @FXML private LineChart<String, Number> measurementsChart;
    @FXML private ChoiceBox<Language> languageChoiceBox;

    private XYChart.Series<String, Number> sampleSeries = new XYChart.Series<>();
    private ObjectProperty<Language> uiLanguageProperty;

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String, Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("mainWindow", stage);
    }

    @SuppressWarnings("unchecked")
    private Scene init() {
        Node node = loadFromFXML();

        uiThreadAwareBind(minAmountGauge.valueProperty(), model.minAmountBinding());
        uiThreadAwareBind(avgAmountGauge.valueProperty(), model.avgAmountBinding());
        uiThreadAwareBind(maxAmountGauge.valueProperty(), model.maxAmountBinding());
        uiThreadAwareBind(minNameLabel.textProperty(), model.minNameBinding());
        uiThreadAwareBind(maxNameLabel.textProperty(), model.maxNameBinding());
        uiThreadAwareBind(minTimestampLabel.textProperty(), model.minTimestampBinding());
        uiThreadAwareBind(maxTimestampLabel.textProperty(), model.maxTimestampBinding());

        updateTotalLabel();
        model.totalBinding().addListener((v, o, n) -> updateTotalLabel());
        model.localeProperty().addListener((v, o, n) -> updateTotalLabel());
        model.localeProperty().addListener((v, o, n) -> runInsideUIAsync(() -> {
            // force an update of the displayed value
            languageChoiceBox.getItems().add(Language.UNKNOWN);
            languageChoiceBox.getItems().remove(Language.UNKNOWN);
        }));

        TableViewFormat<Measurement> tableFormat = new DefaultTableFormat<>(
            new DefaultTableFormat.Column("name", 0.2d),
            new DefaultTableFormat.Column("amount", 0.1d),
            new DefaultTableFormat.Column("timestamp")
        );
        ObservableList<Measurement> measurements = uiThreadAwareObservableList(model.getMeasurements());
        TableViewModel<Measurement> tableModel = new DefaultTableViewModel<>(measurements, tableFormat);
        setI18nKey(tableModel.getColumnAt(0), SampleView.class.getName() + ".table.name.title");
        setI18nKey(tableModel.getColumnAt(1), SampleView.class.getName() + ".table.amount.title");
        setI18nKey(tableModel.getColumnAt(2), SampleView.class.getName() + ".table.timestamp.title");
        tableModel.attachTo(measurementsTableView);
        measurementsTableView.setEditable(false);

        sampleSeries.setData(new MappingObservableList<>(measurements, m -> new XYChart.Data<>(m.getName(), m.getAmount())));
        measurementsChart.getData().add(sampleSeries);

        languageChoiceBox.setConverter(new ToStringOnlyStringConverter<>(l -> msg(Language.class.getName() + "." + l.getCode())));
        languageChoiceBox.setItems(model.getLanguages());
        uiLanguageProperty = uiThreadAwareObjectProperty(model.languageProperty());
        languageChoiceBox.valueProperty().bindBidirectional(uiLanguageProperty);

        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("bootstrapfx.css");
        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        toolkitActionFor(controller, "clear").enabledProperty()
            .bind(mapAsBoolean(model.totalBinding(), n -> n.intValue() > 0));

        return scene;
    }

    private void updateTotalLabel() {
        runInsideUIAsync(() -> totalLabel.setText(msg(SampleView.class.getName() + ".label.total", asList(model.totalBinding().getValue()))));
    }
}
