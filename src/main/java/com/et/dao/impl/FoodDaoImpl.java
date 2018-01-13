package com.et.dao.impl;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.et.dao.FoodDao;
@Repository
public class FoodDaoImpl implements FoodDao {
	static String dir="E:\\index";
	 //定义分词器
	static Analyzer analyzer = new IKAnalyzer();
	@Autowired
	JdbcTemplate jdbc;
	public List<Map<String, Object>> getFood() {
		String sql="select * from food";
		List<Map<String, Object>> result=jdbc.queryForList(sql);
		return result;
	}
	public void write() {
		//索引库的存储目录
		Directory directory;
		try {
			directory = FSDirectory.open(new File(dir));
			//关联lucene版本和当前分词器
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			//创建索引 （传入写入的目录和分词器）
			IndexWriter iwriter = new IndexWriter(directory, config);
			//获取数据库中的数据
			List<Map<String, Object>> list= getFood();
			//将数据库中的数据写入document对象中
			for (Map<String, Object> map : list) {
				//document对象field属性
				Document doc=new Document();
				Field field=new Field("foodname",map.get("foodname").toString(),TextField.TYPE_STORED);
				doc.add(field);
				iwriter.addDocument(doc);
			}
			//提交事务
			iwriter.commit();
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public List<String> search(String foodname) {
		List<String> ls=new ArrayList<String>();
		//索引库的存储目录
		Directory directory;
		try {
			directory = FSDirectory.open(new File(dir));
			//读取索引库的存储目录
			DirectoryReader ireader = DirectoryReader.open(directory);
			//搜索类
			IndexSearcher isearcher = new IndexSearcher(ireader);
			//lucene的查询解析器 用于指定查询的属性名和分词器
			QueryParser parser = new QueryParser(Version.LUCENE_47, "foodname", analyzer);
			//开始搜索
		    Query query = parser.parse(foodname);
		    //用来存储等分高的document  获取搜索的结果 指定返回的document个数
		    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
		    //从ScoreDoc数组中获取单独的document
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      ls.add(hitDoc.getField("foodname").stringValue());
		      //ik分词器的方式
		       StringReader sr=new StringReader(hitDoc.getField("foodname").stringValue());  
		       IKSegmenter ik=new IKSegmenter(sr, false);  //true代表调用IKSegmenter()构造函数时使用智能分词
		       Lexeme lex=null;  
		        while((lex=ik.next())!=null){  
		            System.out.print(lex.getLexemeText()+"|");  
		        }  
		    }
		    
		    ireader.close();
		    directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ls;
				
		
	}

	

	

}
