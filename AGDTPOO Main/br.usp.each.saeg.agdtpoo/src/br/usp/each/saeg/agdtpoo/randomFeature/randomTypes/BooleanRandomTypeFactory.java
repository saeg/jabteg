
package br.usp.each.saeg.agdtpoo.randomFeature.randomTypes;

import br.usp.each.saeg.agdtpoo.entity.PrimitiveRandomTip;
import java.util.Random;

public class BooleanRandomTypeFactory 
    implements IRandomTypeFactory {

    @Override
    public void setPrimitiveType(PrimitiveRandomTip primitiveTips)
    {
        // Neste caso o uso de geração aleatória não se aplica
    }
    
    @Override
    public Object generateRandomValue()
    {
        Random rand = new Random();
        
        return rand.nextBoolean();
    }    
}
