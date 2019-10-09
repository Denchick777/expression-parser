class RelationExp extends AstExpression {
    private RelationExp(String opCode) {
        super(opCode);
    }

    static String[] OPCODES = {"<", "<=", ">", ">=", "=", "/="};

    @Override
    String performAction(String[] args) throws ExpressionFormatException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        long left = Long.parseLong(args[0]);
        long right = Long.parseLong(args[1]);
        switch (content) {
            case "<":
                return String.valueOf(left < right ? 1L : 0L);
            case "<=":
                return String.valueOf(left <= right ? 1L : 0L);
            case ">":
                return String.valueOf(left > right ? 1L : 0L);
            case ">=":
                return String.valueOf(left >= right ? 1L : 0L);
            case "=":
                return String.valueOf(left == right ? 1L : 0L);
            case "/=":
                return String.valueOf(left != right ? 1L : 0L);
            default:
                throw new ExpressionFormatException("Wrong operator of action");
        }
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

    static AstNode parseRelation(ExpParser parser, boolean isInsideParenthesis) throws ExpressionFormatException {
        AstNode res = TermExp.parseTerm(parser, isInsideParenthesis);
        if (isThisOpCode(parser.getLast())) {
            AstNode newNode = new AstNode(new RelationExp(parser.getLast()));
            parser.moveNext();
            newNode.setLeftChild(res);
            newNode.setRightChild(TermExp.parseTerm(parser, isInsideParenthesis));
            res = newNode;
        }
        if (isThisOpCode(parser.getLast())) {  // Only one relation is available on one level by syntax description
            throw new ExpressionFormatException("Wrong expression format (only one relation in a row is acceptable)");
        }
        return res;
    }
}
