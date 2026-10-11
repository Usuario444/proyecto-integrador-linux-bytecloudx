# 🐧 Proyecto Integrador - Sistemas Operativos
## Implementación y Administración de una Plataforma GNU/Linux — ByteCloudX

![Java](https://img.shields.io/badge/Java-21%2B-orange?logo=openjdk)
![Ubuntu](https://img.shields.io/badge/Ubuntu-Server%20LTS-E95420?logo=ubuntu)
![Git](https://img.shields.io/badge/Git-GitHub-black?logo=github)
![Estado](https://img.shields.io/badge/Progreso-PC2%20completado-brightgreen)

## 📑 Tabla de contenidos
- [Descripción](#-descripción)
- [Información del curso](#-información-del-curso)
- [Organización seleccionada](#-organización-seleccionada)
- [Estado del proyecto](#-estado-del-proyecto)
- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Estructura del repositorio](#-estructura-del-repositorio)
- [Simuladores Java](#-simuladores-java)
- [Scripts Bash](#-scripts-bash-si-aplica)
- [Evidencias](#-evidencias)
- [Informes](#-informes)

## 📝 Descripción
ByteCloudX es una empresa tecnológica ficticia dedicada a infraestructura
cloud, hosting, seguridad informática y monitoreo de sistemas para otras
empresas. Este proyecto implementa y administra una plataforma GNU/Linux
que soporta su operación, junto con simuladores en Java de algoritmos
internos del sistema operativo.

## 🎓 Información del curso
- **Curso:** Sistemas Operativos (100000I56N)
- **Ciclo:** 2026-II
- **Sección:**
- **Docente:**
- **Integrantes:**
  - 
  - 

## 🏢 Organización seleccionada
- **Nombre:** ByteCloudX
- **Sector / actividad:** infraestructura cloud, hosting, seguridad
  informática y monitoreo de sistemas para otras empresas

**Áreas:**
1. Infraestructura Cloud
2. Seguridad Informática
3. Monitoreo (SOC)
4. Administración

**Grupos:**
- `infraestructura`
- `seguridad` (agrupa Seguridad Informática y Monitoreo)
- `administracion`

**Usuarios:**
| Usuario | Área | Grupo |
|---|---|---|
| xxxxx | Infraestructura Cloud (jefe) | infraestructura |
| xxxxx | Infraestructura Cloud (técnico) | infraestructura |
| xxx | Seguridad Informática | seguridad |
| xxxx | Monitoreo (SOC) | seguridad |
| xxxxxx | Administración (gerente) | administracion |
| xxxxxx| Administración (asistente) | administracion |

## ✅ Estado del proyecto

- [x] **PC1 (Semana 5)** — Fundamentos, Linux y procesos
  - VM Ubuntu Server configurada con hostname de ByteCloudX
  - Caso organizacional definido (4 áreas, arquitectura general)
  - Comandos básicos de navegación documentados
  - Simulador Java de FCFS y Round Robin funcionando

- [x] **PC2 (Semana 10)** — Memoria, usuarios, archivos y permisos
  - Creados 6 usuarios y 3 grupos (infraestructura, seguridad, administracion)
  - Estructura de directorios por área organizacional
  - Permisos configurados con chmod/chown/chgrp según mínimo privilegio
  - Pruebas de acceso permitido y denegado entre grupos
  - Monitoreo de memoria con vmstat y pruebas de swap
  - Simulador Java de reemplazo de páginas (FIFO)

- [ ] **PC3 (Semana 15)** — E/S, almacenamiento y disco
  - Pendiente: disco virtual adicional y sistema de archivos
  - Pendiente: backup y restauración de logs de seguridad
  - Pendiente: simulador Java de planificación de disco

- [ ] **Semanas 16–17** — Seguridad y cierre
  - Pendiente: revisión final de permisos y medidas de seguridad
  - Pendiente: pruebas finales y corrección de observaciones

- [ ] **Semana 18** — Entrega final
  - Pendiente: informe final, VM funcional y sustentación

## ⚙️ Tecnologías utilizadas
- Ubuntu Server LTS (VMware)
- Java 21 LTS
- Git / GitHub
- Bash

## 📁 Estructura del repositorio
- `java/` — código del simulador integrado `SimuladorSO` (CPU y memoria)
- `scripts/` — scripts Bash usados para automatizar tareas
- `docs/` — informes de cada entrega y diagrama de arquitectura
- `evidencias/` — capturas de pantalla organizadas por entrega
- `datos/` — archivos TXT/CSV que generan o leen los simuladores

## 💻 Simuladores Java

### Simulador de Sistemas Operativos (SimuladorSO)
Módulo interactivo por consola en Java 21 que integra algoritmos de planificación y administración de memoria virtual.

- **Ubicación:** [`java/SimuladorSO/`](java/SimuladorSO/) *(documentación detallada en [`java/SimuladorSO/README.md`](java/SimuladorSO/README.md))*
- **Módulos implementados:**
  - **Planificación de CPU (PC1):** FCFS (First-Come, First-Served) y Round Robin (con quantum configurable, cálculo de tiempos de espera/retorno y diagrama de Gantt).
  - **Reemplazo de Páginas en Memoria (PC2):** FIFO (First-In, First-Out con matriz de marcos paso a paso, detección de fallos/aciertos y tasas de rendimiento).
  - **Planificación de Disco (PC3 - Pendiente):** Módulo proyectado para la Semana 15.

#### Cómo compilar y ejecutar

**En Linux / Bash:**
```bash
cd java/SimuladorSO
mkdir -p bin
javac -encoding UTF-8 -d bin src/Main.java src/cpu/*.java src/memoria/*.java src/util/*.java
java -cp bin Main
```

**En Windows (PowerShell):**
```powershell
cd java\SimuladorSO
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
java -cp bin Main
```

## 📜 Scripts Bash (si aplica)
- `crear_usuarios.sh` — descripción breve
- `configurar_permisos.sh` — descripción breve

## 🖼️ Evidencias
Las capturas de cada entrega están en `evidencias/PC1`, `evidencias/PC2`, etc.

## 📄 Informes
Los informes de cada entrega están en `docs/`.
