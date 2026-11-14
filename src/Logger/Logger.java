package Logger;

import ShapeFactory.ShapeFactory;

public class Logger {
    private static LoggerStrategy strategy = new ConsoleLogger();
    private static Logger instance;

    private Logger() {
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setStrategy(LoggerStrategy strategy) {
        this.strategy = strategy;
    }

    public void log(ShapeFactory shapeFactory, String actiontype,String actionStatus) {
        strategy.log(shapeFactory, actiontype,actionStatus);
    }
}