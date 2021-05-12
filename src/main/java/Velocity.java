public class Velocity {
    int x1;
    int x2;
    double probability;

    public Velocity(int x1, int x2, double probability) {
        this.x1 = x1;
        this.x2 = x2;
        this.probability = probability;
    }


    public int getX1() {
        return x1;
    }


    public int getX2() {
        return x2;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
