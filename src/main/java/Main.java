import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        TSPReader reader = new TSPReader();
        Long[][] distanceWeight = reader.asymmetricMatrix("src/main/resources/ft53.atsp.txt");
        DiscretePSO pso = new DiscretePSO(reader.getNumCities(), 150, 0.85, 0.15, distanceWeight,  1000);
        pso.execute();
    }
}
