<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=rect&color=0:6c5ce7,100:3f51b5&height=180&section=header&text=iMirly&fontSize=70&fontColor=fff&fontAlignY=40&desc=Plataforma%20de%20Contrataci%C3%B3n%20de%20Servicios%20Profesionales&descAlignY=70&descSize=18" width="100%"/>
</div>

<br>

<div align="center">
  <img src="https://img.shields.io/badge/Status-MVP-FFA500?style=for-the-badge" alt="Status" />
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Backend" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="DB" />
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-8A2BE2?style=for-the-badge" alt="Architecture" />
</div>

<br>

<div align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=22&duration=3000&pause=1000&color=6c5ce7&center=true&vCenter=true&width=600&lines=Kotlin+%2B+Jetpack+Compose;Java+21+%2B+Spring+Boot+3;Arquitectura+Hexagonal+%2F+Clean;Chat+en+tiempo+real+con+WebSockets;Sistema+de+presupuestos+y+pagos;PostgreSQL+%2B+JPA+%2B+Hibernate"/>
</div>

# 🚀 Sobre el Proyecto

**iMirly** es un **mercado de servicios profesionales** que conecta clientes con proveedores para la contratación de servicios de forma segura y centralizada. La plataforma permite la comunicación en tiempo real, negociación de presupuestos, pagos retenidos y valoraciones bidireccionales, todo dentro de una aplicación Android nativa acompañada por un backend robusto.

✨ Características principales:

- 💬 **Chat en tiempo real** entre clientes y profesionales
- 🤝 **Sistema inteligente de presupuestos** gestionados como burbujas dinámicas dentro del flujo conversacional
- 💳 **Monedero virtual integrado** con retención segura del saldo y liberación de pagos tras aprobación mutua
- 🛠️ **Gestor dinámico de tareas** con estados de finalización y aprobación
- ⭐ **Sistema de valoración global** con reseñas bidireccionales de 1 a 5 estrellas que nutren un algoritmo de confianza
- 🏗️ **Arquitectura hexagonal / Clean Architecture** para escalabilidad y mantenimiento

# 🧠 Contexto del Proyecto

Este proyecto surge como **Trabajo Fin de Ciclo (TFC)** del ciclo de **DAM (Desarrollo de Aplicaciones Multiplataforma)** en **NDT NewDigitalTalent · Granada**.

El objetivo era crear una **plataforma completa de contratación de servicios** que permitiera:

- Conectar clientes con proveedores de servicios profesionales de forma segura
- Gestionar todo el ciclo de vida del servicio dentro de la app (chat → presupuesto → pago → valoración)
- Aplicar arquitecturas modernas y escalables tanto en frontend como en backend
- Validar la experiencia de usuario con stakeholders reales

> Este repositorio contiene tanto el **frontend Android** como el **backend Spring Boot** en una estructura monorepo.

# 🖼️ Diseño del Proyecto

El diseño visual se adapta a una experiencia **mobile-first** con identidad de marca consistente:

- 🎨 **Paleta de colores** en OKLCH (morado `#6C5CE7`, verde éxito, rojo destructivo)
- 📐 **UI 100% declarativa** con Jetpack Compose y Material Design 3
- 📱 **Arquitectura de navegación** fluida con Compose Navigation
- ✨ **Gradientes y sombras** consistentes con la identidad de marca iMirly

> El objetivo fue mantener fidelidad visual al 100% entre la app nativa y la web de presentación.

# 🛠️ Tecnologías

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java_21-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
</p>

**Frontend:** Kotlin + Jetpack Compose (UI declarativa 100%), Compose Navigation, Retrofit + OkHttp, ViewModels y Corrutinas  
**Backend:** Java 21 + Spring Boot 3, Arquitectura Hexagonal / Clean Architecture, Spring Data JPA + Hibernate, Spring Security con JWT  
**Base de datos:** PostgreSQL (con H2 para tests)  
**Build:** Gradle 8.13 (frontend) + Maven (backend)  
**Deploy:** Docker Compose para PostgreSQL en desarrollo

# 🧩 Arquitectura del Proyecto

```bash
iMirly/
│
├── frontend/                  # Aplicación Android nativa
│   ├── app/
│   │   ├── src/main/java/com/imirly/app/
│   │   │   ├── MainActivity.kt
│   │   │   └── ...
│   │   ├── src/main/res/      # Recursos Compose
│   │   └── build.gradle.kts   # Configuración Gradle
│   ├── build.gradle.kts         # Build root
│   ├── settings.gradle.kts
│   └── gradle/libs.versions.toml
│
└── backend/                   # API REST Spring Boot
    └── src/main/java/tfg/imirly/
        ├── auth/              # Módulo de usuarios, registro y monedero
        ├── catalog/           # Gestión de anuncios, subcategorías y explorador
        ├── contracting/       # Ciclo de vida del contrato y pagos retenidos
        └── messages/          # Chat dinámico, envío de ofertas y valoraciones
```

> Cada módulo del backend se rige por **Arquitectura Hexagonal**:
> - **Dominio (`domain`):** Reglas puras del negocio, sin librerías de terceros. Puertos definidos mediante Interfaces.
> - **Aplicación (`application`):** Casos de uso concretos que ejecutan las directrices del negocio.
> - **Infraestructura (`infrastructure`):** Adaptadores reales — controladores REST (`input`) y repositorios JPA (`output`).

# 📱 Funcionalidades

### 💬 Chat en Tiempo Real
- Comunicación fluida y unificada entre clientes y profesionales
- WebSockets para mensajería instantánea
- Burbujas dinámicas para presupuestos integradas en el flujo conversacional

### 🤝 Sistema de Presupuestos
- Ofertas, rechazos y acuerdos gestionados como burbujas dinámicas dentro del chat
- Estados del presupuesto: `pendiente` → `aceptado` → `aprobado` → `finalizado`
- Simulación de envío/recepción entre cliente y proveedor

### 💳 Monedero Virtual
- Retención segura del saldo al aceptar un presupuesto
- El profesional recibe el importe exacto (restando comisiones) cuando el servicio se completa y se aprueba mutuamente
- Recargas y historial de transacciones

### 🛠️ Gestor de Tareas
- Proveedores marcan el trabajo como "Finalizado"
- Clientes lo "Aprueban y liberan el pago" o solicitan revisiones
- Seguimiento completo del ciclo de vida del servicio

### ⭐ Sistema de Valoración
- Reseñas bidireccionales de 1 a 5 estrellas al finalizar cada servicio
- Algoritmo de confianza reflejado en los perfiles de usuario

### 📂 Catálogo de Servicios
- Navegación jerárquica: Categoría → Subcategoría → Lista de anuncios
- Gestión de anuncios con imágenes, descripción, precio y ubicación

# 🚀 Cómo empezar

### Requisitos
- **Android Studio** (Koala o superior recomendado)
- **Java JDK 21** (para backend)
- **PostgreSQL** (o Docker para levantar la base de datos)
- **Gradle 8.13** (wrapper incluido en frontend)

### 1. Clonar el repositorio
```bash
git clone https://github.com/iMirly/iMirly.git
cd iMirly
```

### 2. Levantar la base de datos (opcional con Docker)
```bash
docker-compose up -d db
```
> Esto creará un contenedor PostgreSQL con la configuración de desarrollo. Asegúrate de que las credenciales en `backend/src/main/resources/application.properties` coincidan:
> ```properties
> spring.datasource.url=jdbc:postgresql://localhost:5432/imirly_db
> spring.datasource.username=user_imirly
> spring.datasource.password=password_imirly
> ```

### 3. Iniciar el Backend (Spring Boot)
```bash
cd backend
# Compilar y ejecutar
./mvnw spring-boot:run
# O ejecutar la clase principal: TfgApplication.java
```
> Al arrancar por primera vez, el `DatabaseSeeder` inyectará automáticamente las categorías de trabajo (Reformas, Educación, Estética, Salud).

### 4. Compilar el Frontend (Android)
```bash
cd frontend
./gradlew assembleDebug
```
> O abre la carpeta `frontend/` en **Android Studio**, haz *Sync Project with Gradle Files* y ejecuta en un emulador o dispositivo real.

---

## 📚 Lo que aprendí con este proyecto

- **Arquitectura Hexagonal / Clean Architecture:** Separación total de la lógica de dominio de las librerías y bases de datos mediante Puertos y Adaptadores
- **Jetpack Compose 100% declarativo:** Construcción de UI moderna sin XML, con estado reactivo y animaciones fluidas
- **Spring Boot 3 + Java 21:** API REST robusta con seguridad JWT, validación y persistencia JPA
- **Comunicación en tiempo real:** Implementación de WebSockets para chat dinámico con presupuestos integrados
- **Sistema de pagos retenidos:** Lógica de negocio compleja para monedero virtual y liberación condicional de fondos
- **Monorepo con Gradle + Maven:** Gestión de dos ecosistemas de build en un solo repositorio

# 🔗 Repositorios Relacionados

<div align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <h3>iMirly Web MVP</h3>
        <p>Demo web estática con Vite + React 19</p>
        <a href="https://github.com/iMirly/iMirlyWeb-MVP">
          <img src="https://img.shields.io/badge/Ver_repositorio-6c5ce7?style=for-the-badge&logo=github&logoColor=white"/>
        </a>
      </td>
      <td align="center" width="33%">
        <h3>iMirlyAppBackend</h3>
        <p>API REST + Admin Panel (versión extendida)</p>
        <a href="https://github.com/iMirly/iMirlyAppBackend">
          <img src="https://img.shields.io/badge/Ver_repositorio-3f51b5?style=for-the-badge&logo=github&logoColor=white"/>
        </a>
      </td>
      <td align="center" width="33%">
        <h3>iMirlyPresentacion</h3>
        <p>Sitio web de presentación del TFC</p>
        <a href="https://github.com/iMirly/iMirlyPresentacion">
          <img src="https://img.shields.io/badge/Ver_repositorio-6c5ce7?style=for-the-badge&logo=github&logoColor=white"/>
        </a>
      </td>
    </tr>
  </table>
</div>

# 🎯 Mejoras futuras

- 🔗 **Conexión con SDK de pago real** (Stripe/PayPal) para retiros reales
- 🔔 **Alertas push** mediante Firebase Cloud Messaging
- 🗺️ **Mapa interactivo nativo** con filtro de visualización de proveedores
- 🌙 **Modo oscuro completo** en la app Android
- 📸 **Subida de imágenes** desde el dispositivo para anuncios y perfiles
- 🧪 **Tests unitarios y de integración** con cobertura completa

<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:6c5ce7,100:3f51b5&height=100&section=footer" width="100%"/>
</div>

<div align="center">
  <sub>© 2025 iMirly — Todos los derechos reservados</sub>
</div>
