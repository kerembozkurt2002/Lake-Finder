import java.util.ArrayList;
/**
 * In the terrain class, the row, column, base, how many modifications have been made and the final version of our table are kept. In addition, operations such as applying modifications, printing the table, and creating the final state table are also done through methods.
 * @author Kerem Bozkurt
 * @since Date: 09.05.2023
 */
public class Terrain {
    public ArrayList<ArrayList<Integer>> table;
    public int row;
    public int column;
    public ArrayList<String> base;
    public int modification=1;
    public ArrayList<ArrayList<String>> finalTable;

    public Terrain(ArrayList<ArrayList<Integer>> table,int row,int column){
        // constructor with row, column, table information and makes the base
        this.row=row;
        this.column=column;
        this.table=table;
        this.base=makeBase(row);
    }


    public void addModification() {
        this.modification+=1;
    } // add modifications variable

    public void addStone(String temp){
        // add modifications to table if it is a valid modification
        String [] inputCoordinate=temp.split("");
        int index=-1;
        for (int i=0;i<inputCoordinate.length;i++){
            try {
                int numStart=Integer.parseInt(inputCoordinate[i]);
                index=i;
                break;
            }
            catch (NumberFormatException e){
                continue;
            }
        }

        if (index==-1||index==0){
            System.out.println("Not a valid step!");
            System.out.println("Add stone "+modification+" / 10 to coordinate:");
        }
        else {
            try {
                String myAlphPart=temp.substring(0,index);
                String myNumPart=temp.substring(index);
                int rowIndex= base.indexOf(myAlphPart);
                int columnIndex=Integer.parseInt(myNumPart);
                int currentValue=table.get(Integer.parseInt(myNumPart)).get(rowIndex);
                ArrayList<Integer> rowList = table.get(columnIndex);
                rowList.set(rowIndex, currentValue + 1);
                table.set(columnIndex,rowList);
                addModification();
                printTerrain();
                System.out.println("---------------");
                if(modification<=10){
                    System.out.println("Add stone "+modification+" / 10 to coordinate:");
                }

            }
            catch (Exception e){
                System.out.println("Not a valid step!");
                System.out.println("Add stone "+modification+" / 10 to coordinate:");
            }
        }

    }
    public void printTerrain(){
        // prints the table in the desired form
        for (int i=0;i<table.size();i++){
            if(i<10){
                System.out.print("  ");
                System.out.print(i);
            }
            else {
                System.out.print(" ");
                System.out.print(i);
            }
            for (int i2=0;i2<table.get(0).size();i2++){

                System.out.print("  ");
                System.out.print(table.get(i).get(i2));
            }
            System.out.println(" ");
        }
        System.out.print("   ");
        for (int i=0;i<base.size();i++){
            if(i<26){
                System.out.print("  ");
                System.out.print(base.get(i));
            }
            else {
                System.out.print(" ");
                System.out.print(base.get(i));
            }
        }
        System.out.println(" ");
    }
    public void printFinalTerrain(){
        // prints the final table in the desired form
        for (int i=0;i<finalTable.size();i++){
            if(i<10){
                System.out.print("  ");
                System.out.print(i);
            }
            else {
                System.out.print(" ");
                System.out.print(i);
            }
            for (int i2=0;i2<finalTable.get(0).size();i2++){
                if (finalTable.get(i).get(i2).length()==1){
                    System.out.print("  ");
                    System.out.print(finalTable.get(i).get(i2));
                }
                else {
                    System.out.print(" ");
                    System.out.print(finalTable.get(i).get(i2));
                }
            }
            System.out.println(" ");
        }
        System.out.print("   ");
        for (int i=0;i<base.size();i++){
            if(i<26){
                System.out.print("  ");
                System.out.print(base.get(i));
            }
            else {
                System.out.print(" ");
                System.out.print(base.get(i));
            }
        }
        System.out.println(" ");
    }

    public  ArrayList<String> makeBase(int row){
        // creates the String list to use in table's base of print part
        ArrayList<String> baseOfTerrain=new ArrayList<>();
        if(row<=26) {
            for (int i = 0; i < row; i++) {
                char temp = 'a';
                int asciival = temp;
                char addItem = (char) ((char) asciival + i);
                baseOfTerrain.add(String.valueOf(addItem));
            }
            return baseOfTerrain;
        }
        else {
            for (int i = 0; i < 26; i++) {
                char temp = 'a';
                int asciival = temp;
                char addItem = (char) ((char) asciival + i);
                baseOfTerrain.add(String.valueOf(addItem));
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
                baseOfTerrain.add(tem);
            }
        }
        return baseOfTerrain;
    }
    public void makeStringTable(){
        // make the final table in a String list form
        ArrayList<ArrayList<String>> temp=new ArrayList<>();
        for (int i=0;i<table.size();i++){
            temp.add(new ArrayList<String>());
            for (int i2=0;i2<table.get(0).size();i2++){
                temp.get(i).add(String.valueOf(table.get(i).get(i2)));
            }
        }
        finalTable=temp;
    }
    public void setFinalTable(String value,int y,int x){
        // set the final table with desired values
        finalTable.get(y).set(x,value);
    }
}
