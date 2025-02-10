package com.example.actividad_1_compiladores

import com.example.actividad_1_compiladores.Lexer.Lexer

fun main() {
    println("Introduce el codigo o expresion (escribe 'FIN' en una línea nueva para terminar):")
    val codigo = StringBuilder()
    var linea: String?

    while (true) {
        linea = readLine()
        if (linea == "FIN") {
            break
        }
        if (linea != null) {
            codigo.append(linea)
            codigo.append("\n")
        }
    }

    val lexer = Lexer(codigo.toString().trim())

    try {
        // Analizamos el código
        val (tokens, tablaSimbolos) = lexer.analizar()

        // Mostramos los tokens encontrados
        println("\nTokens encontrados:")
        tokens.forEachIndexed { index, token ->
            println("${index + 1}. ${token.tipo}: ${token.valor}")
        }

        // Mostramos la tabla de símbolos
        println("\nTabla de símbolos:")
        tablaSimbolos.mostrar()
    } catch (e: IllegalArgumentException) {
        println("Error al analizar el código: ${e.message}")
    }
}