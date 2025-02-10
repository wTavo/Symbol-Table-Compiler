package com.example.actividad_1_compiladores.Lexer

enum class TokenType(val pattern: String) {
    NUMERO_ENTERO("[0-9]+"), // Números enteros
    NUMERO_FLOTANTE("[0-9]+\\.[0-9]+"), // Números con decimales
    CADENA("\"[^\"]*\""), // Cadenas de texto
    CARACTER("'.'"), // Caracteres individuales
    BOOLEANO("verdadero|falso"), // Valores booleanos
    ARREGLO("\\b(entero|flotante|booleano|caracter|cadena)\\[\\]"), // Ejemplo: entero[], flotante[]
    PALABRA_RESERVADA("\\b(principal|entero|flotante|booleano|caracter|cadena|arreglo|verdadero|falso|imprimir|publico|privado|clase)\\b"),
    TIPO_DATO("\\b(entero|flotante|booleano|caracter|cadena)"),
    IDENTIFICADOR("[a-zA-Z_][a-zA-Z0-9_]*"),
    CONDICIONAL("\\b(si|sino)\\b"),
    ESTRUCTURA_REPETITIVA("\\b(mientras|para)\\b"),
    OPERADOR_ARITMETICO("[+\\-*/%]"),
    OPERADOR_ASIGNACION("=|\\+=|-=|\\*=|/=|%="), // =, +=, -=, *=, /=, %=
    OPERADOR_COMPARACION("==|!=|<=|>=|<|>"), // ==, !=, <=, >=, <, >
    OPERADOR_LOGICO("AND|OR|NOT"),
    DELIMITADOR_FIN_INSTRUCCION("XD"),
    DELIMITADOR("[\\[\\]{}()]"),
    SEPARADOR(",|;"), // Coma
    ACCESO_METODO("\\."), // Punto
    ESPACIO("\\s+"),
    DESCONOCIDO("")
}