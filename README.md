# Mapa-Utez
---
<img width="535" height="535" alt="unnamed" src="https://github.com/user-attachments/assets/b3cd4456-35fb-4d1a-abcd-3cc99ce34d24" />

## Equipo de Desarrollo

- Barrera Villareal Carlos Israel | [[Backend, Frontend]] | @CarlosIs002 |
- Jaimes Pereira Emmanuel | [Backend, Frontend] | @lolxdr23 |
- Rosales Hernandez Edgar | [Backend, Retrofit] | @Edgar-alt423 |

---

## Descripción

Esta aplicación, **Mapa-Utez**, tiene como objetivo principal guiar a los estudiantes y visitantes a través de las instalaciones de la universidad. El problema que resuelve es la dificultad de encontrar aulas, oficinas y otros puntos de interés dentro del campus, especialmente para los alumnos de nuevo ingreso.

Para lograr esto, la aplicación utiliza el **sensor GPS** del dispositivo móvil para obtener la ubicación del usuario en tiempo real y mostrarla en un mapa interactivo del campus, facilitando la navegación y la orientación. Además de esto, se podría incorporar una funcionalidad específica que permita a los usuarios reportar problemas o incidencias relacionados con las aulas (como fallos en el equipamiento, mobiliario dañado o la necesidad de mantenimiento), garantizando que estos reportes sean recibidos y gestionados por el equipo de soporte o mantenimiento de manera eficiente y priorizada

## Stack Tecnológico y Características

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario:** Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Conectividad (API REST):** Retrofit.
    * **GET:** Se obtienen los datos de los reportes o "lugares" guardados (id, título, salón, descripción, imagen, latitud y longitud) para mostrarlos en la galería de la aplicación.
    * **POST:** Se envían los datos de un nuevo reporte (título, salón, descripción, coordenadas y la imagen) al servidor para ser creado en la base de datos.
    * **UPDATE:** Permite la actualización de la información existente de un reporte o lugar específico.
    * **DELETE:** Permite borrar un reporte o lugar de la base de datos a través del servidor.
* **Sensor Integrado:** GPS
    * *Uso:* El GPS se utiliza para obtener las coordenadas geográficas (latitud y longitud) del usuario, que se asocian a cada reporte o "lugar" creado en la aplicación, permitiendo así registrar la ubicación exacta de cada uno.
---

## Capturas de Pantalla
|| Pantalla de Inicio | Operación CRUD | Uso del Sensor |
| :---: | :---: | :---: |
| ![Inicio](https://github.com/user-attachments/assets/936094ab-d176-4408-a86f-893ed7874212) | 
  <img src="https://github.com/user-attachments/assets/b51a0829-6fdc-490f-8ada-da67efacfd18" width="150" alt="CREATE">
  <img src="https://github.com/user-attachments/assets/0c9a6e7e-d51c-4adf-b08a-a43e344ee4cd" width="150" alt="READ">
  <img src="https://github.com/user-attachments/assets/4b449be4-0cf5-41c3-af14-054801e3da3f" width="150" alt="UPDATE">
  <img src="https://github.com/user-attachments/assets/0c9a6e7e-d51c-4adf-b08a-a43e344ee4cd" width="150" alt="DELETE">
| ![Sensor] ![1000112904](https://github.com/user-attachments/assets/92683267-3762-41ca-b131-d8caf3a8f937) |
## Instalación y Releases

El ejecutable firmado (.apk) se encuentra disponible en la sección de **Releases** de este repositorio.
https://github.com/CarlosIs002/MAPA-DE-LA-UTEZ/releases
