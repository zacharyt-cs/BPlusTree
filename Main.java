import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int counter = 0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // Main Menu
        int first_option;
        do {
            System.out.println("Enter one of the following numbers\n" + "1. Set block size to 100 B\n"
                    + "2. Set block size to 500 B\n" + "0. Exit");
            first_option = input.nextInt();
            switch (first_option) {
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
        } while (first_option != 0);
    }

    private static void runExperiments(int blockSize) throws IOException {
        int second_option;
        int n = blockSize/4; // assuming 1 keypair takes up 4B
        BPlusTree bPlusTree;

        Disk disk = new Disk(blockSize);
        // disk.readTSV("data(30k).tsv");
        disk.readTSV("data.tsv");
        do {
            System.out.println(
                "Enter one of the following numbers:\n" + 
                "1) Run Experiment 1\n" +
                "2) Run Experiment 2\n" +
                "3) Run Experiment 3\n" +
                "4) Run Experiment 4\n" +
                // "5) Run Experiment 5\n" +
                "0 to change block size");
            second_option = input.nextInt();
            switch(second_option){
            case 1:
                System.out.println("--------------------Experiment 1----------------------"); 
                System.out.println("=> The number of blocks = "+ disk.diskBlocks.size());
                System.out.println("=> The size of database = "+ disk.getTotalBytes() + " bytes");
                System.out.println("------------------------End---------------------------\n"); 
                break;
            case 2:
                System.out.println("--------------------Experiment 2----------------------"); 
                // Insert records into Tree
                bPlusTree = new BPlusTree(n);
                bPlusTree.buildTreeWithFromDisk(disk);
                bPlusTree.printTree();
                System.out.println("\n=> The parameter n = "+ n);
                System.out.println("=> The number of nodes = "+ bPlusTree.numofNodes);
                System.out.println("=> The height of tree = "+ bPlusTree.heightOfTree);
                System.out.println("------------------------End---------------------------\n"); 
                break;
            case 3:
                System.out.println("--------------------Experiment 3----------------------"); 
                // Retrieve tconst for averageRating==8.0
                // Find number of index nodes and show all index nodes processed
                // Find number of data blocks and show all data blocks processed
                bPlusTree = new BPlusTree(n);
                bPlusTree.buildTreeWithFromDisk(disk);
                List<Records> recordsObtained = bPlusTree.search(8);
                System.out.println("Please refer to Experiment_3.txt for the tconst of the key 8.0");
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Experiment_3"), "utf-8"))) {
                    for (Records r : recordsObtained){
                        writer.write(r.tconstant+",");
                        }
                    writer.close();
                }
                int numBlocks = bPlusTree.getNumOfBlocksAccessed(recordsObtained, disk);
                System.out.println("\n\n=> The number of blocks accessed = "+numBlocks);
                System.out.println("------------------------End---------------------------\n"); 
                break;
            case 4:
                System.out.println("--------------------Experiment 4----------------------"); 
                int rangeCounter=0;
                bPlusTree = new BPlusTree(n);
                bPlusTree.buildTreeWithFromDisk(disk);
                List<Keys> test= bPlusTree.search(7,9); 
                // print nodes accessed with the key and values                
                for(int d=0; d<test.size(); d++){
                    Keys keys = test.get(d);
                    float key = keys.key;
                    List<Records> records = keys.values;
                    // System.out.println("\nFor key "+records.get(0).averagerating+" the tconst are:");
                    // for (Records record : records){
                    //     System.out.print(record.tconstant+",");
                    //     rangeCounter++;
                    // }
                    System.out.println("Please refer to Experiment_4_"+records.get(0).averagerating+".txt for the tconst of the key "+records.get(0).averagerating);
                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Experiment_4_"+records.get(0).averagerating), "utf-8"))) {
                        for (Records record : records){
                            writer.write(record.tconstant+",");
                            rangeCounter++;
                            }
                    }    
                }
                int blockNum=0;
                int printNum=1;
                for(int g=0; g< disk.diskBlocks.size();g++){
                    List<Records> rangeBlo = disk.diskBlocks.get(g).recordlist;
                    for(int a=0;a<rangeBlo.size();a++){
                        if(rangeBlo.get(a).averagerating >= 7.0f && rangeBlo.get(a).averagerating <=9.0f){
                            blockNum++;
                            for(int b=0; b<rangeBlo.size();b++){
                                Records r = rangeBlo.get(b);
                                // System.out.println(" The block data it accessing is "+"Tconst : "+ r.tconstant + " Average Rating:  "+ r.averagerating + " Number of votes : "+ r.numofvote);
                                printNum++;
                            }
                            break;
                        }
                    }
                }
                System.out.println("\n\n=> Number of nodes accessed = " + rangeCounter);
                System.out.println("=> Number of blocks accessed = " + blockNum);
                System.out.println("------------------------End---------------------------\n"); 
                break;
            // case 5:
            //     System.out.println("--------------------Experiment 5----------------------"); 
            //     break;
            default:
                break;
            }          
        } while (second_option != 0);
    }
}