package KNN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;


/**
 * key in is the offset of training data
 * value in is the training data
 * key out is the offset of test data
 * value out is the distance between (key out) test data and (key in)training data
 * @author dongnanzhy
 *
 */
public class KNNMapper extends Mapper<Text, vectorList, Text, vectorSD> {
	
	private ArrayList<vectorList> test = new ArrayList<vectorList>();

	protected void map(Text key, vectorList value, Context context) throws IOException,
            InterruptedException {
        context.setStatus(key.toString());
		int index = 0;
    	for (vectorList testCase : test) {
    		//System.out.println(testCase);
    		double distance = euclideanDistance(testCase, value);
    		String keyIn = key.toString();
    		String keyOut = toString(index);
    		keyOut = keyOut +  ":";
    		for (double x : testCase) {
    			keyOut = keyOut + " " + x;
    		}
            context.write(new Text(keyOut), new vectorSD(keyIn, distance));
            index++;
    	}
    }
    public String toString(int i) {
    	int digit = i/10;
    	int unit = i%10;
    	String s = "";
    	s = s + digit + unit;
    	return s;
    }
    public vectorList readLine(String line) {
    	String[] value = line.split(",");
    	vectorList rst = new vectorList();
    	for (int i = 0; i < value.length; i++) {
    		rst.add(Double.valueOf(value[i]));
    	}
    	return rst;
    }
    
    protected void setup(Context context)
            throws java.io.IOException, InterruptedException {
        //System.out.print("!!!!!!!!!!!!!!!!!!!!!!!loading shared comparison vectors...");
        
        FileSystem fs = FileSystem.get(context.getConfiguration());
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(context.getConfiguration().get(
                "test data")))));
        String line = br.readLine();
        while (line != null) {
        	vectorList list = readLine(line);
            test.add(list);
            line = br.readLine();
        }
        br.close();
    }
    
    public double euclideanDistance(vectorList test, vectorList train) {
        double res = 0;
        for (int i = 0; i < test.size(); i++) {
            res = res + Math.pow(test.get(i) - train.get(i), 2);
        }
        return Math.sqrt(res);
    }
}