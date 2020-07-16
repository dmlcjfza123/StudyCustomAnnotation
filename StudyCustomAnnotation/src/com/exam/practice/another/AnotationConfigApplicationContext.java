package com.exam.practice.another;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exam.practice.annotation.Exam;
import com.exam.practice.component.Amuguna;

public class AnotationConfigApplicationContext {
	Map<String,Object> SPRING_CONTAINER = new HashMap<>();
	
	public void Init() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
//		Package[] packages = Package.getPackages();
//		
//		for (Package package1 : packages) {
//			
//			
//			if(package1.getName().startsWith("com.exam"))
//			{
//				System.out.println(package1.getName());
//				Class[] classes = getClasses(package1.getName());
//				for (Class classes1 : classes) {
//					Annotation[] annotations = classes1.getDeclaredAnnotations();
//					for (Annotation annotations1 : annotations) {
//						if(annotations1.annotationType().equals(Exam.class)) {
//							Class classtype = Class.forName(classes1.getName());
//							Object obj = classtype.newInstance();
//							((Amuguna)obj).printout();
//						}
//					}
//				}
//			}
//		}
		
		Class[] classes = getClasses("com.exam.practice");
		for (Class classes1 : classes) {
			Annotation[] annotations = classes1.getDeclaredAnnotations();
			for (Annotation annotations1 : annotations) {
				if(annotations1.annotationType().equals(Exam.class)) {
					Class classtype = Class.forName(classes1.getName());
					
					Field[] fields = classtype.getFields();
					for(Field field : fields) {
						field.getDeclaredAnnotations();
					}
					
					
					Object obj = classtype.newInstance();
					
					
					
					System.out.println(obj instanceof Amuguna);
					((Amuguna)obj).printout();
					String beanName = classes1.getSimpleName();
					SPRING_CONTAINER.put(beanName, obj);
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		AnotationConfigApplicationContext a = new AnotationConfigApplicationContext();
		a.Init();
	}
	
	private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
	
}
