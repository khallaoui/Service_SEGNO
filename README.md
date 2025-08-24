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

----------------------------------------------------
####################################################
----------------------------------------------------


## 📄 Complete API Specification (OpenAPI 3.0.3)

openapi: 3.0.3
info:
  title: SEGNO - Service de Recherche
  description: |
    🎯 **Service de recherche intelligent pour la marketplace e-commerce SEGNO**

    Ce service d'API RESTful fournit les fonctionnalités essentielles pour permettre aux utilisateurs de naviguer, rechercher, filtrer et découvrir efficacement les produits disponibles sur la plateforme SEGNO. Il est conçu pour améliorer l'expérience utilisateur en facilitant l'accès aux produits pertinents, tout en intégrant des mécanismes de personnalisation, d'optimisation de la pertinence, et de performance.

    🔍 **Fonctionnalités principales :**

    ✅ **Recherche textuelle intelligente**  
    Permet aux utilisateurs de rechercher des produits à partir de mots-clés partiels ou complets.

    ✅ **Filtres avancés personnalisables**  
    Les utilisateurs peuvent affiner leurs résultats avec des critères dynamiques.

    ✅ **Suggestions de recherche dynamiques**  
    Générées en temps réel à partir des données de saisie partielle.

    ✅ **Historique des recherches utilisateur**  
    Permet de consulter les requêtes précédentes de chaque utilisateur.

    ✅ **Affichage de produits populaires & tendances**  
    Liste automatiquement les produits les plus recherchés.

    ✅ **Pagination et tri intelligent**  
    Système de pagination optimisé avec critères de tri personnalisés.

    ✅ **Performance et scalabilité**  
    Conçu pour une faible latence avec des temps de réponse optimisés.
    
  version: 1.0.0
  contact:
    name: Équipe SEGNO
    email: mohamed.khallaoui@uit.ac.ma
  license:
    name: MIT
    url: https://opensource.org/licenses/MIT

servers:
  - url: https://servicesegno-production.up.railway.app
    description: Production server
  - url: http://localhost:8080
    description: Development server

paths:
  /api/search:
    get:
      tags:
        - Search
      summary: Rechercher des produits
      description: Recherche des produits en fonction de différents critères
      operationId: searchProducts
      parameters:
        - name: q
          in: query
          description: Terme de recherche principal
          required: false
          schema:
            type: string
            example: "smartphone"
        - name: category
          in: query
          description: Filtrer par catégorie
          required: false
          schema:
            type: string
            example: "électronique"
        - name: minPrice
          in: query
          description: Prix minimum
          required: false
          schema:
            type: number
            format: double
            minimum: 0
            example: 100.0
        - name: maxPrice
          in: query
          description: Prix maximum
          required: false
          schema:
            type: number
            format: double
            minimum: 0
            example: 1000.0
        - name: minRating
          in: query
          description: Note minimum (sur 5)
          required: false
          schema:
            type: number
            format: double
            minimum: 0
            maximum: 5
            example: 4.0
        - name: sortBy
          in: query
          description: Critère de tri
          required: false
          schema:
            type: string
            enum: [relevance, name, price, rating, popularity]
            default: relevance
        - name: sortOrder
          in: query
          description: Ordre de tri
          required: false
          schema:
            type: string
            enum: [asc, desc]
            default: desc
        - name: page
          in: query
          description: Numéro de page (commence à 0)
          required: false
          schema:
            type: integer
            minimum: 0
            default: 0
        - name: size
          in: query
          description: Nombre d'éléments par page
          required: false
          schema:
            type: integer
            minimum: 1
            maximum: 100
            default: 20
        - name: userId
          in: query
          description: ID utilisateur pour personnaliser les résultats
          required: false
          schema:
            type: string
            example: "user_123"
      responses:
        '200':
          description: Résultats de recherche trouvés
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SearchResponse'
        '400':
          description: Paramètres de recherche invalides
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /api/search/suggestions:
    get:
      tags:
        - Suggestions
      summary: Obtenir des suggestions de recherche
      description: Retourne des suggestions basées sur la saisie partielle de l'utilisateur
      operationId: getSearchSuggestions
      parameters:
        - name: q
          in: query
          description: Début du terme de recherche
          required: true
          schema:
            type: string
            minLength: 2
            example: "sma"
        - name: limit
          in: query
          description: Nombre maximum de suggestions
          required: false
          schema:
            type: integer
            minimum: 1
            maximum: 10
            default: 5
      responses:
        '200':
          description: Suggestions de recherche
          content:
            application/json:
              schema:
                type: object
                properties:
                  suggestions:
                    type: array
                    items:
                      type: string
                    example: ["smartphone", "smart tv", "smartwatch"]
                required:
                  - suggestions
        '400':
          description: Paramètres invalides
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /api/search/popular:
    get:
      tags:
        - Popular
      summary: Obtenir les produits populaires
      description: Retourne les produits les plus populaires basés sur les avis et recherches
      operationId: getPopularProducts
      parameters:
        - name: limit
          in: query
          description: Nombre de produits à retourner
          required: false
          schema:
            type: integer
            minimum: 1
            maximum: 20
            default: 10
      responses:
        '200':
          description: Produits populaires
          content:
            application/json:
              schema:
                type: object
                properties:
                  popularProducts:
                    type: array
                    items:
                      $ref: '#/components/schemas/Product'
                required:
                  - popularProducts
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /api/search/categories:
    get:
      tags:
        - Categories
      summary: Obtenir toutes les catégories disponibles
      description: Retourne la liste des catégories pour le filtrage
      operationId: getCategories
      responses:
        '200':
          description: Liste des catégories
          content:
            application/json:
              schema:
                type: object
                properties:
                  categories:
                    type: array
                    items:
                      type: object
                      properties:
                        id:
                          type: string
                          example: "electronique"
                        name:
                          type: string
                          example: "Électronique"
                        productCount:
                          type: integer
                          example: 2450
                      required:
                        - id
                        - name
                        - productCount
                required:
                  - categories
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

  /api/search/history/{userId}:
    get:
      tags:
        - History
      summary: Obtenir l'historique de recherche d'un utilisateur
      description: Retourne l'historique des recherches d'un utilisateur spécifique
      operationId: getUserSearchHistory
      parameters:
        - name: userId
          in: path
          description: ID de l'utilisateur
          required: true
          schema:
            type: string
            example: "user_123"
        - name: limit
          in: query
          description: Nombre d'entrées d'historique à retourner
          required: false
          schema:
            type: integer
            minimum: 1
            maximum: 50
            default: 10
      responses:
        '200':
          description: Historique de recherche
          content:
            application/json:
              schema:
                type: object
                properties:
                  history:
                    type: array
                    items:
                      $ref: '#/components/schemas/SearchHistoryEntry'
                required:
                  - history
        '400':
          description: Paramètres invalides
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '404':
          description: Utilisateur non trouvé
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

    delete:
      tags:
        - History
      summary: Effacer l'historique de recherche
      description: Supprime tout l'historique de recherche d'un utilisateur
      operationId: clearSearchHistory
      parameters:
        - name: userId
          in: path
          description: ID de l'utilisateur
          required: true
          schema:
            type: string
            example: "user_123"
      responses:
        '204':
          description: Historique effacé avec succès
        '404':
          description: Utilisateur non trouvé
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Erreur interne du serveur
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'

components:
  schemas:
    SearchResponse:
      type: object
      properties:
        products:
          type: array
          items:
            $ref: '#/components/schemas/Product'
        pagination:
          $ref: '#/components/schemas/Pagination'
        filters:
          $ref: '#/components/schemas/AppliedFilters'
        totalResults:
          type: integer
          example: 150
        searchTime:
          type: number
          format: double
          description: Temps de recherche en millisecondes
          example: 45.2
      required:
        - products
        - pagination
        - totalResults

    Product:
      type: object
      properties:
        id:
          type: string
          example: "prod_123"
        name:
          type: string
          example: "Smartphone iPhone 15"
        description:
          type: string
          example: "Smartphone dernière génération avec caméra 48MP"
        category:
          type: string
          example: "électronique"
        price:
          type: number
          format: double
          example: 999.99
        originalPrice:
          type: number
          format: double
          nullable: true
          example: 1199.99
        rating:
          type: number
          format: double
          example: 4.7
        reviewCount:
          type: integer
          example: 1250
        imageUrl:
          type: string
          format: uri
          example: "https://images.marketplace.com/prod_123.jpg"
        availability:
          type: string
          enum: [IN_STOCK, OUT_OF_STOCK, LIMITED_STOCK]
          example: "IN_STOCK"
        brand:
          type: string
          example: "Apple"
        tags:
          type: array
          items:
            type: string
          example: ["nouveau", "populaire", "promo"]
      required:
        - id
        - name
        - category
        - price
        - rating
        - availability

    Pagination:
      type: object
      properties:
        currentPage:
          type: integer
          example: 0
        totalPages:
          type: integer
          example: 8
        pageSize:
          type: integer
          example: 20
        totalElements:
          type: integer
          example: 150
        hasNext:
          type: boolean
          example: true
        hasPrevious:
          type: boolean
          example: false
      required:
        - currentPage
        - totalPages
        - pageSize
        - totalElements
        - hasNext
        - hasPrevious

    AppliedFilters:
      type: object
      properties:
        query:
          type: string
          nullable: true
          example: "smartphone"
        category:
          type: string
          nullable: true
          example: "électronique"
        priceRange:
          $ref: '#/components/schemas/PriceRange'
        minRating:
          type: number
          format: double
          nullable: true
          example: 4.0
        sortBy:
          type: string
          example: "relevance"
        sortOrder:
          type: string
          example: "desc"

    PriceRange:
      type: object
      properties:
        min:
          type: number
          format: double
          example: 100.0
        max:
          type: number
          format: double
          example: 1000.0
      required:
        - min
        - max

    SearchHistoryEntry:
      type: object
      properties:
        query:
          type: string
          example: "smartphone samsung"
        timestamp:
          type: string
          format: date-time
          example: "2024-12-15T10:30:00Z"
        resultCount:
          type: integer
          example: 45
      required:
        - query
        - timestamp

    ErrorResponse:
      type: object
      properties:
        error:
          type: string
          example: "INVALID_PARAMETERS"
        message:
          type: string
          example: "Le paramètre 'minPrice' doit être un nombre positif"
        timestamp:
          type: string
          format: date-time
          example: "2024-12-15T10:30:00Z"
        path:
          type: string
          example: "/api/search"
      required:
        - error
        - message
        - timestamp

  securitySchemes:
    BearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
