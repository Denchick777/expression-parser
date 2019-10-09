class ExpParser extends StringWalker {
    ExpParser(String expression) {
        super(expression.replaceAll("\\s", "").toLowerCase(), getAllOpCodes());
    }

    static String[] getAllOpCodes() {
        int setSize = LogicalExp.OPCODES.length
                + RelationExp.OPCODES.length
                + TermExp.OPCODES.length
                + FactorExp.OPCODES.length
                + PrimaryExp.OPCODES.length;
        String[] opCodes = new String[setSize];
        int i = 0;
        int start = 0;
        for (; i < start + LogicalExp.OPCODES.length; i++) {
            opCodes[i] = LogicalExp.OPCODES[i - start];
        }
        start = i;
        for (; i < start + RelationExp.OPCODES.length; i++) {
            opCodes[i] = RelationExp.OPCODES[i - start];
        }
        start = i;
        for (; i < start + TermExp.OPCODES.length; i++) {
            opCodes[i] = TermExp.OPCODES[i - start];
        }
        start = i;
        for (; i < start + FactorExp.OPCODES.length; i++) {
            opCodes[i] = FactorExp.OPCODES[i - start];
        }
        start = i;
        for (; i < start + PrimaryExp.OPCODES.length; i++) {
            opCodes[i] = PrimaryExp.OPCODES[i - start];
        }
        return opCodes;
    }
}
