package States;

import Logger.Logger;
import Providers.GraphicsContextProvider;
import ShapeFactory.*;
import Zone.CanvasZone;

import java.util.ArrayList;
import java.util.List;

public class ShapeManager implements Observable{
    private static ShapeManager instance;
    private List<ShapeFactory> shapes;
    private List<Observer> observers;
    private ShapeFactory currentFactory;

    private ShapeManager() {
        shapes = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public static synchronized ShapeManager getInstance() {
        if (instance == null) {
            instance = new ShapeManager();
        }
        return instance;
    }

    public void addShape(ShapeFactory shape) {
            shapes.add(shape);
    }

    public void removeLastShape() {
        Logger.getInstance().log(shapes.getLast(),"Undo","Success");
        shapes.removeLast();
        GraphicsContextProvider.getGraphicsContext().clearRect(0, 0,900, 700);
        reDrawAll();
    }
    public void restoreShapes(double height, double width) {
        GraphicsContextProvider.getGraphicsContext().clearRect(0, 0,height, width);
        reDrawAll();
    }

    public List<ShapeFactory> getShapes() {
        return new ArrayList<>(shapes); // Return a copy to prevent external modification
    }

    public void reDrawAll() {
        for (ShapeFactory shape : shapes)
            shape.draw();
    }

    public void clearShapes() {
        shapes.clear();
    }

    public void setCurrentFactory(ShapeFactory shapeFactory){
        this.currentFactory=shapeFactory;
        notifyAllObservers();
    }
    public ShapeFactory getCurrentFactory(){return this.currentFactory;}

    // Observer pattern for UI updates
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
