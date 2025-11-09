Sistema de Biblioteca UNAB

Aplicación en Java con POO con Swing que permite gestionar usuarios, libros y préstamos en una biblioteca.

Funciones principales:

-Registrar y editar usuarios (docentes y estudiantes)

-Registrar y editar libros

-Realizar préstamos y devoluciones

-Calcular multas por retraso

-Guardar y cargar datos desde archivos de texto


Ejecución:

Abrir en ide

Ejecutar la clase Main

Se abre el sistema biblioteca UNAB

Estructura:

modelo/: clases Usuario, Docente, Estudiante, Libro, Prestamo

controlador/: clase Biblioteca con la lógica del sistema

util/: clases GestorArchivos y Validador

gui/: interfaz gráfica Swing

datos/: archivos de texto con la información guardada

Reglas:

RUN válido con dígito verificador

Género M o F

ISBN único

Un préstamo activo por usuario

Multa de $1000 por día de atraso

El sistema no usa base de datos.
Toda la información se guarda en archivos de texto plano dentro de la carpeta datos.
