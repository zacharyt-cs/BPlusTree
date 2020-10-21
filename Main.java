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
        List<Blocks> block_list = new ArrayList<Blocks>();
        FileIO.readTSV("data.tsv", tree, block_list);
        
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
                // Incorrect
                case 1:
                    // run Exp 1: print number of blocks and size of database
                    System.out.println("--------------------Experiment 1 and 2----------------------");

                    System.out.println("The number of blocks "+ block_list.size());

                    // size of database = num of blocks * block size (WARNING: last block may not be full)
                    System.out.println("The size of database "+block_list.size()*100);
                break;

                case 2:
                    // run Exp 2
                    System.out.println("run ex 2");
                    tree.printTree();
                break;

                case 3:
                // run Expr 3
                System.out.println("run ex 3");
               
    		    
    			List<Records> tconstValue= tree.search(8);
    			for(int i= 0; i<tconstValue.size();i++)
    			{
    				//limit the max output to be 20 entries due to the huge amount of data
    				
    					//printing of tconst value
    					System.out.println(" The t constant value is " + tconstValue.get(i).tconstant);
    			
    			}
//    			Print out the number of blocks and the block data
    			int blockCounter=0;
    			for(int j=0; j<block_list.size();j++)
    			{
    				//put the block into the dataBlo arraylist
    				List<Records> dataBlo =	block_list.get(j).recordlist;
    				
    				for(int a=0;a<dataBlo.size();a++)
    				{
    					if(dataBlo.get(a).averagerating==8)
    					{
    						blockCounter++;
    						
    						for(int b=0; b<dataBlo.size();b++)
    						{
    									
    							Records r = dataBlo.get(b);
    							System.out.println(" The block data it accessing is "+"Tconst : "+ r.tconstant + " Average Rating:  "+ r.averagerating + " Number of votes : "+ r.numofvote);
    							
    						}
    						break;
    
    					}
    				}
    			}
    			
    			System.out.println("Number of blocks: " + blockCounter);
                break;

                case 4:
                // run Expr 4
                    System.out.println("------Experiment 4--------------");
                    int rangeCounter=0;
                    List<Keys> test= tree.search(7,9); 
                    for(int d=0; d<test.size(); d++){
                    	  Keys keys = test.get(d);
                              float key = keys.key;
                              
                              	 List<Records> records = keys.values;
                                   
                                       System.out.println("key,");
                                       for (Records record : records)
                                       {
                                    	   
                                    	   System.out.println("Tconst is "+record.tconstant);
                                       
                                    	   rangeCounter++;
                                       }


                    }
                    int blockSize=0;
                    int printNum=1;
               
                    for(int g=0; g< block_list.size();g++)
                    {
                    	List<Records> rangeBlo =block_list.get(g).recordlist;
        				
        				for(int a=0;a<rangeBlo.size();a++)
        				{
        				
        					if(rangeBlo.get(a).averagerating >= 7.0f && rangeBlo.get(a).averagerating <=9.0f)
        					{
        						blockSize++;
        						
        							for(int b=0; b<rangeBlo.size();b++)
            						{
            						
            								Records r = rangeBlo.get(b);
                							System.out.println(" The block data it accessing is "+"Tconst : "+ r.tconstant + " Average Rating:  "+ r.averagerating + " Number of votes : "+ r.numofvote);
                							printNum++;

            						}

        						break;
        					}
        				}
        				
                    }
                    System.out.println("Number of blocks: "+ blockSize);
                break;
                case 5:
                // run Expr 5
                System.out.println("run ex 5");
                
                break;
            }
        } while (second_option != 0);
    }
}