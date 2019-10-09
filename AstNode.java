/**
 * Represents a head node for some Abstract Syntax Tree (or it's subtree).
 * Content of a node - {@code AstExpression}.
 */
public class AstNode {
    private AstExpression expression;
    private AstNode parent, lChild, rChild;

    public AstNode(AstExpression exp) {
        expression = exp;
        parent = null;
        lChild = null;
        rChild = null;
    }

    public AstExpression getExpression() {
        return expression;
    }

    public AstNode getParent() {
        return parent;
    }

    public AstNode getLeftChild() {
        return lChild;
    }

    public void setLeftChild(AstNode lChild) {
        this.lChild = lChild;
        if (lChild != null) {
            lChild.parent = this;
        }
    }

    public AstNode getRightChild() {
        return rChild;
    }

    public void setRightChild(AstNode rChild) {
        this.rChild = rChild;
        if (rChild != null) {
            rChild.parent = this;
        }
    }

    @Override
    public String toString() {
        return "{ expression : " + expression +
                ", leftChild : " + lChild.hashCode() +
                ", rightChild : " + rChild.hashCode() + "}";
    }

    /**
     * Print tree/subtree, which is starting from this node.
     * Format of output:
     * ├── el1<p>
     * │   ├── el3<p>
     * │   └── el4<p>
     * └── el2
     */
    public void printSubtree() {
        printSubtree("", true);
    }

    private void printSubtree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + expression.getContent());
        if (lChild != null) {
            lChild.printSubtree(prefix + (isTail ? "    " : "│   "), rChild == null);
            if (rChild != null) {
                rChild.printSubtree(prefix + (isTail ? "    " : "│   "), true);
            }
        } else if (rChild != null) {
            rChild.printSubtree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public String actOnSubtree() throws ExpressionFormatException {
        String[] args = {"", ""};
        if (lChild != null) {
            args[0] = lChild.actOnSubtree();
        }
        if (rChild != null) {
            args[1] = rChild.actOnSubtree();
        }
        return expression.performAction(args);
    }

    /**
     * Creates a JSON representation of this subtree. May be used instead of {@code toString} for subtree.
     *
     * @param tabulation String for pre-line tabulation
     * @param endLine    String at hte end of each line (for example, {@code "\n"})
     * @return String in JSON format with this subtree
     */
    public String subtreeToJson(String tabulation, String endLine) {
        return subtreeToJson("", tabulation, endLine);
    }

    private String subtreeToJson(String prefix, String tab, String nl) {
        String res = "{" + nl;
        res += prefix + tab + "\"expression\" : \"" + expression.getContent() + "\"," + nl;
        res += prefix + tab + "\"leftChild\" : " +
                (lChild == null ? "null" : lChild.subtreeToJson(prefix + tab, tab, nl)) + "," + nl;
        res += prefix + tab + "\"rightChild\" : " +
                (rChild == null ? "null" : rChild.subtreeToJson(prefix + tab, tab, nl)) + "," + nl;
        return res + prefix + "}";
    }

    /**
     * Creates an XML representation of this subtree. May be used instead of {@code toString} for subtree.
     *
     * @param tabulation String for pre-line tabulation
     * @param endLine    String at hte end of each line (for example, {@code "\n"})
     * @return String in XML format with this subtree
     */

    public String subtreeToXml(String tabulation, String endLine) {
        return subtreeToXml("", tabulation, endLine);
    }

    private String subtreeToXml(String prefix, String tab, String nl) {
        String res = "<AstNode expression=\"" + expression.getContent() + "\">" + nl;
        res += prefix + tab + "<leftChild>" +
                (lChild == null ? "" : nl + prefix + tab + tab +
                        lChild.subtreeToXml(prefix + tab + tab, tab, nl) + nl + prefix + tab) +
                "</LeftChild>" + nl;
        res += prefix + tab + "<RightChild>" +
                (rChild == null ? "" : nl + prefix + tab + tab +
                        rChild.subtreeToXml(prefix + tab + tab, tab, nl) + nl + prefix + tab) +
                "</RightChild>" + nl;
        return res + prefix + "</AstNode>";
    }
}
