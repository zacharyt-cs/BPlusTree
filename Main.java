import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args){
		BPlusTree tree=new BPlusTree();		

		System.out.println("--------------------Experiment 1 and 2----------------------");
		List<Blocks> blo = new ArrayList<Blocks>();

		FileIO.readTSV("data.tsv", tree, blo);
		//	
		// // one data block is equal to 100
		// System.out.println("The number of blocks "+ blo.size());
		// System.out.println("The size of database "+blo.size()*100);

		// int No_of_nodes= BPlusTree.printTree();
		// //			need multply by node size
		// int calculateSize;
		// //			my degree is 4 so 8 *4 bytes, Since is 8
		// calculateSize= No_of_nodes* 96;

		// Experiment 3
		// System.out.println("------------------Experiment 3-----------------");
		// List<Records> tconstValue= BPlusTree.search(8);
		// for(int i= 0; i<tconstValue.size();i++)
		// {
		// 	//limit the max output to be 20 entries due to the huge amount of data
		// 	if(i < 20)
		// 	{
		// 		//printing of tconst value
		// 		System.out.println(" The t constant value is " + tconstValue.get(i).tconstant);
		// 	}
		// 	else 
		// 	{
		// 		break;
		// 	}
		// }
		// //			Print out the number of blocks and the block data
		// int blockCounter=0;
		// for(int j=0; j<blo.size();j++)
		// {
		// 	//put the block into the dataBlo arraylist
		// 	List<Records> dataBlo =	blo.get(j).recordlist;
		// 	for(int a=0;a<dataBlo.size();a++)
		// 	{
		// 		if(dataBlo.get(a).averagerating==8)
		// 		{
		// 			blockCounter++;
		// 			for(int b=0; b<dataBlo.size();b++)
		// 			{
		// 				Records r = dataBlo.get(b);
		// 				System.out.println("Tconst is "+ r.tconstant + " Average Rating is "+ r.averagerating + " Number of votes is "+ r.numofvote);
		// 			}
		// 			break;
		// 		}
		// 	}
		// }
		// System.out.print("Number of blocks: " + blockCounter);

		System.out.println("------Experiment 4--------------");
		List<Keys> test= tree.search(7,9); 

		for(int d=0; d<test.size(); d++){
			// Print out 20 records of the tconstant
			Keys keys = test.get(d);
			float key = keys.key;
			List<Records> records = keys.values;
			System.out.println("key,");
			for (Records record : records){
				System.out.println(key+", "+record.tconstant+", "+record.numofvote);
			}
		}
	}
}