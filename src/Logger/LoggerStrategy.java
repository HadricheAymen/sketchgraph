package Logger;

import ShapeFactory.ShapeFactory;

public interface LoggerStrategy {
    void log(ShapeFactory shapeFactory, String actiontype,String actionStatus);
}
