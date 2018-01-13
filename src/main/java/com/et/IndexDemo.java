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
	 //����ִ���
	static Analyzer analyzer = new IKAnalyzer();
	public static void main(String[] args) throws Exception {
		//write();
		search();
	}
	
	/**
	 * ��������
	 * @throws IOException 
	 */
	public static void write() throws IOException{
		//������Ĵ洢Ŀ¼
		Directory directory = FSDirectory.open(new File(dir));
		//����lucene�汾�͵�ǰ�ִ���
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//�������� ������д���Ŀ¼�ͷִ�����
		IndexWriter iwriter = new IndexWriter(directory, config);
		//document����field����
		Document doc=new Document();
		Field field=new Field("userName","����",TextField.TYPE_STORED);
		doc.add(field);
		field=new Field("userDesc","������������ϲ������",TextField.TYPE_STORED);
		doc.add(field);
		iwriter.addDocument(doc);
		
		Document doc1=new Document();
		Field field1=new Field("userName","����",TextField.TYPE_STORED);
		doc1.add(field1);
		field1=new Field("userDesc","������������ϲ��������",TextField.TYPE_STORED);
		doc1.add(field1);
		iwriter.addDocument(doc1);
		//�ύ����
		iwriter.commit();
		iwriter.close();
	}
	/**
	 * ��������
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void search() throws IOException, ParseException{
		//������Ĵ洢Ŀ¼
		Directory directory = FSDirectory.open(new File(dir));
		//��ȡ������Ĵ洢Ŀ¼
		DirectoryReader ireader = DirectoryReader.open(directory);
		//������
		IndexSearcher isearcher = new IndexSearcher(ireader);
		//lucene�Ĳ�ѯ������ ����ָ����ѯ���������ͷִ���
		QueryParser parser = new QueryParser(Version.LUCENE_47, "userDesc", analyzer);
		//��ʼ����
	    Query query = parser.parse("����");
	    //�����洢�ȷָߵ�document  ��ȡ�����Ľ�� ָ�����ص�document����
	    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
	    //��ScoreDoc�����л�ȡ������document
	    for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      System.out.println(hitDoc.getField("userName").stringValue());
	    }
	    ireader.close();
	    directory.close();
	}
}
