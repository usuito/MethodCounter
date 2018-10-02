package jp.co.******;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * 指定したパッケージ以下のクラスのメソッド数を指定したファイルにcsvファイルとして出力します。
 *
 */

public class MethodCounter {

	private static String PACKAGE = "jp.co.******";
	private static String TARGET_FILE = "C:\\workspace\\methodCount.csv";

	public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException {

		FileWriter file = new FileWriter(TARGET_FILE);
		PrintWriter writer = new PrintWriter(new BufferedWriter(file));

		List<Class<?>> classes = getClasses(PACKAGE);

		List<String> temp = new ArrayList<>();

		int methodCount = 0;
		int classCount = 0;

		writer.println("ClassName,methodCount");

		for (Class<?> class1 : classes) {
			if (class1.getName().contains("MethodCounter")) {
				continue;
			}

			if(class1.getName().contains("Test")) {
				continue;
			}

			if(temp.contains(class1.getName())) {
				continue;
			}



			Method[] methods = class1.getDeclaredMethods();
			methodCount += methods.length;
			classCount += 1;
			temp.add(class1.getName());

			System.out.println(class1.getSimpleName());
			String line = class1.getName() + "," + class1.getDeclaredMethods().length;
			writer.println(line);
		}

		writer.close();

		System.out.println("Class Count is " + classCount);
		System.out.println("All method count is " + methodCount);
	}

		private static List<Class<?>> getClasses(String packageName)
				throws IOException, URISyntaxException, ClassNotFoundException {

      ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> e = cl.getResources(packageName.replace(".", "/"));

			List<Class<?>> classes = new ArrayList<>();

			for (; e.hasMoreElements();) {

				URL url = e.nextElement();

				File dir = new File(url.getPath());

				for (String path : dir.list()) {

					if (!path.endsWith(".class")) {
						classes.addAll(getClasses(packageName + "." + path));
					}

					if (path.endsWith(".class")) {
						classes.add(Class.forName(packageName + "." + path.substring(0, path.length() - 6)));
					}

				}
			}
			return classes;
		}

}
