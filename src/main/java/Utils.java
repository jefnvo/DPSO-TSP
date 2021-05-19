import java.util.ArrayList;
public class Utils {
    public static ArrayList<Velocity> getListOfEdgeExchanges(ArrayList<Long> tour1, ArrayList<Long> tour2){
        ArrayList<Velocity> list = new ArrayList<>();
        if( tour1.equals(tour2)){
            return list;
        }else if( tour1.size() != tour2.size() ){
            return list;
        }else if( isEquivalent( tour1, tour2 ) ){
            return list;
        }

        while( !isEquivalent( tour1, tour2 ) ){
            for( int i = 0; i < tour1.size(); i++ ) {
                if(!tour1.get(i).equals(tour2.get(i))){
                    int idx = indexOf(tour1, tour2.get(i));
                    tour1 = reverse(new ArrayList<>(tour1), i, idx );
                    list.add( new Velocity(i, idx, 0));
                }
            }
        }

        return list;
    }

    private static int indexOf(ArrayList<Long> self, Long value){
        int i = 0;
        for(; i < self.size() && !self.get(i).equals(value); i++);
        return i;
    }


    private static boolean isEquivalent(ArrayList<Long> self, ArrayList<Long> other) {
        // two equivalent tours must have the same size()
        if( self.size() != other.size() ){
            return false;
        }

        // find index of matching node
        int startingIndex = -1;

        for( int i = 0; i < self.size(); i++ ){
            if(self.get(0).equals(other.get(i))){
                startingIndex = i;
                break;
            }
        }

        // if for some reason no matching id was found
        if( startingIndex == -1 ){
            return false;
        }

        // scan one direction to see if tours are equal
        boolean isEqual = true;

        for( int i = 0; i < self.size(); i++ ){
            if(!self.get(i).equals(other.get((startingIndex + i) % other.size()))){
                isEqual = false;
                break;
            }
        }

        // if necessary, scan the other direction to see if tours are equal
        if( !isEqual ){
            isEqual = true;

            for( int i = 0; i < self.size(); i++ ){
                if(!self.get(i).equals(other.get((startingIndex - i + other.size()) % other.size()))){
                    isEqual = false;
                    break;
                }
            }
        }

        return isEqual;
    }

    private static ArrayList<Long> reverse(ArrayList<Long> self, int i, int j) {
        while( j < i ){
            j += self.size();
        }

        for( int k = 0; k < (j - i + 1) / 2; k++ ){
            Long temp = self.get(i+k);
            self.set((i+k) % self.size(), self.get((j-k) % self.size()));
            self.set((j-k) % self.size(), temp);
        }
        return self;
    }

    public static ArrayList<Long> applyEdgeExchanges(ArrayList<Long> tour, ArrayList<Velocity> exchanges){
        for(Velocity exchange: exchanges){
            tour = new ArrayList<>(reverse( new ArrayList<>(tour), exchange.getX1(), exchange.getX2() ));
        }
        return tour;
    }

    protected static ArrayList<Velocity> EdgeExchangesByConstant(ArrayList<Velocity> exchanges, double constant){
        int idx = (int) Math.round(exchanges.size() * constant);
        ArrayList<Velocity> tmp = new ArrayList<>();
        for(int i = 0; i < idx; i++){
            tmp.add( exchanges.get( i ) );
        }
        return tmp;
    }

    protected static ArrayList<Velocity> EdgeExchangesAddition(ArrayList<Velocity> exchanges1, ArrayList<Velocity> exchanges2){
        ArrayList<Velocity> list = new ArrayList<>( exchanges1.size() + exchanges2.size() );
        list.addAll( exchanges1 );
        list.addAll( exchanges2 );
        return list;
    }
}
