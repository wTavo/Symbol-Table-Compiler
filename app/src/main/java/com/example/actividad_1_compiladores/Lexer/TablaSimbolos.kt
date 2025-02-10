package com.example.actividad_1_compiladores.Lexer

class TablaSimbolos {
    private val simbolos = mutableListOf<InfoSimbolo>()
    private var idCounter = 1 // Contador para el id único

    data class InfoSimbolo(val id: Int, val nombre: String, val tipo: String)

    fun agregar(nombre: String, tipo: String) {
        simbolos.add(InfoSimbolo(idCounter++, nombre, tipo))
    }

    fun mostrar() {
        println("\nTabla de simbolos:")
        println("ID\tNombre\t\tCategoria")
        simbolos.forEach { info ->
            println("${info.id}\t${info.nombre}\t\t${info.tipo}")
        }
    }
}
