package com.example.actividad_1_compiladores.Lexer

class Lexer(private val codigo: String) {
    private val tokens = mutableListOf<Token>()
    private val tablaSimbolos = TablaSimbolos()
    private var posicion = 0

    // Categorización de palabras reservadas
    private val tiposDatos = setOf("entero", "flotante", "booleano", "caracter", "cadena")
    private val booleanos = setOf("verdadero", "falso")
    private val condicionales = setOf("si", "sino")
    private val estructurasRepetitivas = setOf("mientras", "para")
    private val palabrasReservadas = setOf(
        "principal", "arreglo",
        "imprimir", "publico", "privado", "clase"
    )

    fun analizar(): Pair<List<Token>, TablaSimbolos> {
        while (posicion < codigo.length) {
            when (val caracter = codigo[posicion]) {
                // Ignorar espacios en blanco y saltos de línea
                ' ', '\t', '\n', '\r' -> posicion++

                // Identificadores y palabras reservadas
                in 'a'..'z', in 'A'..'Z', '_' -> {
                    val identificador = leerIdentificador()

                    // Categorizar según el tipo
                    val tipo = when {
                        identificador in booleanos -> TokenType.BOOLEANO
                        identificador in condicionales -> TokenType.CONDICIONAL
                        identificador in estructurasRepetitivas -> TokenType.ESTRUCTURA_REPETITIVA
                        identificador in tiposDatos -> TokenType.TIPO_DATO
                        identificador in palabrasReservadas -> TokenType.PALABRA_RESERVADA
                        else -> TokenType.IDENTIFICADOR
                    }

                    tokens.add(Token(tipo, identificador))
                    tablaSimbolos.agregar(identificador, tipo.toString())

                    // Verificar si es una declaración de arreglo
                    if (posicion < codigo.length && codigo[posicion] == '[') {
                        val arrayDecl = leerDeclaracionArreglo()
                        if (arrayDecl.isNotEmpty()) {
                            tokens.add(Token(TokenType.ARREGLO, arrayDecl))
                            tablaSimbolos.agregar(arrayDecl, TokenType.ARREGLO.toString())
                        }
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

                // Operadores
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
                '(', ')', '{', '}', '[', ']' -> {
                    val token = Token(TokenType.DELIMITADOR, caracter.toString())
                    tokens.add(token)
                    tablaSimbolos.agregar(caracter.toString(), token.tipo.toString())
                    posicion++
                }

                // Separadores
                ',', ';' -> {
                    tokens.add(Token(TokenType.SEPARADOR, caracter.toString()))
                    tablaSimbolos.agregar(caracter.toString(), TokenType.SEPARADOR.toString())
                    posicion++
                }

                // Acceder a métodos
                '.' -> {
                    tokens.add(Token(TokenType.ACCESO_METODO, caracter.toString()))
                    tablaSimbolos.agregar(caracter.toString(), TokenType.ACCESO_METODO.toString())
                    posicion++
                }

                // Fin de instrucción
                '~' -> {
                    val token = Token(TokenType.DELIMITADOR_FIN_INSTRUCCION, caracter.toString())
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

    private fun leerDeclaracionArreglo(): String {
        val sb = StringBuilder()
        while (posicion < codigo.length) {
            val c = codigo[posicion]
            sb.append(c)
            posicion++
            if (c == ']') break
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
        posicion++
        while (posicion < codigo.length) {
            val c = codigo[posicion]
            if (c == '"') {
                posicion++
                break
            } else {
                sb.append(c)
                posicion++
            }
        }
        return sb.toString()
    }
}