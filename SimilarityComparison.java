package similarity;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class SimilarityComparison {

 /**
 * 莱文斯坦距离
 * @param a
 * @param b
 * @return
 */
	public static String txt2String(String filename){
		 File file= new File(filename);
		 StringBuilder result = new StringBuilder();
		 try{
		 BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
		 String s = null;
		 while((s = br.readLine())!=null){//使用readLine方法，一次读一行
		 result.append(System.lineSeparator()+s);
		 }
		 br.close(); 
		 }catch(Exception e){
		 return "error";
		 }
		 if (result.toString()==null) return "error";
		 return result.toString();
		 }
	
	
	
 public static float Levenshtein(String str_a, String str_b) { 
 if (str_a == null && str_b == null) {  //两个用于比对的文档都为空
 return 1f;
 }
 if (str_a == null || str_b == null) {  //其中一个文档为空
 return 0f;
 }
 int editDistance = editDis(str_a, str_b);
 return 1 - ((float) editDistance / Math.max(str_a.length(), str_b.length()));
   //使用'1-(编辑距离/两个字符串的最大长度)'来表示相似度
 }
 
 private static int editDis(String a, String b) {  //计算编辑距离
 int aLen = a.length();
 int bLen = b.length();
 if (aLen == 0) return aLen;
 if (bLen == 0) return bLen;
 int[][] v = new int[aLen + 1][bLen + 1];
 for (int i = 0; i <= aLen; ++i) {
 for (int j = 0; j <= bLen; ++j) {
 if (i == 0) {
 v[i][j] = j;
 } else if (j == 0) {
 v[i][j] = i;
 } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
 v[i][j] = v[i - 1][j - 1];
 } else {
 v[i][j] = 1 + Math.min(v[i - 1][j - 1], Math.min(v[i][j - 1], v[i - 1][j]));
 }
 }
 }
 return v[aLen][bLen];
 }

 public static String delSpecialChar(String str) {  //过滤标点符号
	 String regEx = "\\pP|\\pS|\\s+";
	 str = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
	 return str;
	}
 
 
 public static void main(String[] args) {
 System.out.println("请输入第一篇文档路径：");
 Scanner sc1 = new Scanner (System.in);
 String Text_A = sc1.nextLine();
 System.out.println("请输入第二篇文档路径：");
 String Text_B = sc1.nextLine();
 sc1.close();

 String F1,F2; //若文档不为空且无异常则读入内容，否则显示读入异常
 float t1;
 F1=txt2String(Text_A);F2=txt2String(Text_B);

 if(F1 == "error") System.out.println("第一篇文档异常，查重失败");
 else if(F2 == "error") System.out.println("第二篇文档异常，查重失败");
 else {
 delSpecialChar(F1); delSpecialChar(F2);
 t1=SimilarityComparison.Levenshtein(F1,F2);
 if(t1!=1f || t1!=0f)
 System.out.println("两篇文章相似度为：" + t1);
  }
 }
 
}