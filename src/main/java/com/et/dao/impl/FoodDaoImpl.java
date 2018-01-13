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
	 //����ִ���
	static Analyzer analyzer = new IKAnalyzer();
	@Autowired
	JdbcTemplate jdbc;
	public List<Map<String, Object>> getFood() {
		String sql="select * from food";
		List<Map<String, Object>> result=jdbc.queryForList(sql);
		return result;
	}
	public void write() {
		//������Ĵ洢Ŀ¼
		Directory directory;
		try {
			directory = FSDirectory.open(new File(dir));
			//����lucene�汾�͵�ǰ�ִ���
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			//�������� ������д���Ŀ¼�ͷִ�����
			IndexWriter iwriter = new IndexWriter(directory, config);
			//��ȡ���ݿ��е�����
			List<Map<String, Object>> list= getFood();
			//�����ݿ��е�����д��document������
			for (Map<String, Object> map : list) {
				//document����field����
				Document doc=new Document();
				Field field=new Field("foodname",map.get("foodname").toString(),TextField.TYPE_STORED);
				doc.add(field);
				iwriter.addDocument(doc);
			}
			//�ύ����
			iwriter.commit();
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public List<String> search(String foodname) {
		List<String> ls=new ArrayList<String>();
		//������Ĵ洢Ŀ¼
		Directory directory;
		try {
			directory = FSDirectory.open(new File(dir));
			//��ȡ������Ĵ洢Ŀ¼
			DirectoryReader ireader = DirectoryReader.open(directory);
			//������
			IndexSearcher isearcher = new IndexSearcher(ireader);
			//lucene�Ĳ�ѯ������ ����ָ����ѯ���������ͷִ���
			QueryParser parser = new QueryParser(Version.LUCENE_47, "foodname", analyzer);
			//��ʼ����
		    Query query = parser.parse(foodname);
		    //�����洢�ȷָߵ�document  ��ȡ�����Ľ�� ָ�����ص�document����
		    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
		    //��ScoreDoc�����л�ȡ������document
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      ls.add(hitDoc.getField("foodname").stringValue());
		      //ik�ִ����ķ�ʽ
		       StringReader sr=new StringReader(hitDoc.getField("foodname").stringValue());  
		       IKSegmenter ik=new IKSegmenter(sr, false);  //true�������IKSegmenter()���캯��ʱʹ�����ִܷ�
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
