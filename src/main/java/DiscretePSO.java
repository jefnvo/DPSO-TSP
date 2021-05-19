import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class DiscretePSO {
    int numCities;
    int swarmSize;
    int iterations;
    double alfa;
    double beta;
    Particle globalBest;
    Long[][] distanceWeight;

    public DiscretePSO(int numCities, int swarmSize, double alfa, double beta,
                       Long[][] distanceWeight, int iterations) {
        this.numCities = numCities;
        this.swarmSize = swarmSize;
        this.alfa = alfa;
        this.beta = beta;
        this.distanceWeight = distanceWeight;
        this.iterations = iterations;
    }

    public void execute() {
        ArrayList<Particle> swarm = initializeSwarm();
        for (int i = 0; i < iterations; i++) {
            globalBest = swarm.stream()
                .min(Comparator.comparing(Particle::getBestFitness))
                .orElseThrow(NoSuchElementException::new);

            for (Particle particle : swarm) {
                ArrayList<Long> globalBestSolution = new ArrayList<>(globalBest.getBestSolution());
                ArrayList<Long> particleBestSolution = new ArrayList<>(particle.getBestSolution());

                ArrayList<Velocity> totalVelocity = new ArrayList<>(createBasicSwapSequence(particle, particleBestSolution, alfa));
                totalVelocity.addAll(createBasicSwapSequence(particle, globalBestSolution, beta));
                ArrayList<Long> solution = createSolution(particle, totalVelocity);

                particle.setSolution(solution);
                Long actualFitness = calcFitnessTour(solution);
                particle.setFitness(actualFitness);

                if(actualFitness < particle.getBestFitness()) {
                    particle.setBestSolution(solution);
                    particle.setBestFitness(actualFitness);
                }
            }
        }
        System.out.println("Global best solution="+ globalBest.getSolution()+"\nGlobal best fitness="+ globalBest.getBestFitness());
    }
    private ArrayList<Velocity> createBasicSwapSequence(Particle particle, ArrayList<Long> bestSolution, double probability) {
        ArrayList<Velocity> basicSwapSequence = new ArrayList<>();
        ArrayList<Long> actualSolution = new ArrayList<>(particle.getSolution());
        for (int i = 0; i < numCities; i++) {
            if(!bestSolution.get(i).equals(actualSolution.get(i))) {
                Velocity swapOperator = new Velocity(i, actualSolution.indexOf(bestSolution.get(i)), probability);

                Long aux = actualSolution.get(swapOperator.getX1());
                actualSolution.set(swapOperator.getX1(), actualSolution.get(swapOperator.getX2()));
                actualSolution.set(swapOperator.getX2(), aux);

                if( Math.random() < swapOperator.getProbability()) {
                    basicSwapSequence.add(swapOperator);
                }
            }
        }
        return basicSwapSequence;
    }

    private ArrayList<Long> createSolution(Particle particle, ArrayList<Velocity> velocities) {
        ArrayList<Long> newSolution = new ArrayList<>(particle.getSolution());
        for (Velocity swapOperator : velocities) {
            Long aux = newSolution.get(swapOperator.getX1());
            newSolution.set(swapOperator.getX1(), newSolution.get(swapOperator.getX2()));
            newSolution.set(swapOperator.getX2(), aux);
        }
        return newSolution;
    }

    public ArrayList<Particle> initializeSwarm() {
        ArrayList<Particle> swarm = new ArrayList<>();
        ArrayList<Long> tour = new ArrayList<>();

        for (int i = 0; i < numCities; i++) {
            tour.add((long) i);
        }

        for (int i = 0; i < swarmSize; i++) {
            Collections.shuffle(tour);
            long fitness = calcFitnessTour(tour);
            Particle p = new Particle(new ArrayList<>(tour), fitness);
            swarm.add(p);
        }
        return swarm;
    }

    private long calcFitnessTour(ArrayList<Long> tour) {
        int startCity = tour.get(0).intValue();
        int lastCity = tour.get(numCities - 1).intValue();
        long distanceFirstAndLastCity = distanceWeight[startCity][lastCity];
        long totalDistance = 0;
        for (int i = 0; i < numCities - 1; i++) {
            totalDistance += distanceWeight[tour.get(i).intValue()][tour.get(i + 1).intValue()];
        }
        return  distanceFirstAndLastCity + totalDistance;
    }

    public Particle getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(Particle globalBest) {
        this.globalBest = globalBest;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

}
