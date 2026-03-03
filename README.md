<div align="center">
  <img src="https://img.shields.io/badge/Status-MVP-FFA500?style=for-the-badge" alt="Status" />
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Backend" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="DB" />
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-8A2BE2?style=for-the-badge" alt="Architecture" />
</div>

# 🚀 iMirly - Plataforma de Contratación de Servicios

**iMirly** es un mercado de servicios moderno donde proveedores y clientes pueden conectar, negociar presupuestos a través de un chat en tiempo real, realizar pagos seguros y valorar la experiencia. Todo el proceso está centralizado, de principio a fin, en una aplicación Android nativa acompañada por un backend robusto.

---

## 🌟 Características Principales

*   💬 **Chat en Tiempo Real:** Comunicación fluida y unificada entre clientes y profesionales.
*   🤝 **Sistema Inteligente de Presupuestos:** Ofertas, rechazos y acuerdos gestionados directamente como burbujas dinámicas dentro del flujo conversacional del chat.
*   💳 **Monedero Virtual Integrado:** Retención segura del saldo. Los clientes pagan al aceptar el presupuesto, pero el profesional recibe su importe exacto (restando comisiones previas) cuando el servicio se completa y aprueba mutaumente.
*   🛠️ **Gestor Dinámico de Tareas:** Los proveedores pueden marcar el trabajo como "Finalizado" y esperar a que el cliente lo "Apruebe y libere el pago" o pida revisiones.
*   ⭐ **Sistema de Valoración Global:** Reseñas bidireccionales de 1 a 5 estrellas al finalizar cada servicio que nutren un algoritmo de confianza reflejado en los perfiles.

---

## 🏗️ Arquitectura y Tecnologías

El proyecto se divide en dos grandes componentes orquestados para lograr escalabilidad, mantenimiento fácil y seguridad.

### 📱 Frontend (Aplicación Android)
- **Lenguaje:** Kotlin 
- **UI Toolkit:** Jetpack Compose (100% UI Declarativa)
- **Navegación:** Compose Navigation
- **Red & API:** Retrofit + OkHttp
- **Gestión de Estado:** ViewModels y Corrutinas (`viewModelScope`)
- **Autenticación:** JWT Tokens interceptores en cabecera HTTP

### ⚙️ Backend (Servidor API)
- **Core:** Java 21 + Spring Boot 3
- **Arquitectura:** Hexagonal / Clean Architecture (Puertos y Adaptadores para separar totalmente la lógica de dominio de las librerías o bases de datos).
- **Persistencia:** Spring Data JPA + Hibernate
- **Base de Datos:** PostgreSQL
- **Seguridad:** Spring Security con Filtros Stateful JWT y Base64 (`BCryptPasswordEncoder`).
- **Población en Frío:** DB Seeders automatizados para disponer de mockups de datos reales instantáneamente en desarrollo.

---

## 🛠️ Estructura del Proyecto: Backend (Arquitectura Hexagonal)
```
iMirly/
│── frontend/ # Aplicación Android completa
└── backend/ # API REST en Spring Boot
    └── src/main/java/tfg/imirly
        ├── auth/ # Módulo de usuarios, registro y monedero
        ├── catalog/ # Gestión de anuncios, subcategorías y explorador
        ├── contracting/ # Ciclo de vida del contrato y pagos retenidos
        └── messages/ # Chat dinámico, envío de ofertas y encuestas de valoración
```

> Cada uno de los módulos internos se rige por:
> **1. Dominio (`domain`):** Reglas puras del negocio (`.java` classes), sin librerías de terceros (ni Spring, ni DB). Puertos definidos mediante Interfaces (Ej: `OutPortRepository`).
> **2. Aplicación (`application`):** Casos de uso concretos que ejecutan las directrices del negocio (Los "Servicios" principales que implementan `InPorts`).
> **3. Infraestructura (`infrastructure`):** Implementaciones reales (Adaptadores). Contiene los controladores REST (`input`) y los repositorios JPA (`output`).


## ⚙️ Cómo empezar

### Requisitos
- **Android Studio** (Koala o superior recomendado).
- **PostgreSQL** (Ejecutándose en local).
- **Java JDK 21**.

### Pasos de Instalación
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Anyeel/imirly.git
   ```

2. Configurar PostgreSQL:
   Crea una base de datos llamada `imirly_db` y cerciórate de que tus credenciales en el archivo `backend/src/main/resources/application.properties` coinciden.
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/imirly_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```

3. Inicializar el Servidor Spring Boot:
   Ejecuta la clase principal (`TfgApplication.java`). Al arrancar por primera vez, el `DatabaseSeeder` inyectará las categorías completas de trabajo (Reformas, Educación, Estética, Salud) automáticamente.

4. Compilar en Android Studio:
   Abre la carpeta `frontend/` y haz Sync. Conecta un emulador o dispositivo real para lanzar la aplicación.

---

### 🚀 Futuras Mejoras 
- [ ] Conectar una SDK de pago directo como Stripe para retiros reales.
- [ ] Alertas mediante web-sockets o Push Notifications.
- [ ] Filtro de visualización en el mapa interactivo nativo. 

---

_Proyecto desarrollado con arquitectura escalable y las mejores prácticas en el marco del 2026._
