module sql.core {
    // Exportamos los paquetes que la UI y el CLI necesitan usar
    exports dao;
    exports grammar; // Necesario porque Token usa TSymbol
    exports parser;
    exports parser.ast;
    exports scanner;
    exports semantic;
}
