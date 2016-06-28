
package br.usp.each.saeg.agdtpoo.generationAlgorithms.mutation;

import br.usp.each.saeg.agdtpoo.entity.Individual;
import br.usp.each.saeg.agdtpoo.entity.InputPrimitiveDomain;
import br.usp.each.saeg.agdtpoo.entity.MethodPrimitiveDomain;
import br.usp.each.saeg.agdtpoo.entity.ReflectionMethod;
import br.usp.each.saeg.agdtpoo.entity.ReflectionParameter;
import br.usp.each.saeg.agdtpoo.entity.TestIndividual;
import br.usp.each.saeg.agdtpoo.entity.PrimitiveRandomTip;
import br.usp.each.saeg.agdtpoo.randomFeature.RandomValueFactory;
import java.util.Random;

public class InputValueMutation implements IMutation {
    
    private InputPrimitiveDomain _inputDomain;
    private boolean _executedWithSuccess;
    
    @Override
    public Individual mutate(Individual target)
    {
        Individual returnValue = new Individual();
        TestIndividual testI = null;
        
        // Captura a estrutura do individuo sob teste
        testI = target.getTestIndividual().clone();
        
        // Muta um de seus valores inteiros
        testI = mutate(testI);
        
        // Reatribui a estrutura do individuo para um novo individuo
        returnValue.setIndividual(testI);
        
        return returnValue;
    }
    
    @Override
    public void setInputDomain(InputPrimitiveDomain inputDomain)
    {
        this._inputDomain = inputDomain;
    }
    
    private TestIndividual mutate(TestIndividual target)
    {       
        int countOfIntermediateMethods = 0;
        this._executedWithSuccess = false;
        
        ReflectionMethod targetMethod = target.getMethodUnderTest();
           
        MethodPrimitiveDomain methodDomain = this._inputDomain.getOrCreateMethodDomain(targetMethod);
        
        if (targetMethod.getParameters().length > 0)
        {
            for (int i = 0; i < 5; i++) {

                Random rand = new Random();
                int selectedParameter = rand.nextInt(targetMethod.getParameters().length); 

                    ReflectionParameter reflectionParameter =  targetMethod.getParameters()[selectedParameter];

                    if (reflectionParameter.isPrimitive())
                    {
                        PrimitiveRandomTip tip = methodDomain.getMethodPrimitiveRandomTips(selectedParameter);
                        
                        // Atribuir os tipos primitivos
                        RandomValueFactory.setPrimitiveTips(this._inputDomain);
                        // Atribuir um novo valor ao parametro
                        RandomValueFactory.setRandomValue(reflectionParameter, tip);    
                        // Marca o individuo como mutado
                        this._executedWithSuccess = true;
                        break;
                    }
             } 
        }
        
        if (this._executedWithSuccess)
            return target;
            
        countOfIntermediateMethods = target.getMethods().length;
        
        int tentatives = 0;
        do
        {    
            // Avalia o conteudo dos metodos intermediarios
            if (countOfIntermediateMethods > 0)
            {
                Random rand = new Random();     
                // Seleciona um metodo aleatoriamente
                int selectedMethod = rand.nextInt(countOfIntermediateMethods);
                // Captura o metodo
                ReflectionMethod method = target.getMethods()[selectedMethod];
                
                methodDomain = this._inputDomain.getOrCreateMethodDomain(method);        
                
                // Varrer os parametros, identificar um primitivo, e gerar um novo valor
                for (int i = 0; i < method.getParameters().length; i++) {
                    ReflectionParameter reflectionParameter =  method.getParameters()[i];
                    
                    if (reflectionParameter.isPrimitive())
                    {
                        PrimitiveRandomTip tip = methodDomain.getMethodPrimitiveRandomTips(i); 
                        
                        // Atribuir os tipos primitivos
                        RandomValueFactory.setPrimitiveTips(this._inputDomain);
                        // Atribuir um novo valor ao parametro
                        RandomValueFactory.setRandomValue(reflectionParameter, tip);    
                        // Marca o individuo como mutado
                        this._executedWithSuccess = true;
                        break;
                    }
                }                              
            }    
            
            tentatives++;
            
            // Se forem executadas 5 tentativas de mudar o valor de algum parametro primitivo, e estas
            // 5 tentativas falharem, entao o operador nao foi executado com sucesso e sera cancelado
            if (tentatives >= 5)
                break;
            
        }while (!this._executedWithSuccess);
        
        return target;
    }
        
    @Override
    public boolean isExecutedWithSuccess()
    {
        return this._executedWithSuccess;
    }
}
