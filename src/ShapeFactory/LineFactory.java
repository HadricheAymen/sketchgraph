package ShapeFactory;

import ShapeFactory.AdditionalFactoryData.LineValue;
import ShapeFactory.AdjustCoordinates.AdjustLine;
import ShapeFactory.Drawing.LineDrawing;
import ShapeFactory.Validation.*;


public class LineFactory extends ShapeFactory {

    CircleFactory firstCircle;
    CircleFactory secondCircle;
    int value;

    public CircleFactory getFirstCircle() {return firstCircle;}
    public CircleFactory getSecondCircle() {return secondCircle;}
    public int getValue() {return value;}
    public void setValue(int value) {this.value = value;}
    public void setFirstCircle(CircleFactory firstCircle) {this.firstCircle = firstCircle;}
    public void setSecondCircle(CircleFactory secondCircle) {this.secondCircle = secondCircle;}

    public void setCoordinates(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public String getShapeName() {
        return  "Line";
    }
    @Override
    public void setShapeData() {
        new LineValue().setValue(this);
    }
    @Override
    public void setShapeValue(int value) {
        this.value = value;
    }

    @Override
    public void draw() {
        new LineDrawing().draw(this);
    }
    @Override
    public boolean Validate() {
        return new ValidateLine().isValid(this);
    }
    @Override
    public void AdjustCoordinates() {
        new AdjustLine().AdjustCoordinates(this);
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        LineFactory clone = (LineFactory) super.clone();
        clone.startX=this.startX;
        clone.startY=this.startY;
        clone.endX=this.endX;
        clone.endY=this.endY;
        clone.firstCircle = this.firstCircle != null ? (CircleFactory) this.firstCircle.clone() : null;
        clone.secondCircle = this.secondCircle != null ? (CircleFactory) this.secondCircle.clone() : null;

        clone.value=this.value;
        return clone;
    }

}
