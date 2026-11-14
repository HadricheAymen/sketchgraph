package ShapeFactory.AdditionalFactoryData;

import ShapeFactory.ShapeFactory;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.util.Optional;

public class CircleValue implements FactoryValue {

    @Override
    public void setValue(ShapeFactory shapeFactory) {
        // Create a TextInputDialog for entering the circle name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Circle Name");
        dialog.setHeaderText("Please enter a name for the circle:");
        dialog.setContentText("Circle Name:");
        dialog.initStyle(StageStyle.UTILITY); // Optional: Makes the dialog simpler

        // Show the dialog and wait for user input
        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                try {
                    String name = result.get();
                    shapeFactory.setShapeValue(name);
                    break;
                }
                catch (Exception e){
                    break;
                }
            }
        }
    }
}
