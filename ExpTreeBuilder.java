/**
 * Returns Abstract Syntax Tree head node ({@code AstNode}) of given mathematical expression.
 * {@code getPossibleOpCodes} returns a set of all supported operators.
 * All integers are checked valid strings ({@code long} type).
 */
public class ExpTreeBuilder {
    public static AstNode buildExpAST(String expression) throws ExpressionFormatException {
        ExpParser parser = new ExpParser(expression);
        AstNode head = LogicalExp.parseLogical(parser, false);
        if (!parser.isEnd()) {
            throw new ExpressionFormatException("Wrong expression format");
        }
        return head;
    }

    public static String[] getPossibleOpCodes() {
        return ExpParser.getAllOpCodes();
    }
}
