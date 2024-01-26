import java.util.ArrayList;
/**
 * In the lake class, there are variables for each lake object that hold the name of that lake, its coordinates, how much money is accumulated in those coordinates, and the volume of the total accumulated money.
 * @author Kerem Bozkurt, Student ID: 2020400177
 * @since Date: 09.05.2023
 */
public class Lake {
    public ArrayList<ArrayList<Integer>> coordinates;
    public ArrayList<Integer> volumeValues;
    public String name;
    public double volume;


    public Lake(ArrayList<ArrayList<Integer>> coordinates,ArrayList<Integer> volumeValues) {
        // constructor creates coordinates and volume values in that coordinate of lake
        // then, help of findVolume calculate the total volume of accumulated money
        this.coordinates = (ArrayList<ArrayList<Integer>>) coordinates.clone();
        this.volumeValues= (ArrayList<Integer>)volumeValues.clone();
        findVolume();
    }
    public void findVolume(){
        // find the total volume of accumulated money
        double sum=0;
        for (int i=0;i<volumeValues.size();i++){
            sum+=volumeValues.get(i);
        }
        this.volume=Math.pow(sum,0.5);
    }
}
