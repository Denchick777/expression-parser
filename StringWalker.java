/**
 * Represents parser that allows to separate some string into parts by some separators.
 * Each step returns expression and separator in succession.
 * Firstly - string before/between separator(s), secondly - separator itself, etc.
 * <p>
 * Note: parser will divide string between separators if some symbol which is beginning of
 * any separator met. For better performance check if your string don't have non-separator sequence
 * which starts like any of separators.
 */
public class StringWalker {
    private String line;
    private String[] separators;
    private int position;
    private String lastReturn;

    public StringWalker(String line, String[] separators) {
        this.line = line;
        this.separators = separators;
        position = 0;
        moveNext();
    }

    private enum State {begin, isSeparator, isContent}

    /**
     * Move current position of parser for the next element (separator or between two of them) of the string.
     *
     * @return Last parsed element of the string (also available throw method {@code getLast})
     */
    public String moveNext() {
        if (isEnd()) return "";
        State curState = State.begin;
        StringBuilder buffer = new StringBuilder();
        boolean endOfPart = false;
        while (!isEnd()) {
            String curChar = Character.toString(line.charAt(position));
            String curBuff = buffer.toString();
            switch (curState) {
                case begin:  // Set the mode of parser (separator or not)
                    for (String s : separators) {
                        if (s.startsWith(curChar)) {
                            curState = State.isSeparator;
                            break;
                        }
                    }
                    if (curState == State.begin) {
                        curState = State.isContent;
                    }
                    continue;
                case isSeparator:  // Check current buffer via set of separators
                    boolean buffIncreased = false;
                    for (String s : separators) {
                        if (s.startsWith(curBuff + curChar)) {
                            buffer.append(curChar);
                            buffIncreased = true;
                            break;
                        }
                    }
                    if (!buffIncreased) {
                        endOfPart = true;
                    }
                    break;
                case isContent:  // Continue check while no operator's beginning met
                    boolean sepFound = false;
                    for (String s : separators) {
                        if (s.startsWith(curChar)) {
                            sepFound = true;
                            break;
                        }
                    }
                    if (sepFound) {
                        endOfPart = true;
                        break;
                    } else {
                        buffer.append(curChar);
                    }
                    break;
            }
            if (endOfPart) {
                break;
            }
            ++position;
        }
        lastReturn = buffer.toString();
        return lastReturn;
    }

    /**
     * Get last parsed element after execution of method {@code moveNext}.
     * Before first move it returns the very first element of string.
     *
     * @return Last parsed element of string
     */
    public String getLast() {
        return lastReturn;
    }

    /**
     * Returns the beginning position of element from {@code getLast} in the string.
     * Note: counting starts with 0 and ends at {@code string.length() - 1}.
     *
     * @return Number of position in the string
     */
    public int getPosition() {
        return position - lastReturn.length();
    }

    /**
     * Is an element from the method {@code getLast} the very last in the string?
     *
     * @return {@code true} is that is the last element, {@code false} otherwise
     */
    public boolean isEnd() {
        return position >= line.length();
    }
}
