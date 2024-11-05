package parser;

import grammar.NonTerminalSymbol;
import grammar.TerminalSymbol;

public class TableEntry {

    NonTerminalSymbol nonTerminal;

    TerminalSymbol terminal;

    TableEntry(NonTerminalSymbol nonTerminal, TerminalSymbol terminal) {
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
    }

    @Override
    public int hashCode (){
        return nonTerminal.hashCode() + terminal.hashCode();
    }

}
