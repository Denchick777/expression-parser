import java.util.Scanner;

/**
 * This program calculates given mathematical expression via building AST tree
 * of operators and integers ({@code long} type). For this purpose there exist two classes
 * for work with AST structure: AstNode of tree and AstExpression (which may be inside
 * the other expression).
 * <p>
 * Classes LogicalExp, RelationExp, TermExp, FactorExp and PrimaryExp are extensions
 * of class AstExpression. Each of them stores specific set of operators for each
 * type of supported operations:<p>
 * Logical: {@code "and", "or", "xor"}<p>
 * Relation: {@code "<", "<=", ">", ">=", "=", "/="}<p>
 * Term: {@code "+", "-"}<p>
 * Factor: {@code "*", "/"}<p>
 * Primary: {@code "(", ")"} - only for defining expression with higher priority inside the other<p>
 * Note: Pre-integer sign of positive/negative integer is allowed (assumed equal to TermExp).
 * Those you get an expression with this sign and zero as the left child.
 * <p>
 * Each AstExpression stores content represented as a String (operator or integer).<p>
 * Also there is action applicable to this expression (implemented throw abstract method
 * that gets an array of Strings as set of arguments). In realizations of math expressions
 * this action is calculation of subtrees. Primary as container of integer only returns
 * itself's content.<p>
 * Note: PrimaryExp class doesn't store parenthesises because they're auxiliary.
 * <p>
 * To calculate expression you should use static method {@code buildExpAST} from the class
 * ExpTreeBuilder. This method creates special parser for mathematical expressions ExpParser,
 * which extends class StringWalker realized for AST expressions parsing with specified set
 * of delimiters (in our case - mathematical operators, see implementation).<p>
 * Note: ExpParser cuts off all the whitespaces.
 * <p>
 * To add new operator - modify methods {@code OPCODES} and {@code performAction} according
 * to the specific of your operator.<p>
 * To add new type of operators - create new extension of class AstExpression and modify
 * method {@code parse...} in lower-order type to save the order and firstly call for
 * higher-order operator parsing to save the functionality of parser.
 *
 * @author Denis Chernikov
 * @version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String exp;
        String[] ops = ExpTreeBuilder.getPossibleOpCodes();
        System.out.print("All available operators:");
        for (String s : ops) {
            System.out.print(" " + s);
        }
        System.out.println("\n");
        while (true) {
            System.out.println("Input expression (only long integers are acceptable):");
            exp = sc.nextLine();
//            exp = "1 + (26 - 98) / 15 + 777 < 28";  // Example of expression, comment previous to test it
            if (exp.isEmpty()) {
                System.out.println("No input provided. End of execution...");
                break;
            }
            try {
                AstNode head = ExpTreeBuilder.buildExpAST(exp);
                System.out.println("Parsed successfully");
                head.printSubtree();
                String res = head.actOnSubtree();
                // Replace arguments on ("", "") for single-line serialization
                System.out.println("JSON Serialization:\n" + head.subtreeToJson("  ", "\n"));
//                System.out.println("XML Serialization:\n" + head.subtreeToXml("  ", "\n"));
                System.out.println("Result: " + res);
            } catch (ExpressionFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
