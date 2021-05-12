import java.util.ArrayList;

public class Particle {
    ArrayList<Long> solution;
    ArrayList<Velocity> velocity;
    ArrayList<Long> pBest;
    Long fitness;
    Long bestFitness;

    public Particle(ArrayList<Long> solution, ArrayList<Velocity> velocity, Long fitness) {
        this.solution = solution;
        this.velocity = velocity;
        this.pBest = solution;
        this.bestFitness = fitness;
        this.fitness = fitness;
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

    public Long getFitness() {
        return fitness;
    }

    public void setFitness(Long fitness) {
        this.fitness = fitness;
    }

    public Long getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Long bestFitness) {
        this.bestFitness = bestFitness;
    }
}
