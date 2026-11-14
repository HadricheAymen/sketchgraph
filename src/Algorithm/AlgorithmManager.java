package Algorithm;

public class AlgorithmManager {
    private static AlgorithmManager instance;
    private GraphAlgorithm graphAlgorithm;

    private AlgorithmManager() {}

    public static synchronized AlgorithmManager getInstance(){
        if (instance==null)
            instance = new AlgorithmManager();
        return instance;
    }

    public void setAlgorithm(GraphAlgorithm graphAlgorithm) {
        this.graphAlgorithm = graphAlgorithm;
    }
    public void Execute() {
        graphAlgorithm.launch();
    }
}
