import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String[] benchs = new String[] {
            "src/main/resources/dantzig42.tsp.txt",
            "src/main/resources/fri26.tsp.txt",
            "src/main/resources/gr21.tsp.txt",
            "src/main/resources/ft53.atsp.txt",
            "src/main/resources/ftv33.atsp.txt",
            "src/main/resources/ftv38.atsp.txt"};
        TSPReader reader = new TSPReader();
        Long[][] distanceWeight;
        for(int i =0; i< benchs.length; i++) {
            System.out.println(benchs[i]);
            if(i<3) {
                distanceWeight = reader.symmetricMatrix(benchs[i]);
            } else {
                distanceWeight = reader.asymmetricMatrix(benchs[i]);
            }
            DiscretePSO pso = new DiscretePSO(reader.getNumCities(), 150, 0.5, 0.5, distanceWeight,  1000);
            pso.execute();
            System.out.println("\n");

        }
    }
}
