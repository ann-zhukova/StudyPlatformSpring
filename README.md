# Лабораторная работа №2 Spring

Выполнили: студенты группы 6131-010402D Жукова Анна, Недугов Владимир

## Схема базы данных
<img width="1001" height="398" alt="image" src="https://github.com/user-attachments/assets/fcb7f024-9657-48fc-a332-a97f09469bc7" />

## Используемый стек
- Java 17
- Maven (Spring Boot Maven Plugin)
- Spring Boot 3.5.6
- Spring Web (Spring MVC)
- Spring Data JPA (Hibernate как провайдер)
- Spring Boot Starter Thymeleaf
- База данных: PostgreSQL (JDBC‑драйвер 42.7.7)
- Фронт: Thymeleaf + Bootstrap 5.1.3 + Bootstrap Icons 1.8.1 (CDN)

## Сравнение JAX-RS и Spring REST
- JAX-RS — стандарт Jakarta EE для REST; реализация зависит от контейнера (Jersey, RESTEasy и т.п.).
- Spring REST — часть Spring MVC, глубоко интегрирована со Spring Boot и всей экосистемой Spring.
- Spring Boot даёт автонастройку: быстрее старт и меньше кода.
- JAX-RS обычно требует явной конфигурации и больше ручной сборки.

Выбор: Spring REST быстрая реализация JSON/XML и простая интеграция XSLT через кастомный XML‑конвертер.

## Реализация REST API с JSON и XML

- Эндпоинты:
  - /api/topics (GET/POST), /api/topics/{id} (GET/PUT/DELETE)
  - /api/tasks (GET/POST), /api/tasks/{id} (GET/PUT/DELETE)
- Форматы:
  - Поддерживаются JSON и XML для входных и выходных данных (Content‑Type/Accept).
  - В браузере можно явно указать `?format=xml` или `?format=json`.
  - Для XML‑ответов автоматически добавляется `<?xml-stylesheet ...>` для XSL‑преобразования (см. ниже).
- Примеры:
  - JSON: `http://localhost:8080/api/tasks?format=json`
  - XML: `http://localhost:8080/api/topics?format=xml` (XSL преобразует XML в HTML)
Пример пост запроса:
  - `curl -X POST "http://localhost:8080/api/tasks?topicId=1&format=json" -H "Content-Type: application/json" -H "Accept: application/json" -d "{\"title\":\"Implement caching\",\"description\":\"Add caching to service\",\"completionStatus\":\"NOT_STARTED\"}"`
## XSLT и отображение в браузере

- Файл XSL размещён в статике: `/xsl/study.xsl`.
- Страницы генерируются из XML (topics/topic/tasks/task) и содержат навигацию между сущностями.
- В начало каждого XML‑ответа REST автоматически добавляется PI: `<?xml-stylesheet type="text/xsl" href="/xsl/study.xsl"?>` — браузер отображает HTML.

