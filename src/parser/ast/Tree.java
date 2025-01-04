package parser.ast;

import grammar.TSymbol;
import parser.ast.nodes.*;
import scanner.token.ControlStructure;
import scanner.token.Operand;
import scanner.token.Operator;
import scanner.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tree {
    public final NodeQuery root;

    public Tree(List<Token> query) {
        this.root = buildTree(query);
    }

    private NodeQuery buildTree(List<Token> query) {
        NodeSelect select = null;
        NodeFrom from = null;
        NodeWhere where = null;
        int i = 0;
        while (i < query.size()) {
            if (query.get(i) instanceof ControlStructure) {
                switch (query.get(i).type) {
                    case SELECT:
                        ArrayList<NodeExp> expressions = new ArrayList<>();
                        ArrayList<Token> expression = new ArrayList<>();
                        i++;
                        while (query.get(i).type != TSymbol.FROM) {
                            expression.add(query.get(i));

                            if (query.get(i).type == TSymbol.COMA || query.get(i + 1).type == TSymbol.FROM) {
                                expressions.add(buildExpression(expression));
                                expression.clear();
                            }
                            i++;
                        }
                        select = new NodeSelect(expressions);
                        break;
                    case FROM:
                        ArrayList<NodeTable> tables = new ArrayList<>();
                        ArrayList<Token> table = new ArrayList<>();
                        i++;
                        while (query.get(i).type != TSymbol.WHERE && query.get(i).type != TSymbol.EOF) {
                            table.add(query.get(i));

                            if (query.get(i).type == TSymbol.COMA || query.get(i + 1).type == TSymbol.WHERE || query.get(i + 1).type == TSymbol.EOF) {
                                tables.add(buildTable(table));
                                table.clear();
                            }
                            i++;
                        }
                        from = new NodeFrom(tables);
                        break;
                    case WHERE:
                        ArrayList<Token> condition = new ArrayList<>();
                        i++;
                        while (i < query.size()) {
                            if (query.get(i).type != TSymbol.EOF)
                                condition.add(query.get(i));
                            else
                                where = new NodeWhere(buildExpression(condition));
                            i++;
                        }
                        break;
                }
            } else {
                System.out.println("Error: Invalid token " + query.get(i).lexeme);
                i++;
            }
        }
        return new NodeQuery(select, from, where);
    }

    private NodeExp buildExpression(List<Token> expression) {
        Stack<NodeExp> stack = new Stack<>();

        for (Token token : expression) {
            if (token instanceof Operand) {
                stack.push(new NodeExp(token));
            } else if (token instanceof Operator) {
                NodeExp node = new NodeExp(token);

                node.right = stack.pop();
                node.left = stack.pop();

                stack.push(node);
            }
        }

        return stack.pop();
    }

    public NodeTable buildTable(List<Token> tokens) {
        Token table = tokens.get(0);
        Token alias = tokens.size() > 1 ? tokens.get(1) : null;
        return new NodeTable(table, alias);
    }

    public void printAST(NodeQuery query, int depth) {
        if (query == null) return;

        printIndent("QUERY", depth);

        if (query.select != null) {
            printIndent("SELECT", depth + 1);
            for (NodeExp expression : query.select.columns) {
                printTree(expression, depth + 2);
            }
        }

        if (query.from != null) {
            printIndent("FROM", depth + 1);
            for (NodeTable table : query.from.tables) {
                printIndent(table.table + " -> " + table.alias, depth + 2);
            }
        }

        if (query.where != null) {
            printIndent("WHERE", depth + 1);
            printTree(query.where.condition, depth + 2);
        }
    }

    private void printIndent(String message, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("   ");
        System.out.println(message);
    }

    public void printTree(NodeExp node, int depth) {
        if (node == null) return;

        printIndent(node.value.lexeme, depth);
        printTree(node.left, depth + 1);
        printTree(node.right, depth + 1);
    }
}
