package ShapeFactory.AdditionalFactoryData;

import ShapeFactory.*;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.util.Optional;

public class LineValue implements FactoryValue {
    @Override
    public void setValue(ShapeFactory shapeFactory) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Line Value");
        dialog.setHeaderText("Please enter a value for the line:");
        dialog.setContentText("Line Value:");
        dialog.initStyle(StageStyle.UTILITY);

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int value = Integer.parseInt(result.get());
                    shapeFactory.setShapeValue(value);
                    break;
                } catch (NumberFormatException e) {
                    dialog.setHeaderText("Please enter a valid integer value!");
                    dialog.getEditor().clear();
                }
            }
        }
    }
}