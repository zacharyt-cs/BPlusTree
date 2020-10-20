import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static int counter=0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        // Main Menu
        int first_option;
        do {
            System.out.println(
                "Enter one of the following numbers\n" + 
                "1. Set block size to 100 B\n" + 
                "2. Set block size to 500 B\n" +
                "0. Exit"
                );
            first_option = input.nextInt();
            switch(first_option){
                case 1:    
                // initialize block size = 100 B
                    runExperiments(100);
                break;

                case 2:
                // initialize block size = 500 B
                    runExperiments(500);
                break;

                case 0:
                // terminate
                break;

                default:
                System.out.println("Invalid option! Please try again.");
                break;
            }
        }
        while(first_option != 0);        
    }

    private static void runExperiments(int blocksize) {
        int second_option;
        int n = blocksize/4;
        BPlusTree tree = new BPlusTree(n);
        
        do {
            System.out.println(
                "Enter one of the following numbers:\n" + 
                "1) Run Experiment 1\n" +
                "2) Run Experiment 2\n" +
                "3) Run Experiment 3\n" +
                "4) Run Experiment 4\n" +
                "5) Run Experiment 5\n" +
                "0 to change block size");
            second_option = input.nextInt();
            switch(second_option){
                case 1:
                // run Exp 1
                    System.out.println("--------------------Experiment 1 and 2----------------------");
                    List<Blocks> blo = new ArrayList<Blocks>();
                        
                    
                    FileIO.readTSV("data.tsv", tree, blo);


                    // one data block is equal to 100
                    System.out.println("The number of blocks "+ blo.size());
                    System.out.println("The size of database "+blo.size()*100);
                    

                    // int No_of_nodes= tree.printTree();
                    // need multply by node size
                    // int calculateSize;
                    // my degree is 4 so 8 *4 bytes, Since is 8
                    // calculateSize= No_of_nodes* 96;
                break;

                case 2:
                // run Exp 2
                System.out.println("run ex 2");
                break;

                case 3:
                // Experiment 3
                System.out.println("------------------Experiment 3-----------------");
                List<Records> tconstValue= tree.search(8);
                for(int i= 0; i<tconstValue.size();i++)
                {
                    //limit the max output to be 20 entries due to the huge amount of data
                    if(i<=20){
                        //printing of tconst value
                        System.out.println(" The t constant value is " + tconstValue.get(i).tconstant);
                    }
                    else{
                        break;
                    }
                }
                // Print out the number of blocks and the block data
                int blockCounter=0;
                for(int j=0; j<blo.size();j++){
                    List<Records> dataBlo =	blo.get(j).recordlist;
                
                    for(int a=0;a<dataBlo.size();a++){
                        if(dataBlo.get(a).averagerating==8){
                            blockCounter++;
                            for(int b=0; b<dataBlo.size();b++){
                                Records r = dataBlo.get(b);
                                System.out.println("Tconst is "+ r.tconstant + " Average Rating is "+ r.averagerating + " Number of votes is "+ r.numofvote);
                                
                            }
                            break;
                        }
                    }
                }
                System.out.print("Number of blocks: " + blockCounter);
                        break;

                case 4:
                // run Expr 4
                    System.out.println("------Experiment 4--------------");
                    List<Keys> test= tree.search(7,9); 
                    for(int d=0; d<test.size(); d++){
                        Keys keys = test.get(d);
                        float key = keys.key;
                        List<Records> records = keys.values;
                        System.out.println("key,");
                        for (Records record : records){
                            System.out.println(key+", "+record.tconstant+", "+record.numofvote);
                        }
                    }
                break;

                case 5:
                // run Expr 5
                System.out.println("run ex 5");
                break;
            }
        } while (second_option != 0);
    }
}

    

