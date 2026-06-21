# Compilador en Java

Compilador desarrollado para la materia **Diseño de Compiladores** de la facultad. Toma como entrada un programa escrito en un lenguaje propio, realiza el análisis léxico, sintáctico y semántico, y genera código **Assembler x86 (MASM32)** como salida.

El proyecto recorre todas las etapas clásicas de la construcción de un compilador:

```
Código fuente
     │
     ▼
┌──────────────────┐
│ Analizador Léxico│  ← autómata por matrices de transición/acción (CSV)
└──────────────────┘
     │  tokens
     ▼
┌──────────────────────────────┐
│ Analizador Sintáctico (YACC) │  ← gramática + acciones semánticas
│  + Análisis Semántico        │  ← chequeo de tipos, ámbitos, rangos
└──────────────────────────────┘
     │  tercetos (código intermedio)
     ▼
┌──────────────────────┐
│ Generador de Código  │  ← traducción a Assembler MASM32
└──────────────────────┘
     │
     ▼
  salida.asm
```

## Arquitectura

El código fuente está organizado en `src/`:

| Paquete / archivo | Responsabilidad |
|---|---|
| `lexico/AnalizadorLexico.java` | Autómata finito que reconoce tokens recorriendo las matrices de transición y acción. |
| `lexico/AccionesSemanticas/` | Acciones semánticas del léxico (`AS1`..`AS10`, `ASE`) ejecutadas durante el reconocimiento de tokens. |
| `lexico/TablaDeSimbolos.java` | Tabla de símbolos con manejo de contextos y ámbitos. |
| `lexico/TablaTipoToken.java`, `Token.java`, `Lexema.java`, `MapeoCaracteres.java` | Soporte del análisis léxico (tipos de token, mapeo de caracteres a columnas de la matriz). |
| `parser/gramatica.y` | Gramática YACC con las reglas sintácticas, recuperación de errores y acciones semánticas (chequeo de tipos, generación de tercetos). |
| `parser/Parser.java` | **Parser generado** a partir de `gramatica.y` con BYACC/J (no editar a mano). |
| `parser/Terceto.java` | Representación del código intermedio (tercetos). |
| `parser/Error.java` | Errores léxicos, sintácticos y semánticos. |
| `codigo/GeneradorDeCodigo.java` | Traduce la lista de tercetos a Assembler MASM32 (`salida.asm`). |
| `utils/MatrizTransicion.java`, `utils/MatrizAccion.java` | Carga de las matrices del autómata desde los CSV. |

Las matrices del autómata léxico se encuentran en:

- `src/MATRIZ DE TRANSICIONES - Hoja 1.csv` — matriz de transición de estados.
- `src/MATRIZ DE TRANSICIONES - Hoja 2.csv` — matriz de acciones semánticas.

## El lenguaje

El lenguaje de entrada soporta, entre otras, las siguientes construcciones:

- **Estructura del programa**: `nombre BEGIN ... END`.
- **Tipos de dato**: `SINGLE` (punto flotante), `LONGINT` (entero) y `HEXADECIMAL`.
- **Declaraciones**: variables, listas de variables, subtipos con rangos (`TYPEDEF`), y triples (`TRIPLE<tipo>`).
- **Asignación**: `id := expresion;`.
- **Expresiones aritméticas**: `+ - * /` con precedencia.
- **Selección**: `IF (condicion) THEN ... ELSE ... END_IF`.
- **Bucles**: `REPEAT ... WHILE (condicion)`.
- **Saltos**: etiquetas (`etiqueta:`) y `GOTO etiqueta@`.
- **Funciones**: `tipo FUN nombre(parametro) BEGIN ... RET(expr) END` con chequeo de tipo de retorno y de parámetros.
- **Salida**: `OUTF(expresion)` / `OUTF(cadena)`.
- **Comparadores**: `< > = <= >= <>` y comparación de listas de expresiones.

El compilador también realiza **control de rangos** según el tipo (límites de `LONGINT`, `HEXADECIMAL`, `SINGLE`) y reporta errores con número de línea, con recuperación de errores en la gramática para detectar múltiples problemas en una sola corrida.

Hay programas de ejemplo en `tests/`, incluyendo casos exitosos (`tests/exitosos/`) y casos de error (`tests/TP4Errores*`, etc.).

## Compilación y ejecución

### Requisitos
- **JDK** (Java 8 o superior) para compilar y ejecutar el compilador.
- **MASM32** para ensamblar la salida `.asm` (la salida usa `\masm32\include\masm32rt.inc`).

### Compilar el proyecto

Desde la raíz del repositorio:

```bash
javac -d bin src/lexico/*.java src/lexico/AccionesSemanticas/*.java src/parser/*.java src/codigo/*.java src/utils/*.java
```

### Ejecutar el compilador

La clase principal es `parser.Parser`. Recibe como argumento la ruta del archivo a compilar:

```bash
java -cp "bin;src" parser.Parser tests/exitosos/bucles
```

> En Linux/macOS reemplazar el `;` del classpath por `:`.

Las matrices CSV se leen mediante rutas relativas (`src/...`), por lo que conviene ejecutar el compilador **desde la raíz del repositorio**.

Si no se especifica archivo, el `main` usa un archivo de prueba por defecto (`TP3CP9`) y muestra:

- la lista de **estructuras** reconocidas,
- los **errores** (léxicos, sintácticos y semánticos) y warnings,
- los **tercetos** generados,
- y, si no hubo errores, genera el Assembler en `src/codigo/salida.asm`.

### Ensamblar la salida (MASM32)

Una vez generado `salida.asm`, se ensambla y enlaza con MASM32:

```bat
\masm32\bin\ml /c /coff salida.asm
\masm32\bin\link /SUBSYSTEM:CONSOLE salida.obj
```

## Regenerar el parser

`Parser.java` está generado a partir de `parser/gramatica.y` usando **BYACC/J**. Si se modifica la gramática, hay que regenerarlo (por ejemplo con el `yacc.exe` incluido en `src/parser/`):

```bash
yacc -J gramatica.y
```

Esto produce `Parser.java` (y los archivos auxiliares `y.tab.c` / `y.output`). No editar `Parser.java` directamente: los cambios deben hacerse en `gramatica.y`.

## Estructura del repositorio

```
src/        Código fuente del compilador
  lexico/   Analizador léxico, tabla de símbolos y acciones semánticas
  parser/   Gramática YACC y parser generado
  codigo/   Generador de código Assembler
  utils/    Carga de matrices del autómata
tests/      Programas de prueba (casos exitosos y de error)
bin/        Artefactos de compilación / salida
```
