
package selectionAlgorithms;

import br.usp.each.saeg.agdtpoo.entity.Individual;
import br.usp.each.saeg.agdtpoo.generationAlgorithms.selection.ISelection;
import java.util.ArrayList;
import java.util.List;

public class AverageSelection implements ISelection {

    @Override
    public Individual[] selectIndividualsToMutation(Individual[] population) {
        
        // Captura a fitness média
        double average = getAverage(population);
        
        // Retorna os indivíduos acima ou igual a média
        return getIndividualsAboveTheAverage(population, average);
    }
    
    // Retorna apenas os indivíduos acima da média
    private Individual[] getIndividualsAboveTheAverage(Individual[] population, double average) {
        List<Individual> resultIndividuals = new ArrayList<Individual>();
        
        for (Individual individual : population) {
            if (individual.getFitness() >= average)
                resultIndividuals.add(individual);
        }
        
        return resultIndividuals.toArray(new Individual[resultIndividuals.size()]);
    }
    
    // Identifica a média da fitness dos indivíduos da população
    private double getAverage(Individual[] population) {
        double average = 0;
        double sum = 0;
                
        for (Individual individual : population) {
            sum += individual.getFitness();
        }
        
        if (population.length > 0)
            average = sum / (population.length * 1.0);
        
        return average;
    }
    
    @Override
    public String getSelectionName() {
        return "Average Selection";
    }
}
