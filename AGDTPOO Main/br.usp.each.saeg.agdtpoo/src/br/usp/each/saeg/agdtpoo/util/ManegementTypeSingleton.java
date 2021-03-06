
package br.usp.each.saeg.agdtpoo.util;

import java.util.ArrayList;
import br.usp.each.saeg.agdtpoo.entity.*;

public class ManegementTypeSingleton {
    private static ManegementTypeSingleton _instance;
    
    private ArrayList<String> _packages;
    private ArrayList<BytecodeType> _discoveredTypes;
    
    private ManegementTypeSingleton()
    {
        this._discoveredTypes = new ArrayList<BytecodeType>();
        this._packages = new ArrayList<String>();
    }
    
    public String[] getPackages()
    {
        return this._packages.toArray(new String[this._packages.size()]);
    }
    
    public BytecodeType getBytecodeType(String typeName)
    {
        BytecodeType result = null;
        
        for (BytecodeType type : this._discoveredTypes) {
            String currentTypeName = type.getTypeName();
            if (currentTypeName.equals(typeName)){
                result = type;
                break;
            }
        }
        
        return result;
    }
    
    public boolean containsPackage(String packageName)
    {
        return this._packages.contains(packageName);
    }
    
    public void addPackage(String packageName)
    {
        this._packages.add(packageName);
    }
    
    public boolean containsBytecodeType(String typeName)
    {
        boolean contains = false;
        BytecodeType byteType = null;
        
        byteType = getBytecodeType(typeName);
        
        if (byteType != null)
            contains = true;
        
        return contains;
    }
    
    public void addBytecodeType(String typeName, ReflectionClass currentClass)
    {        
        BytecodeType newType = new BytecodeType();

        newType.setCurrentClass(currentClass);
        newType.setTypeName(typeName);

        this._discoveredTypes.add(newType);        
    }
    
    public static ManegementTypeSingleton getInstance()
    {
        if (_instance == null)
            _instance = new ManegementTypeSingleton();
        
        return _instance;
    }
}
