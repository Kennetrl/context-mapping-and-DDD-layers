Context Mapping y Capas DDD — VeriTrabajo

Resumen

VeriTrabajo es una aplicación modular que aplica principios de Domain-Driven Design (DDD) y Context Mapping para organizar la complejidad del dominio y alinear el software con las necesidades del negocio. El repositorio contiene un backend (Spring Boot / Java) construido bajo capas DDD (domain, application, infrastructure) y un frontend (SPAs estático) para interacción con usuarios.

Tabla de contenidos

- Visión de negocio
- Arquitectura general
- Backend: diseño DDD y patrones aplicados (detalle)
  - Bounded Contexts relevantes
  - Líneas de integración entre contextos (context mapping)
  - Estructura de capas y convenciones
  - Eventos de dominio y comunicación entre contextos
- Frontend: propósito y comandos básicos
- Cómo ejecutar, probar y desplegar
- Contribución y convenciones

Visión de negocio

VeriTrabajo facilita la gestión de reputación y confianza entre profesionales y clientes (marketplace de servicios). El sistema prioriza la integridad de reglas del dominio —puntuaciones, revisiones, medallas y métricas de cumplimiento— y debe escalar manteniendo claridad de responsabilidades entre subdominios.

Arquitectura general

La arquitectura sigue DDD y separación por capas:

- Domain: entidades, value objects, agregados, repositorios (interfaces) y lógica pura del dominio.
- Application: casos de uso, orquestación de operaciones del dominio, DTOs y servicios de aplicación.
- Infrastructure: implementaciones concretas (JPA/Hibernate, adaptadores REST, mensajería, persistencia, configuración).
- API / Adapters: controllers y adaptadores para entrada/salida (REST controllers, mappers).

La separación permite que la lógica del dominio permanezca independiente de frameworks e infra.

Backend — Diseño DDD y patrones aplicados

Resumen rápido:
- Lenguaje ubicuo en inglés (nombres de clases y paquetes en inglés).
- Aplicación de agregados y value objects inmutables.
- Eventos de dominio para comunicación eventual entre contextos.
- Uso de Application Services para orquestar casos de uso.
- Repositorios como puertos (interfaces) con implementaciones en infrastructure.

Ejemplos concretos (módulo Reputation)

El módulo Reputation es la referencia principal del repositorio y muestra las prácticas DDD aplicadas:

- Modelos de dominio observables:
  - ConfidenceScore (Value Object; rango 0–100)
  - ComplianceMetrics (Value Object)
  - Badge (enum)
  - Review (Entity)
  - TradeReputation (Aggregate Root)

- Eventos de dominio (domain events):
  - ProfessionalProfileCreated
  - ReputationUpdated
  - BadgeAwarded

- Repositorios y servicios:
  - TradeReputationRepository (puerto) — implementado en infrastructure
  - ReputationCalculator (interfaz para reglas de cálculo)
  - ReputationApplicationService (casos de uso de alto nivel)

Patrones y decisiones de diseño entre contextos

El proyecto ejemplifica patrones comunes de context mapping y relaciones entre bounded contexts:

- Published Language / Domain Events: los contextos publican eventos de dominio (e.g., ProfessionalProfileCreated) para notificar a consumidores interesados sin acoplar implementaciones.
- Customer/Supplier y Upstream/Downstream: un contexto que requiere datos de identidad o perfil (Profile) es cliente del contexto que lo posee; el proveedor publica eventos o expone APIs.
- Anti-Corruption Layer (ACL) y Mappers: cuando un contexto integra modelos externos con diferente lenguaje ubicuo, se usan adaptadores/ACL para traducir y proteger el dominio.
- Shared Kernel (cuando procede): para pequeños objetos de valor compartidos entre contexts que requieren consistencia.
- Conformist: si un contexto no puede dictar un modelo, adapta su lógica para conformarse con el proveedor.

Comunicación entre contextos

- Asíncrona (preferida): eventos de dominio (pub/sub) que desacoplan ritmo y disponibilidad.
- Síncrona: REST APIs para consultas puntuales; se recomienda ACL para evitar contaminación del modelo.

Estructura de carpetas (resumen)

- backend/
  - src/main/java/com/veritrabajo/backend/
    - reputation/ (dominio de reputación: domain, application, repository, event, infrastructure)
    - ... (otros contexts o módulos)
  - pom.xml, .mvnw.cmd
- frontend/
  - package.json, código de UI
- docker-compose.yml (despliegue local)

Convenciones y calidad

- Nombres en inglés y lenguaje ubicuo en los dominios.
- Checkstyle/formatting en backend (LF endings, max 100 chars por línea) — el build valida reglas.
- Tests: backend probados vía .\mvnw.cmd test en Windows (ver secciones abajo).

Frontend (resumen)

El frontend es una Single Page Application que consume las APIs del backend y presenta UX para gestionar perfiles, revisiones y reputación.
Comandos comunes:
- Instalación: cd frontend && npm install
- Lint: npm run lint
- Build: npm run build
- Dev: npm start (si existe script)

Cómo ejecutar y probar

Backend (Windows):
- Compilar/Tests: abrir terminal en carpeta backend:\
  .\mvnw.cmd test
- Ejecutar local (Spring Boot):
  .\mvnw.cmd spring-boot:run

Frontend:
- cd frontend
- npm install
- npm run build
- npm run lint

Integración local con Docker (si aplica):
- docker-compose up --build

Buenas prácticas operativas

- Mantener las invariantes del dominio dentro de los Aggregates.
- Evitar que la infraestructura filtre lógica de negocio a los objetos de dominio.
- Favorecer eventos de dominio para consistencia eventual entre contextos y trazabilidad de cambios.

Contribuir

- Seguir lenguaje ubicuo en inglés para nuevos módulos.
- Añadir tests unitarios para lógica del dominio y contract tests para integraciones.
- Abrir PRs con descripción del cambio en términos del dominio (¿qué regla de negocio cambia?).

Contacto y recursos

- Autor / Equipo: (agregar responsables)
- Para dudas de modelado DDD: dejar issue describiendo el escenario de negocio y el impacto esperado; se discutirá el bounded context y el patrón de integración más adecuado.

Licencia

- (Indicar licencia del proyecto si aplica)

Anexos técnicos (ejemplos de diseño)

- TradeReputation aggregate encapsula: list of Review, aggregated confidenceScore, awarded Badges.
- ReputationUpdated event puede incluir: subjectId, previousScore, newScore, changeSource.
- ReputationApplicationService expone casos de uso: recordReview, recomputeReputation, awardBadge.

---

Esta documentación pretende servir como guía técnica y de negocio para desarrolladores, arquitectos y stakeholders. Para ampliaciones puntuales (diagramas C4, ejemplos de eventos, contract tests), añadir secciones específicas o abrir un issue indicando los requisitos deseados.
