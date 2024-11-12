Requisitos
    - version Java >= 17
    - Docker
Ejecucion

1- mvn clean install -DskipTests (dentro de la carpeta estudiantes e incripciones )
2- Docker compose build (en la carpeta base)
3- Docker compose up (en la carpeta base)
4- Esperar a que comiencen a correcte los tres contenedores

(Dentro de la carpeta PostMan estan los archivos para importar en PostMan con todas las requests)


Microservicio Estudiantes- Endpoints (http://localhost:4004)
GET - http://localhost:4004/estudiante
POST - http://localhost:4004/estudiante 
    body:   --ejemplo--
        {
            "numeroDeDocumento": 1223,
            "nombre": "Santiago",
            "apellido": "Castano",
            "edad": 20,
            "genero": "masculino",
            "ciudadResidencia": "Tandil"
        }
        (el campo genero solo puede ser masculino o femenino)
PUT - http://localhost:4004/estudiante
    body:   --ejemplo--
        {
            "libretaUniversitaria": ...,        (libretaUniversitaria con la que se haya ingresado)
            "numeroDeDocumento": 1223,
            "nombre": "Santiago",
            "apellido": "Castano",
            "edad": 30,
            "genero": "masculino",
            "ciudadResidencia": "Tandil"
        }
DELETE - http://localhost:4004/estudiante?libreta=
    query params: libreta = ...     (libretaUniversitaria)
GET - http://localhost:4004/estudiante/libreta?libreta=
    query params: libreta = ...     (libretaUniversitaria)
GET - http://localhost:4004/estudiante/documento?documento=
    query params: documento = ... 
GET - http://localhost:4004/estudiante/orden?orden=
    query params: orden = ...   
    ("NOMBRE","APELLIDO","EDAD","GENERO","CIUDAD_RESIDENCIA", "NUMERO_DE_DOCUMENTO")
GET - http://localhost:4004/estudiante/masculinos
GET - http://localhost:4004/estudiante/femeninos
GET - http://localhost:4004/estudiante/ciudad_carrera?ciudad=&carrera=
    query params: ciudad = ... & carrera = ... (idCarrera)

Microservicio Inscripciones- Endpoints (http://localhost:4005)
POST - http://localhost:4005/inscribir?estudianteId=&carreraId=
    query params: estudianteId = ... & carreraID = ...
DELETE - http://localhost:4005/eliminar?estudianteId=&carreraId=
    query params: estudianteId = ... & carreraID = ...
PUT - http://localhost:4005/actualizar
    body:  --ejemplo--
    {
        "estudiante": {
            "libretaUniversitaria": 1,
            "numeroDeDocumento": 0,
            "nombre": "Santiago",
            "apellido": "Castano",
            "edad": 1,
            "genero": "masculino",
            "ciudadResidencia": "tandil"
        },
        "carrera": {
            "carreraId": 2,
            "nombreCarrera": "Ingeniera de Sistemas",
            "fechaCreacion": "2023-01-15T08:00:00"
        },
        "graduado": true,
        "fecha_inscripcion": "2024-11-12T04:08:14"
    }
GET - http://localhost:4005/{libretaUniversitaria}/obtener/{carreraId}
GET - http://localhost:4005/buscar?filtro=&orden=
    query params: filtro = ... & orden = ...
    (filtro es por idCarrera)
    (orden puede ser por "GRADUADO" o "FECHA_INSCRIPCION")
GET - http://localhost:4005/reportes

Carreras (entradas presentes en la base de datos)
+-----------+-------------------------+---------------------+
| carreraId | nombreCarrera           | fechaCreacion       |
+-----------+-------------------------+---------------------+
|         1 | Psicología              | 2023-09-05 16:10:00 |
|         2 | Ingeniería de Sistemas  | 2023-01-15 08:00:00 |
|         3 | Medicina                | 2023-03-10 14:30:00 |
|         4 | Derecho                 | 2023-05-22 09:45:00 |
|         5 | Arquitectura            | 2023-07-18 11:20:00 |
+-----------+-------------------------+---------------------+
