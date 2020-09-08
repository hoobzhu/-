package cn.hoob.utils;

import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.RegexTokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class TfidfUtil {
    /**
     * TF-IDF 分词构造向量
     */
	public static Dataset<Row> tfidf(Dataset<Row> dataset,String inputCol,String outputCol,String pattern) {

		//改用高级分正则分词
		/* RegexTokenizer tokenizer =new RegexTokenizer().setInputCol("kind").setOutputCol("kinds").setPattern("\\|");
        Dataset<Row> wordsData = tokenizer.transform(dataset);
        HashingTF hashingTF = new HashingTF().setInputCol("kinds").setOutputCol("rawFeatures");
        Dataset<Row> featurizedData = hashingTF.transform(wordsData);
        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");*/
		RegexTokenizer tokenizer =new RegexTokenizer().setInputCol(inputCol).setOutputCol(outputCol).setPattern(pattern);
		Dataset<Row> wordsData = tokenizer.transform(dataset);
		HashingTF hashingTF = new HashingTF().setInputCol(outputCol).setOutputCol("rawFeatures");
		Dataset<Row> featurizedData = hashingTF.transform(wordsData);
		IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
		IDFModel idfModel = idf.fit(featurizedData);
		return idfModel.transform(featurizedData);
	}
}