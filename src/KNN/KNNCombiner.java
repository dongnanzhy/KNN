package KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class KNNCombiner extends Reducer<Text, vectorSD, Text, vectorSD> {
        protected void reduce(Text key, java.lang.Iterable<vectorSD> value, Context context)
                        throws java.io.IOException, InterruptedException {
                ArrayList<vectorSD> list = new ArrayList<vectorSD>();
                // sort each vector2SF by similarty
                for (vectorSD v : value) {
                        list.add(new vectorSD(v.getV1(), v.getV2()));
                }
                Collections.sort(list, new Comparator<vectorSD>() {
                        @Override
                        public int compare(vectorSD o1, vectorSD o2) {
                        	 return o1.getV2().compareTo(o2.getV2());
                        }
                });
                int k = 5;

                for (int i = 0; i < k && i < list.size(); i++) {
                        context.write(key, list.get(i));
                }
        };
}