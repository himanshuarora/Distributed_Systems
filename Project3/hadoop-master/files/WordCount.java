import java.io.IOException;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.google.common.collect.Iterators;


public class WordCount {
  private static int maxFreq = 0;
  private static String maxFreqWord;
  private static final String OUTPUT_PATH = "intermediate_output";
  private static String output;

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
	  String prev = null;
      while (itr.hasMoreTokens()) {
		String curr = itr.nextToken();
		if (prev != null) {
			word.set(prev + " " + curr);
			context.write(word, one);
		}
		prev = curr;
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result); //uncomment this will lead a huge o/p on the screen
	  if(sum > maxFreq){
		maxFreqWord = key.toString();
		maxFreq = sum;
	  }
    }



    protected void cleanup(Context context) throws IOException,
                InterruptedException {
            /*Text t1 = new Text("MaxFrequency: " + maxFreqWord);
            context.write(t1, new IntWritable(maxFreq));*/

    }
  }


  private static List<PairOfWritables<Text, IntWritable>> walkOutput(Path output, Configuration conf) throws IOException{
		List<PairOfWritables<Text, IntWritable>> bigrams = new ArrayList<PairOfWritables<Text, IntWritable>>();
		FileSystem fileSystem = output.getFileSystem(conf);
		FileStatus fileStatus = fileSystem.getFileStatus(output);
		if (fileStatus.isDir()) {
		FileStatus[] listStatus = fileSystem.listStatus(output, new PathFilter() {
		  @Override
		  public boolean accept(Path path) {
			return !path.getName().startsWith("_");
		  }
		});
		for (FileStatus fs : listStatus) {
			bigrams.addAll(walkOutput(fs.getPath(), conf));
			return bigrams;
		}
	  } else {
		BufferedReader br=new BufferedReader(new InputStreamReader(fileSystem.open(output)));
		String line;
		line=br.readLine();
		while (line != null){
				String[] tokens = line.split("\\s+");
				Text word = new Text();
				IntWritable freq = new IntWritable();
				word.set(tokens[0] + " " + tokens[1]);
				freq.set(Integer.parseInt(tokens[2]));
				PairOfWritables p = new PairOfWritables(word, freq);
				bigrams.add(p);
				line=br.readLine();
		}
	  }
	  return bigrams;
  }

  public static void main(String[] args) throws Exception {
	output = args[1];
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
	job.setNumReduceTasks(1);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(output));
	job.waitForCompletion(true);

	List<PairOfWritables<Text, IntWritable>> bigrams = new ArrayList<PairOfWritables<Text, IntWritable>>();;
	try{
		bigrams = walkOutput(new Path(output), conf);
	}
	catch(Exception e){ System.out.println(e);}

	//sort the bigrams according to the frequenies

	Collections.sort(bigrams, new Comparator<PairOfWritables<Text, IntWritable>>() {
      public int compare(PairOfWritables<Text, IntWritable> e1,
          PairOfWritables<Text, IntWritable> e2) {
        if (e2.getRightElement().compareTo(e1.getRightElement()) == 0) {
          return e1.getLeftElement().compareTo(e2.getLeftElement());
        }

        return e2.getRightElement().compareTo(e1.getRightElement());
      }
    });

	//Calculate the number of bigrams

	int singletons = 0;
    int sum = 0;
    for (PairOfWritables<Text, IntWritable> bigram : bigrams) {
      sum += bigram.getRightElement().get();

      if (bigram.getRightElement().get() == 1) {
        singletons++;
      }
    }

    System.out.println("\n\nTotal Unique Bigrams: " + bigrams.size());
    System.out.println("Total Bigrams(Sum of frequency of each Bigram): " + sum);

	//Get the bigram with the max frequency

	System.out.println("\nBigram with max frequency : " + bigrams.get(0).getLeftElement() + "\t" + bigrams.get(0).getRightElement());

	//Get the bigrams that make up the top 10%
	int sum10 = sum/10;
	System.out.println("\nPrinting bigrams that make up 10 percent of the count of all the bigrams (i.e. 10 percent of " + sum + " = " + sum/10);
	sum = 0;
	int numBigramsMakingTop10 = 0;
	for (PairOfWritables<Text, IntWritable> bigram : bigrams) {
      sum += bigram.getRightElement().get();
	  numBigramsMakingTop10++;
      if(sum > sum10)
        break;
      System.out.println(bigram.getLeftElement() + "\t" + bigram.getRightElement());
    }
	System.out.println("Number of Bigrams accounting for the top 10% of total Bigram count: " + numBigramsMakingTop10);
  }
}
