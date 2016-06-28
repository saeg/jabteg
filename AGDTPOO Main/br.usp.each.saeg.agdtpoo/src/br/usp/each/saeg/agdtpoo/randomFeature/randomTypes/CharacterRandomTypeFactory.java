
package br.usp.each.saeg.agdtpoo.randomFeature.randomTypes;

import br.usp.each.saeg.agdtpoo.entity.PrimitiveRandomTip;

public class CharacterRandomTypeFactory 
    implements IRandomTypeFactory {
        
    private PrimitiveRandomTip _primitiveTips;
    
    @Override
    public void setPrimitiveType(PrimitiveRandomTip primitiveTips)
    {
        _primitiveTips = primitiveTips;
    }    
    
    @Override
    public Object generateRandomValue()
    {
        char returnValue = 'a';
        int random;
        int randomAux;
        boolean value = false;
        
        if (this._primitiveTips != null && this._primitiveTips.getPossibleChar() != null && this._primitiveTips.getPossibleChar().length > 0)
        {
            randomAux = this._primitiveTips.getPossibleChar().length;
            random = (int)(Math.random()*randomAux);
            
            returnValue = this._primitiveTips.getPossibleChar()[random];
        }
        else
        {        
            randomAux = (int)(Math.random() * 1000);

            for (int i = 0; i < randomAux; i++) {
                randomAux = (int)(Math.random() * 1000);
                value = false;

                do
                {
                    random = (int)(Math.random()*122);

                    if (random >= 48 && random <= 57){
                        value = true;
                    } else if (random >= 97 && random <= 122){
                        value = true;
                    } else if (random >= 65 && random <= 90){
                        value = true;
                    }    
                }while(!value);

                returnValue = ((char)random);
            }
        }
        
        return "'" + returnValue + "'";
    }   
}
