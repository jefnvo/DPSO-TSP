import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        TSPReader reader = new TSPReader();
        Long[][] distanceWeight = reader.asymmetricMatrix("src/main/resources/ft53.atsp.txt");
        DiscretePSO pso = new DiscretePSO(reader.getNumCities(), 200, 0.9, 0.1, distanceWeight,  2000);
        pso.execute();
    }
}
