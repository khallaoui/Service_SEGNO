# SEGNO - Intelligent Search Web Service

🎯 **SEGNO** is a RESTful web service designed for the e-commerce marketplace SEGNO. It provides intelligent product search, filtering, and discovery features to enhance user experience with personalized, performant, and scalable APIs.

---

## 🔗 Live Deployment
- **Production:** Railway
- **Swagger API Documentation:** Swaggerhub

---

## 🛠 Technology Stack

- **Backend:** Java, Spring Boot, Spring MVC, RESTful APIs
- **Database:** PostgreSQL
- **Frontend (for testing / examples):** Swagger UI
- **Deployment:** [Railway](https://railway.com/)
- **External Services:** Consumes external web services for product data

---

## ⚙️ Features

- **Smart Text Search** – Search products with partial or complete keywords.
- **Advanced Filters** – Filter results by category, price range, ratings, and popularity.
- **Search Suggestions** – Real-time suggestions while typing.
- **User Search History** – Store and retrieve previous search queries.
- **Popular Products** – Show trending products automatically.
- **Pagination & Sorting** – Optimized pagination and customizable sorting.
- **Performance & Scalability** – Designed for low latency and high throughput.

---

## 📦 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/search` | GET | Search products by keywords, category, price, rating, etc. |
| `/api/search/suggestions` | GET | Get dynamic search suggestions based on user input |
| `/api/search/popular` | GET | Retrieve most popular products |
| `/api/search/categories` | GET | List available categories |
| `/api/search/history/{userId}` | GET | Get user search history |
| `/api/search/history/{userId}` | DELETE | Clear user search history |

For full API documentation, see [SwaggerHub](https://app.swaggerhub.com/apis/universityibntofail/SEGNO/1.0.0).

---

## 🔧 Configuration

```yaml
server:
  port: ${PORT:8080}

spring:
  datasource:
    url: jdbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
    username: ${PGUSER}
    password: ${PGPASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    open-in-view: false
