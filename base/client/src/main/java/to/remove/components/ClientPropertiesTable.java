package to.remove.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Properties;
import java.util.Set;

public class ClientPropertiesTable extends TableView<ClientPropertiesTable.SystemProperty> {
    class SystemProperty {
        private StringProperty nameProperty;
        private StringProperty valueProperty;

        private SystemProperty(String name, String value) {
            this.nameProperty = new SimpleStringProperty(name);
            this.valueProperty = new SimpleStringProperty(value);
        }
    }

    public ClientPropertiesTable() {
        ObservableList<SystemProperty> rows = FXCollections.observableArrayList();
        Properties properties = System.getProperties();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String property : propertyNames) {
            rows.add(new SystemProperty(property, properties.getProperty(property)));
        }
        TableColumn<SystemProperty, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(param -> param.getValue().nameProperty);
        getColumns().add(keyColumn);
        TableColumn<SystemProperty, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty);
        getColumns().add(valueColumn);
        setItems(rows);
    }
}
