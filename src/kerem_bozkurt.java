import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
/**
 * In the main class, reading the information from the text file, creating a table, then taking input from the console, making the necessary modifications, and then with the help of Terrain and Lake classes, calculating the points at which money is collected through the BFS searching algorithm logic and the volume of the total money collected.
 * @author Kerem Bozkurt, Student ID: 2020400177
 * @since Date: 09.05.2023
 */

public class kerem_bozkurt {
    public static void main(String[] args) throws FileNotFoundException {
        // read input from a file
        File file = new File("C:\\Users\\Kerem\\IdeaProjects\\Radgeat Game\\src\\input.txt");
        Scanner text = new Scanner(file);
        // read the first line to get the dimensions of the terrain
        String[] rowAndColumn = text.nextLine().split(" ");
        int row = Integer.parseInt(rowAndColumn[0]);
        int column = Integer.parseInt(rowAndColumn[1]);
        // create a table from the input text
        ArrayList<ArrayList<Integer>> table = makeTableFromText(column,text);
        // create a Terrain object from the table and row/column values
        Terrain map = new Terrain(table, row, column);

        // make modifications to the terrain according to the user input
        makeModifications(text,map);

        // find the maximum height in the terrain
        int maxHeight=findTheMax(map.table);
        // create a 3D boolean list to keep track of the leakages in each layer
        ArrayList<ArrayList<ArrayList<Boolean>>> visitedLeakageLists=make3dBooleanList(map.table,maxHeight);
        // create a  3D Integer list of layers, where each layer contains which coordinate's is wall or money if there is no leakage
        ArrayList<ArrayList<ArrayList<Integer>>> layerList=makeLayerList(map.table,maxHeight);
        // mark all leakages in each layer
        marksAllLayersLeakages(visitedLeakageLists,layerList);
        // create a 2D Integer list that has the information of which coordinates are leaked or contains money
        ArrayList<ArrayList<Integer>> finalForm=finalForm(visitedLeakageLists,layerList);
        // create a 2D boolean list that represents the final form of the terrain after all leakages
        ArrayList<ArrayList<Boolean>> finalFormTF=make2dBooleanList(map.table);

        // create a list of lakes to keep lake objects
        ArrayList<Lake> lakesList=new ArrayList<>();
        makeLakes(lakesList,finalForm,finalFormTF);
        // put the lakes' names according to ASCII values of letters
        setLakesNames(lakesList);
        // create String list of terrain to put the names of lakes
        map.makeStringTable();
        // add the names of the lakes to their coordinates in terrain
        setTerrainLakeNames(map,lakesList);
        // print final terrain
        map.printFinalTerrain();
        // print total volume of lakes' volumes
        printFinalVolume(lakesList);

    }
    public static ArrayList<ArrayList<Integer>> makeTableFromText(int column, Scanner text){
        // make the 2D Integer list for the table from reading the text
        ArrayList<ArrayList<Integer>> table = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < column; i++) {
            String[] tempt = text.nextLine().split(" ");
            ArrayList<Integer> temptList = new ArrayList<>();
            for (String num : tempt) {
                temptList.add(Integer.parseInt(num));
            }
            table.add(temptList);
        }
        return table;
    }
    public static void makeModifications(Scanner text, Terrain map){
        // make modifications according to input values with help of Terrain class' method of addStone
        map.printTerrain();
        System.out.println("Add stone 1 / 10 to coordinate:");
        while (map.modification<11){
            Scanner inp=new Scanner(System.in);
            String change=inp.nextLine();
            map.addStone(change);
        }
    }


    public static int findTheMax(ArrayList<ArrayList<Integer>> table){
        // finds the maximum Integer value in table
        int max=-1;
        for (int i=0;i<table.size();i++){
            for (int i2=0;i2<table.get(0).size();i2++){
                if(table.get(i).get(i2)>max){
                    max=table.get(i).get(i2);
                }
            }
        }
        return max;
    }
    public static ArrayList<ArrayList<Integer>> moneyLayer(ArrayList<ArrayList<Integer>> table, int num){
        // creates a 2D ArrayList that represents a layer of money and wall at a specified height.
        ArrayList<ArrayList<Integer>> temp= new ArrayList<ArrayList<Integer>>();
        for (int i=0;i<table.size();i++){
            ArrayList<Integer> tempList=new ArrayList<>();
            for (int i2=0;i2<table.get(0).size();i2++) {
                int tempValue=num-table.get(i).get(i2);
                if (tempValue<0){
                    tempList.add(0);
                }
                else {
                    tempList.add(tempValue);
                }
            }
            temp.add(tempList);
        }
        return temp;
    }
    public static ArrayList<ArrayList<ArrayList<Integer>>> makeLayerList(ArrayList<ArrayList<Integer>> table,int num){
        // creates a 3D ArrayList to keep all layers
        ArrayList<ArrayList<ArrayList<Integer>>> layerList=new ArrayList<ArrayList<ArrayList<Integer>>>();
        for(int i=num;i>=1;i--){
            layerList.add(moneyLayer(table,i));
        }
        return layerList;
    }
    public static ArrayList<ArrayList<Boolean>>make2dBooleanList(ArrayList<ArrayList<Integer>> table){
        // create a 2D boolean List to use in BFS leakage test
        ArrayList<ArrayList<Boolean>> list=new ArrayList<ArrayList<Boolean>>();
        int y=table.size();
        int x=table.get(0).size();
        for (int i=0;i<y;i++){
            list.add(new ArrayList<Boolean>());
            for (int i2=0;i2<x;i2++){
                list.get(i).add(false);
            }
        }
        return list;
    }


    public static ArrayList<ArrayList<ArrayList<Boolean>>> make3dBooleanList(ArrayList<ArrayList<Integer>> table,int num){
        // create a 3D boolean List to keep all layers information after the leakages
        ArrayList<ArrayList<ArrayList<Boolean>>> booleanLists=new ArrayList<ArrayList<ArrayList<Boolean>>>();
        for (int i=0;i<num;i++){
            booleanLists.add(new ArrayList<ArrayList<Boolean>>());
            for (int i2=0;i2<table.size();i2++){
                booleanLists.get(i).add(new ArrayList<Boolean>());
                for (int i3=0;i3<table.get(0).size();i3++){
                    booleanLists.get(i).get(i2).add(false);
                }
            }
        }
        return booleanLists;
    }
    public static void bfsLeakage(ArrayList<ArrayList<Integer>> table,ArrayList<ArrayList<Boolean>> visitedList, int row, int col) {
        // implements a breadth-first search algorithm to find the leakages on the selected coordinate in map and marks the visited cells in a visitedList
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        visitedList.get(row).set(col, true);
        ArrayList<Integer> queue = new ArrayList<>();
        queue.add(row);
        queue.add(col);
        while (!queue.isEmpty()) {
            int x = queue.remove(0);
            int y = queue.remove(0);

            for (int i = 0; i < 8; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= 0 && ny >= 0 && nx < table.size() && ny < table.get(0).size() && !visitedList.get(nx).get(ny) && table.get(nx).get(ny) > 0) {
                    visitedList.get(nx).set(ny, true);
                    queue.add(nx);
                    queue.add(ny);

                }
            }
        }
    }
    public static void bfsLake(ArrayList<ArrayList<Integer>> table,ArrayList<ArrayList<Boolean>> visitedList, int row, int col,ArrayList<ArrayList<Integer>> lakeList,ArrayList<Integer> volumeValues){
        // implements a breadth-first search algorithm to find a lake starting from a given point and marks the visited cells in a visitedList
        // also keep the coordinates in a lakeList and volumeValues to use when the total volume has to be calculated and lakes' names has to be put in coordinates

        ArrayList<Integer> lakeCoordinate=new ArrayList<>();
        lakeCoordinate.add(row);
        lakeCoordinate.add(col);
        lakeList.add(lakeCoordinate);
        volumeValues.add(table.get(row).get(col));

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        visitedList.get(row).set(col,true);
        ArrayList<Integer> queue = new ArrayList<>();
        queue.add(row);
        queue.add(col);
        while (!queue.isEmpty()) {
            int x = queue.remove(0);
            int y = queue.remove(0);
            for (int i = 0; i < 8; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if((table.get(nx).get(ny)>0) && (!visitedList.get(nx).get(ny))){
                    visitedList.get(nx).set(ny,true);
                    queue.add(nx);
                    queue.add(ny);

                    ArrayList<Integer> temp=new ArrayList<>();
                    temp.add(nx);
                    temp.add(ny);
                    lakeList.add(temp);
                    volumeValues.add(table.get(nx).get(ny));
                }
            }
        }
    }


    public static ArrayList<ArrayList<Integer>> findTheBorders(ArrayList<ArrayList<Integer>> table){
        // creates a 2D Integer list to keep the borders coordinates for leakage test
        ArrayList<ArrayList<Integer>>borders=new ArrayList<>();
        int row=table.size();
        int column=table.get(0).size();

        for (int i=0;i<column;i++){
            ArrayList<Integer> temp=new ArrayList<>();
            ArrayList<Integer> temp2=new ArrayList<>();
            temp.add(0);        temp.add(i);    borders.add(temp);
            temp2.add(row-1);   temp2.add(i);   borders.add(temp2);
        }
        for (int i=1;i<(row-1);i++){
            ArrayList<Integer> temp=new ArrayList<>();
            ArrayList<Integer> temp2=new ArrayList<>();
            temp.add(i);        temp.add(0);    borders.add(temp);
            temp2.add(i);   temp2.add(column-1);   borders.add(temp2);
        }
        return borders;
    }
    public static void markTheLeakages(ArrayList<ArrayList<Integer>>borders,ArrayList<ArrayList<Boolean>> visitedList,ArrayList<ArrayList<Integer>>layer){
        // marks the leakages using the bfsLeakage function with given a list of borders and a visitedList
        for (int i=0;i<borders.size();i++){
            int y=borders.get(i).get(0); int x=borders.get(i).get(1);
            if(!(visitedList.get(y).get(x))&&(layer.get(y).get(x)>0)){
                bfsLeakage(layer,visitedList,y,x);
            }
        }
    }
    public static void marksAllLayersLeakages(ArrayList<ArrayList<ArrayList<Boolean>>> visitedLeakageLists,ArrayList<ArrayList<ArrayList<Integer>>> layerList){
        // marks all leakages in all layers using markTheLeakages method
        for (int i=0;i<visitedLeakageLists.size();i++){
            ArrayList<ArrayList<Boolean>> tempVisited=visitedLeakageLists.get(i);
            ArrayList<ArrayList<Integer>> tempLayer=layerList.get(i);
            markTheLeakages(findTheBorders(tempLayer),tempVisited,tempLayer);
        }

    }
    public static ArrayList<ArrayList<Integer>> finalForm(ArrayList<ArrayList<ArrayList<Boolean>>> visitedLeakageLists,ArrayList<ArrayList<ArrayList<Integer>>> layerList){
        // creates a final map with money volumes of all layers
        // if money can accumulate at the selected coordinate, how much money is accumulated, otherwise "0" is written
        ArrayList<ArrayList<Integer>> finalMap=new ArrayList<ArrayList<Integer>>();
        int y=visitedLeakageLists.get(0).size();
        int x=visitedLeakageLists.get(0).get(0).size();
        for (int i=0;i<y;i++){
            finalMap.add(new ArrayList<Integer>());
            for (int i2=0;i2<x;i2++){
                finalMap.get(i).add(0);
            }
        }
        for (int i=0;i<layerList.size();i++){
            for (int i2=0;i2<layerList.get(0).size();i2++){
                for (int i3=0;i3<layerList.get(0).get(0).size();i3++){
                    if((layerList.get(i).get(i2).get(i3)>0) && (!visitedLeakageLists.get(i).get(i2).get(i3))){
                        int temp=finalMap.get(i2).get(i3)+1;
                        finalMap.get(i2).set(i3,temp);
                    }
                }
            }
        }
        return finalMap;
    }
    public static void makeLakes(ArrayList<Lake> lakesList,ArrayList<ArrayList<Integer>> finalForm,ArrayList<ArrayList<Boolean>> finalFormTF){
        // find the coordinates of lakes and their volumes after that creates and Lake object with this information and add that object to lakesList

        ArrayList<Integer> volumeValues=new ArrayList<>();
        ArrayList<ArrayList<Integer>> lakeList=new ArrayList<ArrayList<Integer>>();
        for (int i=1;i<finalForm.size()-1;i++){
            for (int i2=1;i2<finalForm.get(0).size()-1;i2++){
                if((finalForm.get(i).get(i2)>0) && (!finalFormTF.get(i).get(i2))){
                    bfsLake(finalForm,finalFormTF,i,i2,lakeList,volumeValues);
                    lakesList.add(new Lake(lakeList,volumeValues));
                    volumeValues.clear();
                    lakeList.clear();
                }
            }
        }
    }
    public static ArrayList<String> makeLakeNames(int row) {
        // create a String list to keep the lakes' names and find the lakes' names according to ASCII value of letters
        ArrayList<String> lakeNames = new ArrayList<>();
        if (row <= 26) {
            for (int i = 0; i < row; i++) {
                char temp = 'a';
                int asciival = temp;
                char addItem = (char) ((char) asciival + i);
                lakeNames.add(String.valueOf(addItem).toUpperCase());
            }
            return lakeNames;
        } else {
            for (int i = 0; i < 26; i++) {
                char temp = 'a';
                int asciival = temp;
                char addItem = (char) ((char) asciival + i);
                lakeNames.add(String.valueOf(addItem).toUpperCase());
            }
            int rowNew = row - 26;
            int k = -1;
            char temp = 'a';
            int asciival = temp;
            for (int i = 0; i < rowNew; i++) {
                if (i % 26 == 0) {
                    k += 1;
                }
                char addItem = (char) ((char) asciival + k);
                char addItem2 = (char) ((char) asciival + i - (k * 26));
                String tem = String.valueOf(addItem);
                if (addItem2 <= 'z') {
                    tem += String.valueOf(addItem2);
                } else {
                    k += 1;
                    tem += 'a';
                    addItem2 = (char) ((char) asciival + i - (k * 26));
                    tem += String.valueOf(addItem2);
                }
                lakeNames.add(tem.toUpperCase());
            }
        }
        return lakeNames;
    }

    public static void setLakesNames(ArrayList<Lake> lakesList){
        // add the lakes' names to their objects in lakesList from lakesNamesList String list
        ArrayList<String> lakesNamesList=makeLakeNames(lakesList.size());
        for (int i=0;i<lakesNamesList.size();i++){
            lakesList.get(i).name=lakesNamesList.get(i);
        }
    }
    public static void setTerrainLakeNames(Terrain map,ArrayList<Lake> lakesList){
        // add the names of lakes in their coordinates in final form of the map
        for (Lake lake: lakesList){
            for (int i=0;i<lake.coordinates.size();i++){
                map.setFinalTable(lake.name,lake.coordinates.get(i).get(0),lake.coordinates.get(i).get(1));
            }
        }
    }
    public static void printFinalVolume(ArrayList<Lake> lakesList){
        // calculate and print the total volume of all lakes
        double m=0;
        for (Lake mylake:lakesList){
            m+=mylake.volume;
        }
        System.out.println("Final score: "+String.format("%.2f",m));
    }
}