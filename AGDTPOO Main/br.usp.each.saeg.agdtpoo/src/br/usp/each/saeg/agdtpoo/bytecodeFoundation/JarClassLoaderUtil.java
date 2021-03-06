
package br.usp.each.saeg.agdtpoo.bytecodeFoundation;

import br.usp.each.saeg.agdtpoo.entity.ByteCodeFoundationException;
import br.usp.each.saeg.agdtpoo.entity.IGUIGenerationStrategy;
import br.usp.each.saeg.agdtpoo.entity.TestDataGenerationResource;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.fitness.IFitness;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.BaseGenerationStrategy;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.selection.ISelection;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.*;

public class JarClassLoaderUtil {
    
    public TestDataGenerationResource getTestDataGenerationResource (String jarPath) throws ByteCodeFoundationException
    {
        TestDataGenerationResource returnValue = new TestDataGenerationResource();
        
        // Identifico todas as classes do pacote JAR
        String[] classes = readAllClasses(jarPath);
        
        // Navego por dentre as classes
        for (String className : classes) {
            
            // Instancio o tipo da classe
            Class<?> clazz = getClass(jarPath, className);
            
            // Verifico se ela é uma técnica de teste, técnica de seleção ou 
            // uma fitness
            boolean isStrategy = isTestStrategy(clazz);
            boolean isSelection = isSelection(clazz);
            boolean isFitness = isFitness(clazz);
            
            // Se não for nenhum dos três então passo para o próximo passo
            if (!isStrategy && !isSelection && !isFitness)
                continue;
            
            try
            {             
                // Crio a instância do tipo de dados
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                
                // Se é uma estratégia de teste, então incluo na lista de 
                // estratégias de teste
                if (isStrategy)
                    returnValue.addNewTestStrategy((BaseGenerationStrategy)object);
                // Se é um algoritmo de seleção, então incluo na lista de 
                // algoritmos de seleção
                if (isSelection)
                    returnValue.addNewSelection((ISelection)object);
                // Se é um algoritmo de fitness, então incluo na lista de fitness                
                if (isFitness)
                    returnValue.addNewFitness((IFitness)object);                
            }
            catch(Exception ex)
            {
                throw new ByteCodeFoundationException(ex);
            }            
        }
        
        return returnValue;
    }
    
    private boolean isTestStrategy(Class<?> clazz)
    {
        if (BaseGenerationStrategy.class.isAssignableFrom(clazz) && IGUIGenerationStrategy.class.isAssignableFrom(clazz))
            return true;
                       
        return false;
    }
    
    private boolean isSelection(Class<?> clazz)
    {
        if (ISelection.class.isAssignableFrom(clazz))
            return true;
                       
        return false;
    }
    
    private boolean isFitness(Class<?> clazz)
    {
        if (IFitness.class.isAssignableFrom(clazz))
            return true;
                       
        return false;
    }
    
    public String[] readAllClasses(String jarPath) throws ByteCodeFoundationException
    {
        ArrayList<String> classesFounded = new ArrayList<String>();
        
        try
        {
            // Instancia arquivo Jar
            File jFile = new File(jarPath);
            // Verifica se o arquivo existe            
            if (jFile.exists())
            {
                // Cria stream do arquivo jar
                JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
             
                JarEntry entry = null;                                        
                do {
                    // Identifica objeto do jar
                    entry = jarFile.getNextJarEntry();
                    if (entry != null)
                    {
                        // Verifica se o objeto é uma classe
                        String className = entry.getName();                        
                        if (className.endsWith(".class"))
                        {
                           classesFounded.add(className);
                        }
                    }
                    
                }while(entry != null);
            }
        }
        catch(Exception ex)
        {
            throw new ByteCodeFoundationException(ex);
        }
        
        return classesFounded.toArray(new String[classesFounded.size()]);
    }
    
    private Class<?> getClass(String jarPath, String className) throws ByteCodeFoundationException
    {
        Class<?> returnValue = null;
        
        className = className.replace(".class", "");
        className = className.replace("/", ".");
        
        try
        {
            File jarFile = new File(jarPath);
            
            if (jarFile.exists())
            {
                URL jarUrl = new URL("jar", "", "file:" + jarFile.getAbsolutePath() + "!/");

                URL[] jarURLs = { jarUrl };

                URLClassLoader child = new URLClassLoader(jarURLs);

                returnValue = Class.forName(className, true, child);                                
            }
        }
        catch(Exception ex)
        {
            throw new ByteCodeFoundationException(ex);
        }
        
        return returnValue;
    }
    
    public void executeSomething(String jarPath, String className) throws ByteCodeFoundationException
    {
        try
        {
            File jarFile = new File(jarPath);
            
            if (jarFile.exists())
            {
                URL jarUrl = new URL("jar", "", "file:" + jarFile.getAbsolutePath() + "!/");

                URL[] jarURLs = { jarUrl };

                URLClassLoader child = new URLClassLoader(jarURLs);

                Class<?> clazz = Class.forName(className, true, child);
                                
                Constructor<?> ctor = clazz.getConstructor();

                Object object = ctor.newInstance();

            }
        }
        catch(Exception ex)
        {
            throw new ByteCodeFoundationException(ex);
        }
    }
    
}
