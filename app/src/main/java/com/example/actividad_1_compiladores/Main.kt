package com.example.actividad_1_compiladores

import com.example.actividad_1_compiladores.Lexer.Lexer

fun main() {
    println("Introduce el codigo o expresion (termina con un salto de linea):")
    val codigo = StringBuilder()
    while (true) {
        val linea = readLine() ?: ""
        if (linea.isBlank()) {
            break // Terminar si la línea está en blanco
        }
        codigo.append(linea).append("\n")
    }

    val lexer = Lexer(codigo.toString())

    try {
        // Analizamos el código
        val (tokens, tablaSimbolos) = lexer.analizar()

        // Mostramos la tabla de símbolos
        tablaSimbolos.mostrar()
    } catch (e: IllegalArgumentException) {
        println("Error al analizar el código: ${e.message}")
    }
}
