
package fitnessAlgorithms;

import br.usp.each.saeg.agdtpoo.entity.GenerationCoverage;
import br.usp.each.saeg.agdtpoo.entity.GenerationRequirement;
import br.usp.each.saeg.agdtpoo.entity.Individual;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.fitness.FitnessException;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.fitness.IFitness;
import java.util.Arrays;
import java.util.List;

public class IneditismFitness  implements IFitness {

    private Individual[] _currentPopulation;
    
    @Override
    public double computeFitness(GenerationRequirement requirement, GenerationCoverage coverage) throws FitnessException {
        
        double[] fitnesses = null; 
        String[] path = null;
        
        // Captura os requisitos necessarios para atingir o alvo
        path = requirement.getCoveragePaths();
        fitnesses = new double[path.length]; 
        
        for (int i = 0; i < path.length; i++) {
            // Calcula a fitness de cada possivel caminho para cobrir um requisito
            fitnesses[i] = getFitness(path[i], coverage.getExecutionPath());
            
            // Se um dos requisitos for igual a 1, então já retorna este valor e 
            // não precisa executar os próximos passos
            if (fitnesses[i] == 1)
                return 1;
        }

        // Calcula o bonus do indivíduo
        double bonus = getBonus(coverage);
        double maxFitness = 0;
        
        if (fitnesses.length == 1)
        {
            maxFitness = fitnesses[0];
        }
        else
        {       
            // Identifica qual a melhor fitness para atingir um item        
            for (double fitness : fitnesses) {
                if (fitness > maxFitness)
                    maxFitness = fitness;
            }        
        }
        
        // Verifica se a fitness não irá estourar o limite de 1.0
        if ((maxFitness + bonus) < 1.0)
            maxFitness += bonus;
        
        return maxFitness;
    }
       
    private double getBonus(GenerationCoverage coverage)
    {
        double returnValue = 0;
        int countOfOriginalRequirements = 0;
        
        if (this._currentPopulation == null)
            return returnValue;
        
        if (this._currentPopulation.length == 0)
            return returnValue;
                
        // Requisitos que devem ser cobertos
        String[] requirementsTarget = getRequirements(coverage.getExecutionPath());
        
        // Navega entre os requisitos validando se ele é inédito ou não
        for (String requirementCovered : requirementsTarget) {
                       
            boolean isOriginal = true; 
            
            // Verifica indivíduo por indivíduo da população, se o requisito atual é inédito
            for (Individual individual : this._currentPopulation) {
                
                if (coverage == individual.getCoverage())
                    continue;
                
                // Verifica se o indivíduo cobre o requisito atual
                if (verifyIfIndividualContainsRequirementTo(requirementCovered, individual.getCoverage()))
                {
                    // Marca o indivíduo como "NÃO INÉDITO"
                    isOriginal = false;
                    break;
                }
            }
            
            // Verifica se o indivíduo é inédito
            if (isOriginal)
            {
                countOfOriginalRequirements++;
            }            
        }
        
        if (countOfOriginalRequirements > 0)
        {
            returnValue = (1 / (this._currentPopulation.length * 1.0));            
            returnValue *= countOfOriginalRequirements;
        }
        
        return returnValue;
    }
    
    private boolean verifyIfIndividualContainsRequirementTo(String requirement, GenerationCoverage coverage)
    {
        String individualCoverage = coverage.getExecutionPath();
        
        if (individualCoverage.contains("-" + requirement + "-"))
            return true;
            
        return false;
    }
        
    private double getFitness(String target, String realized)
    {
        if (realized.contains(target))
           return 1;
        
        // Requisitos que devem ser cobertos
        String[] requirementsTarget = getRequirements(target);
        
        // Requisitos que foram cobertos
        String[] requirementsRealized = getRequirements(realized);
        
        // Esta variável acumula a quantidade de itens que pertencem 
        // ao caminho desejado e que foram cobertos
        double countOfCorrectRequirements = 0;
        
        // Converte o array de alvos para LIST, por que este objeto da suporte
        // ao método CONTAINS. 
        List<String> realizedList = Arrays.asList(requirementsRealized);
                
        // Identifica quantos nós que deveriam ser cobertos foram executados.
        for (String requirement : requirementsTarget) {
            if (realizedList.contains(requirement))
                countOfCorrectRequirements++;
        }
                
        // Divide a quantidade de itens cobertos pela quantidade de itens necessária
        double returnValue = countOfCorrectRequirements / (requirementsTarget.length * 1.0);
        
        if (returnValue == 1)
            return 1;
        
        return returnValue;
    }
    
    private String[] getRequirements(String requirement)
    {
        // Remoção do primeiro hifen
        if (requirement.startsWith("-"))
            requirement = requirement.substring(1);
        // Remoção do ultimo hifen
        if (requirement.endsWith("-"))
            requirement = requirement.substring(0, requirement.length() - 1);
        
        String[] requirementsTarget = requirement.split("-");
        
        return requirementsTarget;
    }
    
    @Override
    public String getFitnessName() {
        return "Ineditism Fitness";
    }

    @Override
    public void setRequirements(GenerationRequirement[] requirements) {
        // Apenas para implementar a interface
    }    
    
    @Override
    public void setCurrentPopulation(Individual[] individuals) {
        this._currentPopulation = individuals;
    }    
}
