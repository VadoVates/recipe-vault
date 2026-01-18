# RecipeVault

Aplikacja do zarządzania przepisami kulinarnymi z planowaną funkcjonalnością spiżarki.

## Stack technologiczny

- **Backend:** Java 17, Spring Boot 4.0
- **Baza danych:** PostgreSQL 16
- **Migracje:** Flyway
- **Testy:** JUnit 5, Testcontainers, REST Assured

## Wymagania

- Java 17+
- PostgreSQL 16+
- Maven 3.9+

## Uruchomienie lokalne

### 1. Baza danych

Utwórz bazę i użytkownika:
```bash
sudo -u postgres psql
CREATE DATABASE recipevault;
CREATE USER recipevault_user WITH PASSWORD 'recipevault_pass';
GRANT ALL PRIVILEGES ON DATABASE recipevault TO recipevault_user;
\q
```

### 2. Konfiguracja

Skopiuj `src/main/resources/application-dev.yml.example` do `application-dev.yml` i uzupełnij dane dostępowe.

### 3. Uruchomienie
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Aplikacja wystartuje na `http://localhost:8080`.

## Testy
```bash
./mvnw test
```

Testy integracyjne używają Testcontainers – wymagany Docker.

## Struktura projektu
```
src/main/java/com/vadovates/app/recipevault/
├── config/          # Konfiguracja Springa
├── recipe/          # Moduł przepisów
├── ingredient/      # Moduł składników
├── user/            # Moduł użytkowników
└── common/          # Wspólne komponenty (wyjątki, DTO)
```

## API

Dokumentacja API dostępna pod `/swagger-ui.html` (po dodaniu SpringDoc).

## Roadmap

- [x] Szkielet projektu
- [ ] CRUD przepisów
- [ ] CRUD składników
- [ ] Wyszukiwanie przepisów
- [ ] Autentykacja (JWT)
- [ ] Spiżarka (faza II)

## Licencja

MIT