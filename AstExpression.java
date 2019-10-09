public abstract class AstExpression {
    String content;

    AstExpression(String content) {
        this.content = content;
    }

    String getContent() {
        return content;
    }

    abstract String performAction(String[] args) throws ExpressionFormatException;
}
