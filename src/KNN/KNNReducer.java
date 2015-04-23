package KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KNNReducer extends Reducer<Text, vectorSD, Text, Text> {

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
        
        String s1 = "setosa"; String s2 = "versicolor"; String s3 = "virginica";
        int count1 = 0; int count2 = 0; int count3 = 0;
        for (int i = 0; i < k && i < list.size(); i++) {
            String flower = list.get(i).getV1();
            String[] s = flower.split(",");
            flower = s[1];
           
            if (flower.equals(s1)) {
            	count1++;
            } else if (flower.equals(s2)) {
            	count2++;
            } else {
            	count3++;
            }
        }
        String valueOut;
        int max = Math.max(count1, count2);
        max = Math.max(max, count3);
        if (max == count1) {
        	valueOut = "   " + s1;
        } else if (max == count2) {
        	valueOut = "   " + s2;
        } else {
        	valueOut = "   " + s3;
        }
        
        context.write(key, new Text(valueOut));
    }
    ;
}
