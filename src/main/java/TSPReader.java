import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TSPReader {
    int numCities;

    Long[][] distanceWeight;

    public TSPReader() {
    }

    public Long[][] symmetricMatrix(String pathname) throws FileNotFoundException {
        try (Scanner input = new Scanner(new File(pathname))) {
            // The input files follow the TSPLib "explicit" format.
            String str = new String();
            String[] pch = new String[2];
            while (true) {
                str = input.nextLine();
                pch = str.split(":");
                if (pch[0].compareTo("DIMENSION")==0) {
                    numCities = Integer.parseInt(pch[1].trim());
                    System.out.println("Number of cities = " + numCities);
                } else if (pch[0].compareTo("EDGE_WEIGHT_SECTION")==0) {
                    break;
                }
            }
            distanceWeight = new Long[numCities][numCities];
            // Distance from i to j
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < i+1; j++) {
                    distanceWeight[i][j] = (long) input.nextInt();
                    distanceWeight[j][i] = distanceWeight[i][j];
                }
            }
        }
        return distanceWeight;
    }

    public Long[][] asymmetricMatrix(String pathname) throws FileNotFoundException {
        try (Scanner input = new Scanner(new File(pathname))) {
            // The input files follow the TSPLib "explicit" format.
            String str = new String();
            String[] pch = new String[2];
            while (true) {
                str = input.nextLine();
                pch = str.split(":");
                if (pch[0].compareTo("DIMENSION")==0) {
                    numCities = Integer.parseInt(pch[1].trim());
                    System.out.println("Number of cities = " + numCities);
                } else if (pch[0].compareTo("EDGE_WEIGHT_SECTION")==0) {
                    break;
                }
            }
            distanceWeight = new Long[numCities][numCities];
            // Distance from i to j
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    distanceWeight[i][j] = (long) input.nextInt();
                }
            }
        }
        return distanceWeight;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    String pathname;

    public int getNumCities() {
        return numCities;
    }

    public void setNumCities(int numCities) {
        this.numCities = numCities;
    }

    public Long[][] getDistanceWeight() {
        return distanceWeight;
    }

    public void setDistanceWeight(Long[][] distanceWeight) {
        this.distanceWeight = distanceWeight;
    }
}

