package parser;

import grammar.NTSymbol;
import grammar.TSymbol;

public class TableEntry {

    NTSymbol nonTerminal;

    TSymbol terminal;

    TableEntry(NTSymbol nonTerminal, TSymbol terminal) {
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
    }

    @Override
    public int hashCode (){
        return nonTerminal.hashCode() + terminal.hashCode();
    }

}
