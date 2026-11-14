package ShapeFactory;

public abstract class ShapeFactory implements Cloneable {
    double startX, startY, endX, endY;
    public void setCoordinates(double startX,double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    };
    public double getStartX() {return startX;}
    public double getStartY() {return startY;}
    public double getEndX() {return endX;}
    public double getEndY() {return endY;}

    public abstract void draw();
    public abstract boolean Validate();
    public abstract String getShapeName();
    public void setShapeValue(String name){};
    public void setShapeValue(int value){};
    public abstract void setShapeData();
    public abstract void AdjustCoordinates();

}
