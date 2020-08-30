package project.review;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class pythonreview {
//    private static PythonInterpreter interpreter;
//    
//    public static void main(String[] args) {
// 
//        interpreter = new PythonInterpreter();
//        interpreter.execfile("test.py");
//        interpreter.exec("print(sum(7,8))");
// 
//    }
	
	public static void main(String[] args) {

		try {
		String line;
		Process p = Runtime.getRuntime().exec("python C:\\Users\\giduc\\eclipseworkspace\\javapro\\src\\test\\test1.py");
		Process py = Runtime.getRuntime().exec("python C:\\Users\\giduc\\eclipseworkspace\\javapro\\src\\webapp\\WEB-INF\\python\\reviewcrawling.py");
		
		// Process p = Runtime.getRuntime().exec("python C:\\Users\\giduc\\eclipseworkspace\\javapro\\src\\test\\forecast.py");
		// Process p = Runtime.getRuntime().exec("python C:\\Users\\giduc\\eclipseworkspace\\javapro\\src\\test\\crawling.py");
		// Process p = Runtime.getRuntime().exec("python C:\\Users\\giduc\\eclipseworkspace\\javapro\\src\\test\\crawling_oracle.py");
		BufferedReader input = new BufferedReader(new
		InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
		System.out.println(line);
		}
		} catch (Exception err) {
		err.printStackTrace();
		}
		} // main() end
}
