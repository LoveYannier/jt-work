package com.jt.jt_luceneDemo;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestLucene {
	
	//测试全文检索
	@Test
	public void writer() throws Exception{
		//1、创建一个document对象
		Document doc = new Document();
		//Store 创建索引时是否保存
		doc.add(new LongField("id",536563L,Store.YES));
		doc.add(new TextField("title", "阿尔卡特 (OT-927) 炭黑 联通3G手机 双卡双待", Store.YES));
		doc.add(new TextField("sellPoint", "清仓！仅北京，武汉仓有货！", Store.YES));
		doc.add(new DoubleField("price", 299000.00, Store.YES));
		doc.add(new TextField("itemDesc", "", Store.YES));//会分词 按各个term来查询
		doc.add(new StringField("image", "", Store.YES));//不分词，将整个内容作为一个词  一般不是用来查询。
		
		//2、创建索引，索引文件
		//创建目录
		Directory directory = FSDirectory.open(new File("./index")); //在当前工程运行的目录中创建一个index的目录
		Analyzer analyzer = new IKAnalyzer(); // 标准分词器
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer); //配置
		
		IndexWriter indexWriter = new IndexWriter(directory,config);
		
		//对上面的文章创建索引文件
		indexWriter.addDocument(doc);
	
		//关闭资源
		indexWriter.close();
		directory.close();
	}
	@Test //检索
	public void search() throws Exception{
		Directory directory = FSDirectory.open(new File("./index"));
		//打开索引目录 创建一个索引对象
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		//创建term查询方式 查询title字段 查询内容“米”
		Query query = new TermQuery(new Term("title","米"));
		//返回前多少条记录
		TopDocs topDocs = searcher.search(query, 10);
		
		//遍历topDocs scoreDocs得分
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            System.out.println("得分：" + scoreDoc.score);
            // 通过文档id查询文档数据
            Document doc = searcher.doc(scoreDoc.doc);

            System.out.println("商品ID：" + doc.get("id"));
            System.out.println("商品标题：" + doc.get("title"));
            System.out.println("商品卖点：" + doc.get("sellPoint"));
            System.out.println("商品价格：" + doc.get("price"));
            System.out.println("商品图片：" + doc.get("image"));
		}
	}

}
