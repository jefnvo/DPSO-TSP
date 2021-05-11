import java.util.ArrayList;

public class Particle {
    ArrayList<Long> solution;
    ArrayList<Velocity> velocity;
    ArrayList<Long> pBest;
    Double fitness;
    Double bestFitness;
    Long cost;

    public Particle(ArrayList<Long> solution, ArrayList<Velocity> velocity, Long cost) {
        this.solution = solution;
        this.velocity = velocity;
        this.pBest = solution;
        this.cost = cost;
    }

    public ArrayList<Long> getSolution() {
        return solution;
    }

    public void setSolution(ArrayList<Long> solution) {
        this.solution = solution;
    }

    public ArrayList<Velocity> getVelocity() {
        return velocity;
    }

    public void setVelocity(ArrayList<Velocity> velocity) {
        this.velocity = velocity;
    }

    public ArrayList<Long> getpBest() {
        return pBest;
    }

    public void setpBest(ArrayList<Long> pBest) {
        this.pBest = pBest;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
