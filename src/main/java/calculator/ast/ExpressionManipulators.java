package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import calculator.gui.ImageDrawer;

public class ExpressionManipulators {

    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    public static AstNode handleToDouble(Environment env, AstNode node) {
       
        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
    	if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            String name = node.getName();			 // There are three types of nodes,  
            if (variables.containsKey(name)) {       // so we have three cases.
                return toDoubleHelper(variables, variables.get(name)); 
            } else {
                throw new EvaluationError("UNKNOWN OPERATION");
            }
        } else {
            String name = node.getName();
            if (name.equals("+")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) + 
                        toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("-")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) - 
                        toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("*")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) * 
                        toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("/")) {
                return toDoubleHelper(variables, node.getChildren().get(0)) / 
                        toDoubleHelper(variables, node.getChildren().get(1));
            } else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, node.getChildren().get(0)), 
                        toDoubleHelper(variables, node.getChildren().get(1)));
            } else if (name.equalsIgnoreCase("negate")) {
                return -(toDoubleHelper(variables, node.getChildren().get(0)));
            } else if (name.equalsIgnoreCase("sin")) {
                return Math.sin(toDoubleHelper(variables, node.getChildren().get(0)));
            }   else if (name.equalsIgnoreCase("sinh")) { //Extra credit
                return Math.sinh(toDoubleHelper(variables, node.getChildren().get(0)));
            }   else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, node.getChildren().get(0)));
            } else if (name.equalsIgnoreCase("cosh")) { //Extra credit
                return Math.cosh(toDoubleHelper(variables, node.getChildren().get(0)));
              }
               else if (name.equals("tan")) { //Extra credit
                  return Math.tan(toDoubleHelper(variables, node.getChildren().get(0)));
               } else if (name.equals("sqrt")) { //Extra credit
                   return Math.sqrt(toDoubleHelper(variables, node.getChildren().get(0)));
             } else {
            throw new EvaluationError("UNKNOWN OPERATION"); 
         }
       }
    }

    public static AstNode handleSimplify(Environment env, AstNode node) {
    	
        assertNodeMatches(node, "simplify", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return simplifyHelper(env, exprToConvert);
    }
    
    private static AstNode simplifyHelper(Environment env, AstNode node) {
    	
        if (node.isNumber()) {
            return node;
        } else if (node.isVariable()) {
            String n = node.getName();
            IDictionary<String, AstNode> var = env.getVariables();
            if (var.containsKey(n)) {
                node = var.get(n);
                return simplifyHelper(env, node);
            } else {
              return node;
            }
         } 
        else {
           String n = node.getName();
           IList<AstNode> childrens = node.getChildren();
           if (childrens.size() == 2) {
               if ((n.equals("+") || n.equals("-")  || n.equals("*")) &&
            		   childrens.get(0).isNumber() && childrens.get(1).isNumber()){ 
                   IList<AstNode> passingNode = new DoubleLinkedList<AstNode>();
                   passingNode.add(node);
                   return handleToDouble(env, new AstNode("toDouble", passingNode));
               } 
               else {
                    IList<AstNode> simplNode = new DoubleLinkedList<AstNode>();  //The simplified passed node 
                    simplNode.add(simplifyHelper(env, childrens.get(0)));		 //will save the operation name 
                    simplNode.add(simplifyHelper(env, childrens.get(1)));        //with two children simplified.
                    return new AstNode(n, simplNode);                                           
           	 	}
        	}
        else if (n.equals("/") || n.equals("^")) {
            		IList<AstNode> simplNode = new DoubleLinkedList<>();
            		simplNode.add(simplifyHelper(env, node.getChildren().get(0)));
            		simplNode.add(simplifyHelper(env, node.getChildren().get(1)));
            		return new AstNode(n, simplNode);
        }  
        else if (n.equals("cos") || n.equals("sin") || n.equals("negate") ||
                n.equals("cosh") || n.equals("sinh")) {
            		IList<AstNode> simplNode = new DoubleLinkedList<>();
            		simplNode.add(simplifyHelper(env, node.getChildren().get(0)));
            		return new AstNode(n, simplNode);
        }
        else {
            throw new EvaluationError("Unknown variables");
        }
      }    
    }
    
    public static AstNode plot(Environment env, AstNode node) {
        assertNodeMatches(node, "plot", 5);
        IList<AstNode> childrens = new DoubleLinkedList<AstNode>();
        IDictionary<String, AstNode> variables = env.getVariables(); 
        childrens = node.getChildren();
        
        AstNode function = childrens.get(0);
        AstNode variableName = childrens.get(1);
        AstNode vMin = childrens.get(2);              //making the five children of the node
        AstNode vMax = childrens.get(3);
        AstNode increment = childrens.get(4);

        IList<AstNode> func = new DoubleLinkedList<AstNode>();
        IList<AstNode> varMin = new DoubleLinkedList<AstNode>();   //changing the vMin, vMax,
        IList<AstNode> varMax = new DoubleLinkedList<AstNode>();   //increment type from AstNode to Double
        IList<AstNode> step = new DoubleLinkedList<AstNode>();
        
        func.add(function);
        varMin.add(vMin);
        varMax.add(vMax);
        step.add(increment);
        
        double incr =  handleToDouble(env, new AstNode("toDouble", step)).getNumericValue();
        double min = handleToDouble(env, new AstNode("toDouble", varMin)).getNumericValue(); 
        double max = handleToDouble(env, new AstNode("toDouble", varMax)).getNumericValue();  
                                                                                             
                                                                                        
        	if (incr <= 0) {                                 //The increment cannot be zero or a negative value. 
            throw new EvaluationError("Unknown variables"); 
        	} else if (min > max) {              			//The minimum is greater than maximum.
                throw new EvaluationError("Unknown variables");
            } else if (variables.containsKey(variableName.getName())) {    //The variable is defined;
                throw new EvaluationError("Unknown variables");            // it cannot be defined again.
            } else {
            IList<Double> x = new DoubleLinkedList<Double>();   //Taking the points of the plot coordinate
            IList<Double> y = new DoubleLinkedList<Double>(); 
            for (double i = min; i <= max; i += incr) {
            	x.add(i);
                variables.put(variableName.getName(), new AstNode(i));
                double yValue = handleToDouble(env, new AstNode("toDouble", func)).getNumericValue();
                y.add(yValue);
            }
            ImageDrawer image = env.getImageDrawer();
            image.drawScatterPlot("Plot", variableName.getName(), "Output", x, y);  //Operating scatter points/plotting.
            variables.remove(variableName.getName());   //removing 
            return new AstNode(1);
        }
    }
}