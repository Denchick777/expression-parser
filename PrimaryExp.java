/**
 * Primary expression (long integer) of mathematical expression for storing in AST for later calculations.
 * Can store only integer (guaranteed by internal check).
 */
class PrimaryExp extends AstExpression {
    private PrimaryExp(String num) {
        super(num);
    }

    static String[] OPCODES = {"(", ")"};

    @Override
    String performAction(String[] args) {
        return content;
    }

    static boolean isThisOpCode(String opCode) {
        if (opCode == null || opCode.isEmpty()) {
            return false;
        }
        for (String curOp : OPCODES) {
            if (opCode.equals(curOp)) {
                return true;
            }
        }
        return false;
    }

    static AstNode parsePrimary(ExpParser parser, boolean isInsideParenthesis) throws ExpressionFormatException {
        AstNode res;
        String curParserExp = parser.getLast();  // Last fixed expression from the parser
        if (TermExp.isThisOpCode(curParserExp)) {  // If primary is precede by TermExp ("-" or "+")
            return new AstNode(new PrimaryExp("0"));
        }

        // Is primary - integer?
        if (isLongInteger(curParserExp)) {
            res = new AstNode(new PrimaryExp(curParserExp));

        } else if (isThisOpCode(curParserExp)) {  // Is next - parenthesis?
            if (curParserExp.equals(OPCODES[0])) {  // If "("...
                parser.moveNext();
                if (parser.isEnd()) {  // Opening parenthesis can't stay at the end of the string
                    throw new ExpressionFormatException("Wrong opening parenthesis detected");
                }
                // Parse subexpression from the top operations
                res = LogicalExp.parseLogical(parser, true);
                if (!parser.getLast().equals(OPCODES[1])) {  // If next is not ")" - exception
                    throw new ExpressionFormatException("Wrong opening parenthesis detected");
                }
            } else {  // If ")" instead of "(" - exception
                throw new ExpressionFormatException("Wrong closing parenthesis detected");
            }

        } else {  // If any unsupported sequence (not long int and not "(" or ")") - exception
            throw new ExpressionFormatException("Wrong expression format");
        }

        // Move if not the end
        if (!parser.isEnd()) {
            parser.moveNext();
            if (parser.getLast().equals(OPCODES[0])) {  // "(" can't stay after an integer or after ")"
                throw new ExpressionFormatException("Wrong parenthesis position detected (possibly multiplication missed)");
            } else if (parser.getLast().equals(OPCODES[1]) && !isInsideParenthesis) {  // No pair for ")"
                throw new ExpressionFormatException("Wrong closing parenthesis detected");
            }
        }

        return res;
    }

    private static boolean isLongInteger(String num) {
        try {
            Long.parseLong(num);
            return num.matches("^([+-])?\\d+$");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
