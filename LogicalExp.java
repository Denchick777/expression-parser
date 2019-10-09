class LogicalExp extends AstExpression {
    private LogicalExp(String opCode) {
        super(opCode);
    }

    static String[] OPCODES = {"and", "or", "xor"};

    @Override
    String performAction(String[] args) throws ExpressionFormatException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        long left = Long.parseLong(args[0]);
        long right = Long.parseLong(args[1]);
        switch (content) {
            case "and":
                return String.valueOf(left & right);
            case "or":
                return String.valueOf(left | right);
            case "xor":
                return String.valueOf(left ^ right);
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

    static AstNode parseLogical(ExpParser parser, boolean isInsideParenthesis) throws ExpressionFormatException {
        AstNode res = RelationExp.parseRelation(parser, isInsideParenthesis);
        while (isThisOpCode(parser.getLast())) {
            AstNode newNode = new AstNode(new LogicalExp(parser.getLast()));
            parser.moveNext();
            newNode.setLeftChild(res);
            newNode.setRightChild(RelationExp.parseRelation(parser, isInsideParenthesis));
            res = newNode;
        }
        return res;
    }
}
