# 🖥️ SimuladorSO - Simulador de Sistemas Operativos

<div align="center">

![Java Version](https://img.shields.io/badge/Java-21%20LTS-orange?logo=openjdk&logoColor=white)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?logo=checkmarx&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-blue?logo=windows&logoColor=white)
![Architecture](https://img.shields.io/badge/Design-Modular%20%26%20Clean%20Code-purple)
![License](https://img.shields.io/badge/License-Academic%20Use-lightgrey)

**Simulador educativo de consola para la enseñanza y análisis de algoritmos fundamentales de Sistemas Operativos.**  
Implementación robusta, desacoplada y orientada a objetos en Java moderno.

---

</div>

## 📌 Tabla de Contenidos

- [📖 ¿Qué es SimuladorSO?](#-qué-es-simuladorso)
- [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
- [⚙️ Arquitectura y Principios de Diseño](#️-arquitectura-y-principios-de-diseño)
- [🧠 Módulos y Algoritmos](#-módulos-y-algoritmos)
  - [1. Planificación de CPU (FCFS y Round Robin)](#1-planificación-de-cpu-fcfs-y-round-robin)
  - [2. Reemplazo de Páginas de Memoria (FIFO)](#2-reemplazo-de-páginas-de-memoria-fifo)
- [📂 Estructura del Proyecto](#-estructura-del-proyecto)
- [🚀 Guía de Instalación y Ejecución](#-guía-de-instalación-y-ejecución)
  - [Prerrequisitos](#prerrequisitos)
  - [Compilación](#compilación)
  - [Ejecución](#ejecución)
  - [Abrir en IntelliJ IDEA](#abrir-en-intellij-idea)
- [💻 Guía de Uso y Demostraciones](#-guía-de-uso-y-demostraciones)
  - [Módulo de CPU](#módulo-de-cpu)
  - [Módulo de Memoria Virtual](#módulo-de-memoria-virtual)
- [🛡️ Validaciones y Casos Límite](#️-validaciones-y-casos-límite)

---

## 📖 ¿Qué es SimuladorSO?

**SimuladorSO** es una herramienta académica interactiva desarrollada en **Java 21** diseñada para modelar y comparar experimentalmente dos de las responsabilidades centrales de un núcleo de Sistema Operativo moderno:

1. **Gestión de Procesos:** La asignación eficiente del procesador (CPU Scheduling) mediante políticas no apropiativas y apropiativas.
2. **Gestión de Memoria Virtual:** La administración y reemplazo de páginas en memoria principal (Paging & Page Replacement).

El simulador permite introducir datos personalizados o cargar **casos de estudio clásicos de la literatura de Sistemas Operativos** (Silberschatz, Galvin & Gagne; Tanenbaum), generando tablas comparativas, métricas formales de rendimiento y diagramas de Gantt visuales.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** [Java 21 LTS](https://openjdk.org/projects/jdk/21/)
- **Paradigma:** Programación Orientada a Objetos (POO) estricta y modular.
- **Patrones de Diseño:**
  - **Strategy Pattern:** Para desacoplar los algoritmos (`Planificador`, `AlgoritmoReemplazo`) de las vistas y controladores.
  - **Inmutabilidad y Value Objects:** Modelos de datos (`Proceso`, `PasoReemplazo`) que protegen la integridad de la simulación.
  - **Defensive Copying:** Envoltorios `Collections.unmodifiableList()` para blindar colecciones frente a efectos colaterales externos.
- **Dependencias:** **0 dependencias externas** (únicamente la biblioteca estándar de Java `java.base`).

---

## ⚙️ Arquitectura y Principios de Diseño

```
┌────────────────────────────────────────────────────────┐
│                        Main                            │
│           (Menú de Enrutamiento Principal)             │
└───────────────┬────────────────────────┬───────────────┘
                │                        │
       ┌────────▼────────┐      ┌────────▼────────┐
       │  SimuladorCPU   │      │ SimuladorMemoria│  (Capa de Presentación / CLI)
       └────────┬────────┘      └────────┬────────┘
                │                        │
     ┌──────────▼──────────┐  ┌──────────▼──────────┐
     │  <<Planificador>>   │  │<<AlgoritmoReemplazo>│ (Capa Lógica / Strategy)
     └─────▲────────────▲──┘  └──────────▲──────────┘
           │            │                │
     ┌─────┴────┐ ┌─────┴──────┐   ┌─────┴──────┐
     │   FCFS   │ │ RoundRobin │   │    FIFO    │
     └──────────┘ └────────────┘   └────────────┘
```

- **Separación de Responsabilidades (SRP):** La lógica de cálculo nunca interactúa con la consola; los algoritmos reciben colecciones de datos puros y retornan estructuras de resultados consolidadas.
- **Determinismo Absoluto:** Reglas de desempate consistentes (orden de inserción garantizado para procesos que arriban simultáneamente).
- **Protección contra Desbordamiento Numérico:** Los acumuladores de métricas usan `long` para evitar overflow al procesar ráfagas grandes.

---

## 🧠 Módulos y Algoritmos

### 1. Planificación de CPU (FCFS y Round Robin)

Permite simular la ejecución de una ráfaga de procesos $P_i$ definidos por $(ID, A_i, B_i)$, donde:
- $A_i$: Tiempo de Llegada (Arrival Time, $A_i \ge 0$).
- $B_i$: Tiempo de Ráfaga / Ejecución (Burst Time, $B_i > 0$).

#### Métricas Calculadas:
- **Tiempo de Finalización ($C_i$):** Instante en el que el proceso concluye su ráfaga.
- **Tiempo de Retorno ($T_i$ / Turnaround Time):**  
  $$T_i = C_i - A_i$$
- **Tiempo de Espera ($W_i$ / Waiting Time):**  
  $$W_i = T_i - B_i$$
- **Promedios del Sistema:** $\bar{T} = \frac{1}{n} \sum T_i$, $\bar{W} = \frac{1}{n} \sum W_i$.

#### Algoritmos Implementados:
1. **First-Come, First-Served (FCFS):**
   - Política **no apropiativa (cooperativa)**.
   - Atiende los procesos estrictamente en orden de arribo.
   - Detecta y grafica periodos de CPU ociosa cuando el procesador queda desocupado esperando nuevos procesos.
2. **Round Robin (RR):**
   - Política **apropiativa (preemptive)** con quantum $q > 0$ configurable.
   - Utiliza una cola FIFO de listos.
   - **Regla de arribo simultáneo:** Si un proceso arriba a la cola en el mismo instante en que expira el quantum del proceso en ejecución, el nuevo proceso se incorpora a la cola **antes** de que el proceso desalojado regrese al final.

---

### 2. Reemplazo de Páginas de Memoria (FIFO)

Modela el subsistema de memoria virtual paginada frente a una cadena de referencias numéricas y un número fijado de marcos de página físicos.

#### Mecánica de First-In, First-Out (FIFO):
- **Acierto (Hit):** La página referenciada ya reside en alguno de los marcos disponibles. En FIFO puro, **la antigüedad de la página no se altera tras un acierto**.
- **Fallo de Página (Page Fault):** La página solicitada no está cargada.
  - *Si hay marcos libres:* Se carga en el primer marco disponible.
  - *Si todos los marcos están ocupados:* Se expulsa la página que lleva más tiempo en memoria principal (el frente de la cola de antigüedad).
- **Verificación de Coherencia:** El simulador valida formalmente que:
  $$\text{Total de Fallos} + \text{Total de Aciertos} = \text{Total de Referencias}$$

---

## 📂 Estructura del Proyecto

```text
C:\SimuladorSO\
├── bin\                                  # Binarios compilados (.class)
├── src\
│   ├── Main.java                         # Menú interactivo principal
│   ├── cpu\
│   │   ├── Proceso.java                  # Modelo inmutable de proceso
│   │   ├── Planificador.java             # Interfaz común de planificación
│   │   ├── FCFS.java                     # Algoritmo First-Come, First-Served
│   │   ├── RoundRobin.java               # Algoritmo Round Robin con quantum
│   │   ├── ResultadoPlanificacion.java   # Contenedor de métricas y segmentos Gantt
│   │   └── SimuladorCPU.java             # Controlador y formateador de tablas CPU
│   ├── memoria\
│   │   ├── AlgoritmoReemplazo.java       # Interfaz común de reemplazo de memoria
│   │   ├── FIFO.java                     # Algoritmo FIFO con preservación de orden
│   │   ├── PasoReemplazo.java            # Snapshot inmutable de cada referencia
│   │   ├── ResultadoReemplazo.java       # Resumen de fallos, aciertos y tasas
│   │   └── SimuladorMemoria.java         # Controlador y matriz de marcos en consola
│   └── util\
│       └── ConsolaUtil.java              # Validación centralizada de entradas por teclado
└── README.md                             # Documentación completa del proyecto
```

---

## 🚀 Guía de Instalación y Ejecución

### Prerrequisitos
- **Java Development Kit (JDK) 21 o posterior** instalado.
- Verificar la instalación en tu terminal:
  ```powershell
  javac -version
  java -version
  ```

### Compilación

Desde **PowerShell** o **Símbolo del Sistema (CMD)** en la raíz del proyecto:

```powershell
cd C:\SimuladorSO
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
```

*(En Linux/macOS equivalente: `javac -encoding UTF-8 -d bin $(find src -name "*.java")`)*

### Ejecución

Una vez compilado, ejecuta la clase principal:

```powershell
java -cp bin Main
```

### Abrir en IntelliJ IDEA
El proyecto ya cuenta con el archivo de configuración `.iml` y la estructura `.idea`:
1. Abre IntelliJ IDEA.
2. Selecciona **File -> Open...** y elige la carpeta `C:\SimuladorSO`.
3. Asegúrate de tener seleccionado el SDK Java 21 en **File -> Project Structure -> Project SDK**.
4. Haz clic derecho sobre `src/Main.java` y selecciona **Run 'Main'**.

---

## 💻 Guía de Uso y Demostraciones

Al iniciar, serás recibido por el menú principal:

```text
=================================================================
          SIMULADOR DE SISTEMAS OPERATIVOS (SimuladorSO)        
        Planificación de CPU | Reemplazo de Páginas             
=================================================================

----------------------- MENÚ PRINCIPAL -------------------------
1. Planificación de CPU (FCFS y Round Robin)
2. Reemplazo de Páginas en Memoria (FIFO)
3. Salir del programa
----------------------------------------------------------------
Seleccione una opción (1-3): 
```

### Módulo de CPU

Puedes ingresar procesos manualmente o seleccionar casos predefinidos:
- **Opción 1 (FCFS):** Ejecuta y calcula la métrica en orden de llegada.
- **Opción 2 (Round Robin):** Solicita el valor del quantum y desglosa los turnos.
- **Opción 3 (Comparar ambos algoritmos):** Ejecuta FCFS y RR con el mismo conjunto de procesos y genera una tabla comparativa directa de tiempos de espera y retorno.

#### Salida del Diagrama de Gantt:
```text
Diagrama de Gantt (Cronología de ejecución):
| P1       | P2       | P3       | P1       | P1       | P1       | P1       | P1       |
0          4          7         10         14         18         22         26         30
```

### Módulo de Memoria Virtual

Muestra la evolución temporal de cada marco de página, indicando si hubo acierto, carga inicial o expulsión de página antigua:

```text
=========================================================================================
  RESULTADO DE LA SIMULACIÓN: First-In, First-Out (FIFO)
  Marcos disponibles: 3 | Total referencias: 20
=========================================================================================
Paso  | Pág    | M[0]  | M[1]  | M[2]  | Evento     | Reemplazo / Detalle 
----------------------------------------------------------------------------
1     | 7      | 7     | -     | -     | FALLO      | Carga en marco libre
2     | 0      | 7     | 0     | -     | FALLO      | Carga en marco libre
3     | 1      | 7     | 0     | 1     | FALLO      | Carga en marco libre
4     | 2      | 2     | 0     | 1     | FALLO      | Expulsa pág. 7      
5     | 0      | 2     | 0     | 1     | ACIERTO    | Página ya en memoria
6     | 3      | 2     | 3     | 1     | FALLO      | Expulsa pág. 0      
...
----------------------------------------------------------------------------
--- Estadísticas Finales ---
Total de Referencias : 20
Total de Fallos      : 15 (75.00%)
Total de Aciertos    : 5 (25.00%)
Verificación         : Fallos + Aciertos = 20 (Coherente)
=========================================================================================
```

---

## 🛡️ Validaciones y Casos Límite

El simulador cuenta con tolerancia total a fallos en la interacción de consola y a nivel de dominio:

| Caso Límite | Comportamiento del Simulador |
| :--- | :--- |
| **Entrada vacía o letras en números** | `ConsolaUtil` captura el error, informa con claridad y solicita el dato sin caerse. |
| **Identificadores duplicados** | Se rechazan identificadores repetidos en una misma lista de procesos. |
| **Tiempos de llegada negativos** | Validación en constructor de `Proceso` ($A_i \ge 0$). |
| **Ráfagas o quantums $\le 0$** | Validación estricta que exige valores $> 0$. |
| **Periodos de CPU ociosa** | Si ningún proceso está listo, se inserta un bloque `[OCIOSO]` en el diagrama de Gantt sin bloquear el avance temporal. |
| **Marcos de memoria $\le 0$** | Validación de capacidad física ($> 0$). |
| **Referencias repetidas** | Detecta múltiples accesos consecutivos o alternados como aciertos sin alterar el orden FIFO. |


