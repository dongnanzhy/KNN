package KNN;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class KNNDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new KNNDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        // config a job and start it
        Configuration conf = getConf();
        conf.set("mapred.textoutputformat.separator", ",");
        //look at the test folder

            conf.set("test data", "iris_test_data.csv");
            
            Job job = Job.getInstance(conf, "KNN Classifier");
            job.setJarByClass(KNNDriver.class);
            job.setMapperClass(KNNMapper.class);
            job.setReducerClass(KNNReducer.class);
            job.setCombinerClass(KNNCombiner.class);
            job.setOutputKeyClass(Text.class);
            job.setMapOutputValueClass(vectorSD.class);
            job.setOutputValueClass(Text.class);
            job.setInputFormatClass(TEXTInputformat.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            Path out = new Path(args[1]);
            FileSystem.get(conf).delete(out, true);
            FileOutputFormat.setOutputPath(job, out);
            //set the current testing file
            int res = job.waitForCompletion(true) ? 0 : 1;
            if (res != 0) {
                return res;
            }
        
        return 0;
    }
   
}