package KNNTemp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import data.ARFFInputformat;
import data.SparseVector;
import data.Vector2;
import data.Vector2SF;

public class KNNMapper extends Mapper<Text, SparseVector, Text, Vector2SF> {

    private Vector<Vector2<String, SparseVector>> test =
            new Vector<Vector2<String, SparseVector>>();

    protected void map(
            Text key,
            SparseVector value,
            org.apache.hadoop.mapreduce.Mapper<Text, SparseVector, Text, Vector2SF>.Context context)
            throws java.io.IOException, InterruptedException {
        // calculate the distance for each test sample with the training data
        context.setStatus(key.toString());
        for (Vector2<String, SparseVector> testCase : test) {
            double d = testCase.getV2().dotProduct(value);
            context.write(new Text(testCase.getV1()), new Vector2SF(key.toString(),
                    (float) d));
        }

    }

    protected void cleanup(
            org.apache.hadoop.mapreduce.Mapper<Text, SparseVector, Text, Vector2SF>.Context context)
            throws java.io.IOException, InterruptedException {
//        test.close();
    }

    ;

    protected void setup(
            org.apache.hadoop.mapreduce.Mapper<Text, SparseVector, Text, Vector2SF>.Context context)
            throws java.io.IOException, InterruptedException {
        System.out.print("loading shared comparison vectors...");

        // load the test vectors
        FileSystem fs = FileSystem.get(context.getConfiguration());
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(context.getConfiguration().get(
                "test data", "test.arff")))));
        String line = br.readLine();
        int count = 0;
        while (line != null) {
            Vector2<String, SparseVector> v = ARFFInputformat.readLine(count, line);
            test.add(new Vector2<String, SparseVector>(v.getV1(), v.getV2()));
            line = br.readLine();
            count++;
        }
        br.close();
        System.out.println("done.");
    }

    ;
}