package com.example.actividad_1_compiladores.Lexer

class Lexer(private val codigo: String) {
    private val tokens = mutableListOf<Token>()
    private val tablaSimbolos = TablaSimbolos()
    private var posicion = 0

    // Lista de palabras reservadas
    private val palabrasReservadas = setOf(
        "principal", "entero", "flotante", "booleano", "caracter", "cadena", "arreglo",
        "verdadero", "falso", "imprimir", "si", "sino", "mientras", "para", "publico", "privado"
    )

    fun analizar(): Pair<List<Token>, TablaSimbolos> {
        while (posicion < codigo.length) {
            when (val caracter = codigo[posicion]) {
                // Ignorar espacios en blanco y saltos de línea
                ' ', '\t', '\n', '\r' -> posicion++

                // Identificadores (variables, funciones, etc.)
                in 'a'..'z', in 'A'..'Z', '_' -> {
                    val identificador = leerIdentificador()

                    // Verificar si es una palabra reservada
                    if (identificador in palabrasReservadas) {
                        tokens.add(Token(TokenType.PALABRA_RESERVADA, identificador))
                        tablaSimbolos.agregar(identificador, TokenType.PALABRA_RESERVADA.toString())
                    } else {
                        // Es un identificador (variable, función, etc.)
                        tokens.add(Token(TokenType.IDENTIFICADOR, identificador))
                        tablaSimbolos.agregar(identificador, TokenType.IDENTIFICADOR.toString())
                    }
                }

                // Números (enteros o flotantes)
                in '0'..'9' -> {
                    val numero = leerNumero()
                    if (numero.contains(".")) {
                        tokens.add(Token(TokenType.NUMERO_FLOTANTE, numero))
                        tablaSimbolos.agregar(numero, TokenType.NUMERO_FLOTANTE.toString())
                    } else {
                        tokens.add(Token(TokenType.NUMERO_ENTERO, numero))
                        tablaSimbolos.agregar(numero, TokenType.NUMERO_ENTERO.toString())
                    }
                }

                // Operadores y símbolos
                '+', '-', '*', '/', '%', '=', '>', '<', '!', '&', '|' -> {
                    val operador = leerOperador(caracter)
                    val tipo = when (operador) {
                        "+", "-", "*", "/", "%" -> TokenType.OPERADOR_ARITMETICO
                        "=", "+=", "-=", "*=", "/=", "%=" -> TokenType.OPERADOR_ASIGNACION
                        "==", "!=", "<=", ">=", "<", ">" -> TokenType.OPERADOR_COMPARACION
                        "&&", "||", "!" -> TokenType.OPERADOR_LOGICO
                        else -> throw IllegalArgumentException("Operador desconocido: $operador")
                    }
                    tokens.add(Token(tipo, operador))
                    tablaSimbolos.agregar(operador, tipo.toString())
                }

                // Delimitadores
                '(', ')', '{', '}', '[', ']', ';' -> {
                    val token = Token(TokenType.DELIMITADORES, caracter.toString())
                    tokens.add(token)
                    tablaSimbolos.agregar(caracter.toString(), token.tipo.toString())
                    posicion++
                }

                // Cadenas
                '"' -> {
                    val cadena = leerCadena()
                    tokens.add(Token(TokenType.CADENA, cadena))
                    tablaSimbolos.agregar(cadena, TokenType.CADENA.toString())
                }

                // Cualquier otro carácter desconocido
                else -> throw IllegalArgumentException("Carácter desconocido: $caracter")
            }
        }
        return Pair(tokens, tablaSimbolos)
    }

    private fun leerIdentificador(): String {
        val sb = StringBuilder()
        while (posicion < codigo.length) {
            val c = codigo[posicion]
            if (c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9' || c == '_') {
                sb.append(c)
                posicion++
            } else {
                break
            }
        }
        return sb.toString()
    }

    private fun leerNumero(): String {
        val sb = StringBuilder()
        var tienePunto = false
        while (posicion < codigo.length) {
            val c = codigo[posicion]
            if (c in '0'..'9') {
                sb.append(c)
                posicion++
            } else if (c == '.' && !tienePunto) {
                sb.append(c)
                posicion++
                tienePunto = true
            } else {
                break
            }
        }
        return sb.toString()
    }

    private fun leerOperador(caracter: Char): String {
        val sb = StringBuilder()
        sb.append(caracter)
        posicion++
        // Verificar si es un operador compuesto (==, !=, +=, etc.)
        if (posicion < codigo.length) {
            val siguiente = codigo[posicion]
            when (caracter) {
                '=', '!', '+', '-', '*', '/', '%', '>', '<', '&', '|' -> {
                    if (siguiente == '=' || siguiente == '&' || siguiente == '|') {
                        sb.append(siguiente)
                        posicion++
                    }
                }
            }
        }
        return sb.toString()
    }

    private fun leerCadena(): String {
        val sb = StringBuilder()
        posicion++ // Saltar la comilla de apertura
        while (posicion < codigo.length) {
            val c = codigo[posicion]
            if (c == '"') {
                posicion++ // Saltar la comilla de cierre
                break
            } else {
                sb.append(c)
                posicion++
            }
        }
        return sb.toString()
    }
}