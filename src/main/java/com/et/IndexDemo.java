package com.et;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexDemo {
	static String dir="E:\\index";
	 //定义分词器
	static Analyzer analyzer = new IKAnalyzer();
	public static void main(String[] args) throws Exception {
		//write();
		search();
	}
	
	/**
	 * 创建索引
	 * @throws IOException 
	 */
	public static void write() throws IOException{
		//索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//关联lucene版本和当前分词器
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//创建索引 （传入写入的目录和分词器）
		IndexWriter iwriter = new IndexWriter(directory, config);
		//document对象field属性
		Document doc=new Document();
		Field field=new Field("userName","张三",TextField.TYPE_STORED);
		doc.add(field);
		field=new Field("userDesc","张三来自深圳喜欢看书",TextField.TYPE_STORED);
		doc.add(field);
		iwriter.addDocument(doc);
		
		Document doc1=new Document();
		Field field1=new Field("userName","李四",TextField.TYPE_STORED);
		doc1.add(field1);
		field1=new Field("userDesc","李四来自兰州喜欢打篮球",TextField.TYPE_STORED);
		doc1.add(field1);
		iwriter.addDocument(doc1);
		//提交事务
		iwriter.commit();
		iwriter.close();
	}
	/**
	 * 搜索索引
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void search() throws IOException, ParseException{
		//索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//读取索引库的存储目录
		DirectoryReader ireader = DirectoryReader.open(directory);
		//搜索类
		IndexSearcher isearcher = new IndexSearcher(ireader);
		//lucene的查询解析器 用于指定查询的属性名和分词器
		QueryParser parser = new QueryParser(Version.LUCENE_47, "userDesc", analyzer);
		//开始搜索
	    Query query = parser.parse("自兰");
	    //用来存储等分高的document  获取搜索的结果 指定返回的document个数
	    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
	    //从ScoreDoc数组中获取单独的document
	    for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      System.out.println(hitDoc.getField("userName").stringValue());
	    }
	    ireader.close();
	    directory.close();
	}
}
