import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class DiscretePSO {
    int numCities; //d-dimensional
    int swarmSize; //quantidade de particulas
    int iterations;
    double alfa;
    double beta;
    Particle gBest;
    Long[][] distanceWeight; //para calculo da função objetivo

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
            gBest = swarm.stream()
                .min(Comparator.comparing(Particle::getFitness))
                .orElseThrow(NoSuchElementException::new);

            for (Particle particle : swarm) {
                particle.setVelocity( new ArrayList<>());
                ArrayList<Velocity> tmpVelocity = new ArrayList<>();

                ArrayList<Long> gBestSolution = gBest.getpBest();
                ArrayList<Long> pBestSolution = particle.getpBest();
                ArrayList<Long> actualSolution = particle.getSolution();

                for (int j = 0; j < numCities; j++) {
                    if(!actualSolution.get(j).equals(pBestSolution.get(j))) {
                        Velocity swapOperator = new Velocity(j, pBestSolution.indexOf(actualSolution.get(j)), alfa);

                        tmpVelocity.add(swapOperator);

                        Long aux = pBestSolution.get(swapOperator.getX1());
                        pBestSolution.set(swapOperator.getX1(), pBestSolution.get(swapOperator.getX2()));
                        pBestSolution.set(swapOperator.getX2(), aux);
                    }
                }
                for (int j = 0; j < numCities; j++) {
                    if(!actualSolution.get(j).equals(gBestSolution.get(j))) {
                        Velocity swapOperator = new Velocity(j, gBestSolution.indexOf(actualSolution.get(j)), beta);

                        tmpVelocity.add(swapOperator);

                        Long aux = gBestSolution.get(swapOperator.getX1());
                        gBestSolution.set(swapOperator.getX1(), gBestSolution.get(swapOperator.getX2()));
                        gBestSolution.set(swapOperator.getX2(), aux);
                    }
                }
                particle.setVelocity(tmpVelocity);
                for (Velocity swapOperator : tmpVelocity) {
                    if(Math.random() <= swapOperator.getProbability()) {
                        Long aux = actualSolution.get(swapOperator.getX1());
                        actualSolution.set(swapOperator.getX1(), actualSolution.get(swapOperator.getX2()));
                        actualSolution.set(swapOperator.getX2(), aux);
                    }
                }
                particle.setSolution(actualSolution);
                //TODO: Update cost new solution based on distance matrix
                Long actualFitness;

                if(actualFitness < particle.getBestFitness()) {
                    particle.setpBest(actualSolution);
                    particle.setBestFitness(actualFitness);
                }
            }
        }
        
    }

    //passo 1
    public ArrayList<Particle> initializeSwarm() {
        ArrayList<Particle> swarm = new ArrayList<>();
        ArrayList<Long> tour = new ArrayList<>();

        for (int i = 0; i < numCities; i++) {
            tour.add((long) i);
        }

        for (int i = 0; i < swarmSize; i++) {
            ArrayList<Long> newTour = new ArrayList<>();
            Collections.shuffle(newTour);
            ArrayList<Velocity> velocity = new ArrayList<>();
            //TODO: calc the cost of tour based on distance weight
            Particle p = new Particle(tour, velocity, 0L);
            swarm.add(p);
        }
        return swarm;
    }

    public Particle getgBest() {
        return gBest;
    }

    public void setgBest(Particle gBest) {
        this.gBest = gBest;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
