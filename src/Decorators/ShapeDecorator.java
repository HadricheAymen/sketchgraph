package Decorators;

import ShapeFactory.ShapeFactory;

public class ShapeDecorator extends ShapeFactory {
    protected ShapeFactory decoratedShape;

    public ShapeDecorator(ShapeFactory decoratedShape) {
        this.decoratedShape = decoratedShape;
    }
    @Override
    public void draw() {
        decoratedShape.draw();
    }

    @Override
    public boolean Validate() {
        return decoratedShape.Validate();
    }

    @Override
    public String getShapeName() {
        return decoratedShape.getShapeName();
    }

    @Override
    public void setShapeData() {
        decoratedShape.setShapeData();
    }

    @Override
    public void AdjustCoordinates() {
        decoratedShape.AdjustCoordinates();
    }
}
