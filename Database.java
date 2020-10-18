import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Database {
	 // initialise storage to 300MB
    ByteBuffer storage = ByteBuffer.allocate(300000000);

    // read byte into buffer from file
    // FileInputStream in = new FileInputStream(file);
    // write data from buffer into file

    // read TSV file into bytebuffer using channel

    public static void main(String[] args) throws IOException {
        // StringTokenizer st;
        // BufferedReader TSVFile = new BufferedReader(new FileReader("data.tsv"));
        // String dataRow = TSVFile.readLine();
        // dataRow = TSVFile.readLine();
        // while (dataRow != null) {
        // st = new StringTokenizer(dataRow,"\t");
        // List<String>dataArray = new ArrayList<String>();
        //     while (st.hasMoreElements()) {
        //         dataArray.add(st.nextElement().toString());
        //     }
        //     System.out.println(dataArray);
        // }
        

    ArrayList<String[]> Data = new ArrayList<>();
    BufferedReader TSVReader = new BufferedReader(new FileReader("D:\\Year 3 Sem 1\\Database System\\data.tsv"));
    String line = null;
    while ((line = TSVReader.readLine()) != null) {
        String[] lineItems = line.split("\t");
        Data.add(lineItems);
    }
  
    Data.forEach(array -> System.out.println(Arrays.toString(array)));
    System.out.println();

    }
}
