
package br.usp.each.saeg.agdtpoo.randomFeature;

import br.usp.each.saeg.agdtpoo.entity.PrimitiveRandomTip;
import br.usp.each.saeg.agdtpoo.entity.PrimitiveType;
import br.usp.each.saeg.agdtpoo.entity.ReflectionParameter;
import br.usp.each.saeg.agdtpoo.randomFeature.randomTypes.*;
import br.usp.each.saeg.agdtpoo.util.Features;
import java.lang.reflect.Type;

public class PrimitiveRandomFactory 
    implements IRandomFactory {
    private PrimitiveRandomTip _primitiveTips;
    
    public PrimitiveRandomFactory()
    {
        this._primitiveTips = null;
    } 
    
    public PrimitiveRandomFactory(PrimitiveRandomTip primitiveTips)
    {
        this._primitiveTips = primitiveTips;
    }
    
    @Override
    public Object getRandomValue(ReflectionParameter parameter)
    {
        Type parameterType = parameter.getParameterType();
        
        return generateRandomValue(parameterType);
    }
    
    private Object generateRandomValue(Type targetType)
    {
        IRandomTypeFactory randomTypeFactory = null;
        Object returnValue = null;
        PrimitiveType priType;

        priType = Features.getPrimitiveType(targetType);
        
        switch(priType)
        {
            case Boolean:
                randomTypeFactory = new BooleanRandomTypeFactory();
                break;
            case Character:
                randomTypeFactory = new CharacterRandomTypeFactory();
                break;
            case Short:
                randomTypeFactory = new ShortRandomTypeFactory();
                break;
            case Byte:
                randomTypeFactory = new ByteRandomTypeFactory();
                break;
            case Integer:
                randomTypeFactory = new IntegerRandomTypeFactory();
                break;
            case Long:
                randomTypeFactory = new LongRandomTypeFactory();
                break;
            case Float:
                randomTypeFactory = new FloatRandomTypeFactory();
                break;
            case Double:
                randomTypeFactory = new DoubleRandomTypeFactory();
                break;
            case Void:
                randomTypeFactory = null;
                break;
            case String:
                randomTypeFactory = new StringRandomTypeFactory();
                break;
            default:
                randomTypeFactory = null;
                break;
        }
        
       randomTypeFactory.setPrimitiveType(_primitiveTips);
        
        if (randomTypeFactory != null)
            returnValue = randomTypeFactory.generateRandomValue();
        
        return returnValue;
    }
}
