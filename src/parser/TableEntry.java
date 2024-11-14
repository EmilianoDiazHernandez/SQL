package parser;

import grammar.NTSymbol;
import grammar.TSymbol;

import java.util.Objects;

public class TableEntry {

    NTSymbol nonTerminal;

    TSymbol terminal;

    public TableEntry(NTSymbol nonTerminal, TSymbol terminal) {
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
    }

    @Override
    public int hashCode (){
        return Objects.hash(nonTerminal, terminal);
    }

}
