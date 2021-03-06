
package br.usp.each.saeg.agdtpoo.bytecodeFoundation;

import br.usp.each.saeg.agdtpoo.entity.JavaClass;
import br.usp.each.saeg.agdtpoo.entity.ReflectionClass;
import br.usp.each.saeg.agdtpoo.util.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class BytecodeRegister {
        
    private String _assemblyPath;
    
    public BytecodeRegister(String assemblyPath)
    {
        this._assemblyPath = assemblyPath;
    }
    
    public void RegisterSubTypes(JavaClass jClass)
    {
        // Registrar a classe sob teste no gerenciador de tipos
        RegisterType(jClass.getClassUnderTest());
        
        for (Constructor item : jClass.getConstructors()) {
            RegisterTypes(item.getGenericParameterTypes());
        }
        
        for (Method item : jClass.getMethods()) {
            RegisterTypes(item.getGenericParameterTypes());
        }
    }
    
    private void RegisterTypes(Type[] types)
    {
        for (Type parameter : types) {
            RegisterType(parameter);
        }
    }
    
    private void RegisterType(Type currentType)
    {
        String typeName = "";
        
        if (Features.isPrimitive(currentType))
            return;
        if (Features.isArray(currentType))
        {
            currentType = Features.getArrayComponentType(currentType);
            if (Features.isPrimitive(currentType))
                return;
        }
        
        typeName = Features.getTypeName(currentType);
        
        if (IsRegistered(typeName))
            return;
        
        // Carregar class de tipo alvo
        Class targetClass = JarReader.GetClassUsingJarPath(this._assemblyPath, typeName);
        // Criar java class
        JavaClass newJavaClass = JavaClassParser.Parse(targetClass);
        // Converter java class para reflectionclass
        ReflectionClass reflecClass = ClassParser.parse(newJavaClass);
        // Registrar tipo na lista de tipos descobertos
        RegisterType(reflecClass, newJavaClass);
    }
    
    private boolean IsRegistered(String className)
    {
        ManegementTypeSingleton mType = ManegementTypeSingleton.getInstance(); 
                
        return mType.containsBytecodeType(className);
    }
    
    private void RegisterType(ReflectionClass reflectClass, JavaClass newJavaClass)
    {
        String className;        
        String packageName;
        
        ManegementTypeSingleton mType = ManegementTypeSingleton.getInstance(); 
        
        className = reflectClass.getClassName();
        packageName = reflectClass.getPackage();        
        
        if (!mType.containsPackage(packageName))
            mType.addPackage(packageName);
        
        if (!mType.containsBytecodeType(className))
            mType.addBytecodeType(className, reflectClass);
        
        RegisterSubTypes(newJavaClass);
    }
}
