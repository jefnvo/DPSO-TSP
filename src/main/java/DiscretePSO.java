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
    Particle globalBest;
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
            globalBest = swarm.stream()
                .min(Comparator.comparing(Particle::getBestFitness))
                .orElseThrow(NoSuchElementException::new);

            System.out.println("Iteration="+i+"\nBestFitness="+ globalBest.getFitness()+"\n\n");

            for (Particle particle : swarm) {
                particle.setVelocity( new ArrayList<>());
                ArrayList<Velocity> tmpVelocity = new ArrayList<>();

                ArrayList<Long> globalBestSolution = new ArrayList<>(globalBest.getBestSolution());
                ArrayList<Long> particleBestSolution = new ArrayList<>(particle.getBestSolution());
                ArrayList<Long> actualSolution = new ArrayList<>(particle.getSolution());


                for (int j = 0; j < numCities; j++) {
                    if(!actualSolution.get(j).equals(particleBestSolution.get(j))) {
                        Velocity swapOperator = new Velocity(j, particleBestSolution.indexOf(actualSolution.get(j)), alfa);

                        tmpVelocity.add(swapOperator);

                        Long aux = particleBestSolution.get(swapOperator.getX1());
                        particleBestSolution.set(swapOperator.getX1(), particleBestSolution.get(swapOperator.getX2()));
                        particleBestSolution.set(swapOperator.getX2(), aux);
                    }
                }
                for (int j = 0; j < numCities; j++) {
                    if(!actualSolution.get(j).equals(globalBestSolution.get(j))) {
                        Velocity swapOperator = new Velocity(j, globalBestSolution.indexOf(actualSolution.get(j)), beta);

                        tmpVelocity.add(swapOperator);

                        Long aux = globalBestSolution.get(swapOperator.getX1());
                        globalBestSolution.set(swapOperator.getX1(), globalBestSolution.get(swapOperator.getX2()));
                        globalBestSolution.set(swapOperator.getX2(), aux);

                    }
                }
                particle.setVelocity(tmpVelocity);
                for (Velocity swapOperator : tmpVelocity) {
                    Double r = Math.random();
                    if(r <= swapOperator.getProbability()) {
                        Long aux = actualSolution.get(swapOperator.getX1());
                        actualSolution.set(swapOperator.getX1(), actualSolution.get(swapOperator.getX2()));
                        actualSolution.set(swapOperator.getX2(), aux);
                    }
                }
                particle.setSolution(actualSolution);
                Long actualFitness = calcFitnessTour(actualSolution);
                particle.setFitness(actualFitness);

                if(actualFitness < particle.getBestFitness()) {
                    particle.setBestSolution(actualSolution);
                    particle.setBestFitness(actualFitness);
                }
            }
        }
        System.out.println("Global best solution="+ globalBest.getSolution()+"\nGlobal best fitness="+ globalBest.getFitness());
    }

    //passo 1
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
