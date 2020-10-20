// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.util.ArrayList;
// import java.util.List;


// public class Disk{
//     ByteArray diskArray;
//     static int blockSize;
//     static List<Blocks> diskBlocks;

//     // Initialise Disk Storage
//     public Disk(int diskSize, int blockSize) {
//         this.diskArray = new ByteArray(diskSize);
//         this.blockSize = blockSize;
//         this.diskBlocks = new ArrayList<Blocks>();
//     }

//     // Read tsv file, insert into Disk
//     public void readTSV(String path) {
//         try (BufferedReader tsvReader = new BufferedReader(new FileReader(path))) {
//             String line = null;
//             Blocks blk = new Blocks(blockSize);
//             diskBlocks.add(blk);
//             int i=0;

//             // For each row in the tsv, insert record into Disk
//             while ((line = tsvReader.readLine()) != null){
//                 if(i!=0){
//                     //splitting the line and adding its items in String[]
//                     String[] lineItems = line.split("\t"); 
//                     // Create record
//                     Records rdata = new Records(lineItems[0],Float.parseFloat(lineItems[1]), Integer.parseInt(lineItems[2]));
//                     // If block is full, create new block
//                     if(blk.recordlist.size() == blockSize){
//                         blk = new Blocks(blockSize);
//                         diskBlocks.add(blk);
//                         blk.addRecord(rdata);
//                     }
//                     // Else, add record to block
//                     else{
//                         blk.addRecord(rdata);
//                     }
//                 }
//                 i++;
//             }
//         }catch (Exception e){
//             e.printStackTrace();
//         }
//     }
// }
