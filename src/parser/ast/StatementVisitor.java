package parser.ast;

public interface StatementVisitor<R> {
    R visitSelect(Select select);
}
