package ShapeFactory;

import ShapeFactory.AdditionalFactoryData.CircleValue;
import ShapeFactory.AdjustCoordinates.AdjustCircle;
import ShapeFactory.Drawing.*;
import ShapeFactory.Validation.ValidateCercle;

public class CircleFactory extends ShapeFactory{
    char name;

    public char getName() {return name;}

    @Override
    public void setShapeValue(String name) {
        this.name = name.charAt(0);
    }
    @Override
    public void draw() {
        new CircleDrawing().draw(this);
    }
    @Override
    public boolean Validate() {
        return new ValidateCercle().isValid(this);
    }
    @Override
    public void setShapeData() {
        new CircleValue().setValue(this);
    }
    @Override
    public void AdjustCoordinates(){
        new AdjustCircle().AdjustCoordinates(this);
    }
    public String getShapeName() {
        return "Circle";
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        CircleFactory clone = (CircleFactory) super.clone();
        clone.startX = this.startX;
        clone.startY = this.startY;
        clone.endX = this.endX;
        clone.endY = this.endY;
        clone.name = this.name;
        return clone;
    }

}
