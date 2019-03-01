package aima.gui.demo.search;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFitnessFunction;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.HillClimbingSearch.SearchOutcome;
import aima.core.search.local.Individual;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

/**
 * @author Rubén Rodríguez Esteban 737215 
 * 
 */

public class NQueensTP6P1 {

	private static final int _boardSize = 8;
	
	// Metodo que repite la generación del estado inicial hasta que se obtiene el exito
	private static void nQueensRandomRestartHillClimbing() {
		double fallos = 0;	
		double expandedNodes = 0;
		double nodosActuales = 0;
		
		try {
			// Creación del problema 
			Problem problem = new Problem(new NQueensBoard(_boardSize),
					NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(),
					new NQueensGoalTest());
		
			// Lanzamiento de la busqueda
			HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
			SearchAgent agent = new SearchAgent(problem, search);

			SearchOutcome outcome = SearchOutcome.SOLUTION_FOUND;
			
			// Comprobación de si ha habido o no exito
			while (!search.getOutcome().equals(outcome)){
					// Se vuelve a lanzar el problema
				    problem = new Problem(new NQueensBoard(_boardSize),
				    		NQueensFunctionFactory.getCActionsFunction(),
				    		NQueensFunctionFactory.getResultFunction(),
				    		new NQueensGoalTest());
			
				    // Ejecución de la nueva búsqueda
				    search = new HillClimbingSearch(new AttackingPairsHeuristic());
				    agent = new SearchAgent(problem, search);
				    
				    // Se incrementan los fallos y los nodos
				    fallos++;
				    nodosActuales = agent.getActions().size();
				    expandedNodes += nodosActuales;
			}
			
			// Se muestran las estadisticas obtenidas
			System.out.println(search.getOutcome());
			System.out.println(search.getLastSearchState());
			System.out.format("Numero de intentos: %.0f\n",(fallos + 1));
			System.out.format("Fallos: %.0f\n", fallos);
			System.out.format("Coste medio de fallos: %.2f\n", expandedNodes/fallos);
			System.out.format("Coste exito: %.2f\n ",nodosActuales);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Se lanzan "numExperiments" veces el algoritmo Hill-ClimbingSearch
	// y se mide el numero de aciertos, fallos, el promedio de coste de aciertos y fallos
	private static void nQueensHillClimbingSearch_Statistics(int numExperiments) {
		System.out.println("\nNQueensHillClimbing con 10000 estados iniciales diferentes -->");
		
		double expandedNodes;
		double exitos = 0;
		double fallos = 0;
		double nodosBien = 0;
		double nodosMal = 0;
		
		// Lista vacia donde se almacenan los tableros
		LinkedList<NQueensBoard> tablerosUsados = new LinkedList();
		
		// Realizacion de los experimentod
		for (int i = 0; i < numExperiments; i++) {	
			try {
				// Creación del estado inicial
				NQueensBoard tableroInicio = new NQueensBoard(_boardSize);
				
				// Verificación de si es estado repetido
				while (tablerosUsados.contains(tableroInicio)) {
					// Creación de un estado inicial nuevo
					tableroInicio = new NQueensBoard(_boardSize);
				}
				
				// Se añade el estado inicial no repetido a la lista
				tablerosUsados.add(tableroInicio);
				
				// Creación del problema
				Problem problem = new Problem(tableroInicio,
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				
				
				// Realización de la búsqueda
				HillClimbingSearch search = new HillClimbingSearch(new AttackingPairsHeuristic());
				SearchAgent agent = new SearchAgent(problem, search);

				
				SearchOutcome outcome = SearchOutcome.SOLUTION_FOUND;
										
				expandedNodes = agent.getActions().size();
				
				// Comprobación del estado final de la busqueda
				if (search.getOutcome().equals(outcome)) {
					//exito
					exitos++;
					nodosBien+= expandedNodes;
				}
				else {
					// fallo
					fallos++;
					nodosMal += expandedNodes;
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Muestreo de las estadisticas
		System.out.format("Fallos: %.0f\n", fallos);
		System.out.format("Coste medio fallos: %.2f\n",nodosMal/fallos);
		System.out.format("Exitos: %.0f\n", exitos);
		System.out.format("Coste medio exitos: %.2f\n\n", nodosBien/exitos);
	}
	
	
	

	private static void printInstrumentation(Properties properties) {
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}

	
	public static void main(String[] args) {

		nQueensHillClimbingSearch_Statistics(10000);
		
	    nQueensRandomRestartHillClimbing();
		
	}
}