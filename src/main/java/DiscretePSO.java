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
                double bRand = Math.random();

                ArrayList<Long> globalBestSolution = new ArrayList<>(globalBest.getBestSolution());
                ArrayList<Long> particleBestSolution = new ArrayList<>(particle.getBestSolution());


                //create dloc
                ArrayList<Velocity> dLocal = createBasicSwapSequence(particle, particleBestSolution, alfa);
                ArrayList<Long> distanceLoc = createSolution(particle, dLocal);

                //create dglob
                ArrayList<Velocity> dGlobal =  createBasicSwapSequence(particle, globalBestSolution, beta);
                ArrayList<Long> distanceGlob = new ArrayList<>(createSolution(particle, dGlobal));

                //create vrand
                ArrayList<Long> randomSolution = createRandomSolution(particle);
                ArrayList<Velocity> destinationRandomVelocity =  createBasicSwapSequence(particle, randomSolution, bRand);

                //dloc - dglob
                ArrayList<Velocity> basicSwapSequenceLocGlob = new ArrayList<>();
                for (int j = 0; j < numCities; j++) {
                    if(!distanceLoc.get(j).equals(distanceGlob.get(j))) {
                        Velocity swapOperator = new Velocity(j, distanceGlob.indexOf(distanceLoc.get(j)), 0);

                        Long aux = distanceGlob.get(swapOperator.getX1());
                        distanceGlob.set(swapOperator.getX1(), distanceGlob.get(swapOperator.getX2()));
                        distanceGlob.set(swapOperator.getX2(), aux);

                        basicSwapSequenceLocGlob.add(swapOperator);
                    }
                }

                //0.5 * (dloc - dglob) + vrand
                ArrayList<Velocity> halfDistance = new ArrayList<>();
                int halfArrayVelocity = (int) Math.floor(0.5*basicSwapSequenceLocGlob.size());
                for(int j = 0; j<halfArrayVelocity; j++) {
                    halfDistance.add(basicSwapSequenceLocGlob.get(j));
                }
                ArrayList<Velocity> halfDistancePlusRandVelocity = new ArrayList<>(halfDistance);
                halfDistancePlusRandVelocity.addAll(destinationRandomVelocity);

                //dglob + 0.5*(dloc-dglob) + vrand
                for (Velocity swapOperator : halfDistancePlusRandVelocity) {
                    Long aux = distanceGlob.get(swapOperator.getX1());
                    distanceGlob.set(swapOperator.getX1(), distanceGlob.get(swapOperator.getX2()));
                    distanceGlob.set(swapOperator.getX2(), aux);
                }

                particle.setSolution(distanceGlob);
                Long actualFitness = calcFitnessTour(distanceGlob);
                particle.setFitness(actualFitness);

                if(actualFitness < particle.getBestFitness()) {
                    particle.setBestSolution(distanceGlob);
                    particle.setBestFitness(actualFitness);
                }
            }
        }
        System.out.println("Global best solution="+ globalBest.getSolution()
            +"\nGlobal best fitness="+ globalBest.getBestFitness());
    }

    private ArrayList<Long> createRandomSolution(Particle particle) {
        ArrayList<Long> actualSolution = new ArrayList<>(particle.getSolution());
        Collections.shuffle(actualSolution);
        return actualSolution;
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
