import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

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
                Random rnd = new Random();

                double rLoc = rnd.nextDouble();
                double rGlob = rnd.nextDouble();

                ArrayList<Long> globalBestSolution = new ArrayList<>(globalBest.getBestSolution());
                ArrayList<Long> particleBestSolution = new ArrayList<>(particle.getBestSolution());


                //dLoc = Xi(t) + rLoc * bLoc * (Pi - Xi(t))
                ArrayList<Velocity> dLocExchanges = getAttractor( new ArrayList<>(particle.getSolution()), particleBestSolution, rLoc * alfa );
                ArrayList<Long> dLoc = Utils.applyEdgeExchanges(new ArrayList<>(particle.getSolution()), dLocExchanges);

                // dGlob = Xi(t) + rGlob * bGlob * (Pglob - Xi(t))
                ArrayList<Velocity> dGlobalExchanges = getAttractor(new ArrayList<>(particle.getSolution()), globalBestSolution, rGlob*beta);
                ArrayList<Long> dGlob = Utils.applyEdgeExchanges(new ArrayList<>(particle.getSolution()), dGlobalExchanges);

                // Vrand = rRand * bRand * (pRand - Xi(t))
                double bRand = rnd.nextDouble();
                double rRand = rnd.nextDouble();
                ArrayList<Long> pRand = createRandomSolution(particle);
                ArrayList<Velocity> vRand = getAttractor(new ArrayList<>(particle.getSolution()), pRand, bRand * rRand);

                // Xi(t+1) = dGlob + 1/2 * (dLoc - dGlob) + Vrand
                dGlob = Utils.applyEdgeExchanges(dGlob, Utils.EdgeExchangesAddition(getAttractor( dGlob, dLoc, 0.5 ), vRand));

                particle.setSolution(dGlob);
                Long actualFitness = calcFitnessTour(dGlob);
                particle.setFitness(actualFitness);

                if(actualFitness < particle.getBestFitness()) {
                    particle.setBestSolution(dGlob);
                    particle.setBestFitness(actualFitness);
                }
            }
        }
        System.out.println("Global best fitness="+ globalBest.getBestFitness());
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

    private ArrayList<Velocity> getAttractor(ArrayList<Long> solution, ArrayList<Long> attractor, double cte){
        // (rLoc * bLoc) * (Pi - Xi(t))
        return Utils.EdgeExchangesByConstant( Utils.getListOfEdgeExchanges( attractor, solution ), cte );
    }
}
