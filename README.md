# Proyecto de Microservicios - Estudiantes e Inscripciones

## Requisitos

- **Java**: Versión 17 o superior
- **Docker**

## Ejecución

1. Ejecutar `mvn clean install -DskipTests` en las carpetas `estudiantes` e `inscripciones`.
2. Ejecutar `docker compose build` en la carpeta base.
3. Ejecutar `docker compose up` en la carpeta base.
4. Esperar a que los tres contenedores se ejecuten correctamente.

> **Nota**: Los archivos necesarios para realizar pruebas en Postman están en la carpeta `PostMan`.

---

## Microservicio Estudiantes

**Base URL:** `http://localhost:4004`

### Endpoints

- **GET** `/estudiante` - Obtener todos los estudiantes
- **POST** `/estudiante` - Crear un nuevo estudiante  
    **Body de ejemplo:**
    ```json
    {
        "numeroDeDocumento": 1223,
        "nombre": "Santiago",
        "apellido": "Castano",
        "edad": 20,
        "genero": "masculino",
        "ciudadResidencia": "Tandil"
    }
    ```
    > El campo `genero` solo acepta los valores `"masculino"` o `"femenino"`.

- **PUT** `/estudiante` - Actualizar un estudiante existente  
    **Body de ejemplo:**
    ```json
    {
        "libretaUniversitaria": ...,
        "numeroDeDocumento": 1223,
        "nombre": "Santiago",
        "apellido": "Castano",
        "edad": 30,
        "genero": "masculino",
        "ciudadResidencia": "Tandil"
    }
    ```

- **DELETE** `/estudiante?libreta=` - Eliminar un estudiante  
    **Query Params:** `libreta`

- **GET** `/estudiante/libreta?libreta=` - Obtener estudiante por libreta  
    **Query Params:** `libreta`

- **GET** `/estudiante/documento?documento=` - Obtener estudiante por documento  
    **Query Params:** `documento`

- **GET** `/estudiante/orden?orden=` - Obtener estudiantes ordenados por un campo  
    **Query Params:** `orden`  
    Valores permitidos: `"NOMBRE", "APELLIDO", "EDAD", "GENERO", "CIUDAD_RESIDENCIA", "NUMERO_DE_DOCUMENTO"`

- **GET** `/estudiante/masculinos` - Listar estudiantes masculinos
- **GET** `/estudiante/femeninos` - Listar estudiantes femeninos
- **GET** `/estudiante/ciudad_carrera?ciudad=&carrera=` - Listar estudiantes por ciudad y carrera  
    **Query Params:** `ciudad`, `carrera` (idCarrera)

---

## Microservicio Inscripciones

**Base URL:** `http://localhost:4005`

### Endpoints

- **POST** `/inscribir?estudianteId=&carreraId=` - Inscribir estudiante en una carrera  
    **Query Params:** `estudianteId`, `carreraID`

- **DELETE** `/eliminar?estudianteId=&carreraId=` - Eliminar inscripción de estudiante  
    **Query Params:** `estudianteId`, `carreraID`

- **PUT** `/actualizar` - Actualizar inscripción  
    **Body de ejemplo:**
    ```json
    {
        "estudiante": {
            "libretaUniversitaria": 1,
            "numeroDeDocumento": 0,
            "nombre": "Santiago",
            "apellido": "Castano",
            "edad": 1,
            "genero": "masculino",
            "ciudadResidencia": "Tandil"
        },
        "carrera": {
            "carreraId": 2,
            "nombreCarrera": "Ingeniería de Sistemas",
            "fechaCreacion": "2023-01-15T08:00:00"
        },
        "graduado": true,
        "fecha_inscripcion": "2024-11-12T04:08:14"
    }
    ```

- **GET** `/{libretaUniversitaria}/obtener/{carreraId}` - Obtener inscripción por libreta y carreraId
- **GET** `/buscar?filtro=&orden=` - Buscar inscripciones  
    **Query Params:** `filtro` (idCarrera), `orden` (`"GRADUADO"` o `"FECHA_INSCRIPCION"`)

- **GET** `/reportes` - Obtener reportes de inscripciones

---

## Carreras Disponibles

| carreraId | Nombre de Carrera        | Fecha de Creación       |
|-----------|---------------------------|--------------------------|
| 1         | Psicología                | 2023-09-05 16:10:00     |
| 2         | Ingeniería de Sistemas    | 2023-01-15 08:00:00     |
| 3         | Medicina                  | 2023-03-10 14:30:00     |
| 4         | Derecho                   | 2023-05-22 09:45:00     |
| 5         | Arquitectura              | 2023-07-18 11:20:00     |
