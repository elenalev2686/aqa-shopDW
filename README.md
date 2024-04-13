# Дипломный проект по профессии «Тестировщик ПО»

[План автоматизации](docs/Plan.md)

[Отчет по итогам тестирования](docs/Report.md)

[Отчет по итогам автоматизации](docs/Summary.md)

### Инструкция по запуску
#### 1. Склонировать репозиторий  <code>git clone <code>https://github.com/elenalev2686/aqa-shopDW</code>
#### 2. Перейти в папку aqa-shopDW
#### 3. Запустить контейнеры docker: Для работы с базами данных mysql, postgres выполнить команду:  

   * docker-compose up --build
   * После прогона тестов остановить контейнеры: docker-compose down

#### 4. Запустить приложение:
   Для запуска приложения с базами данных выполнить команду mysql: java -jar aqa-shop.jar -Dspring.datasource.url=jdbc:mysql://localhost:3306/app;
   postgres выполнить команду: java -jar aqa-shop.jar -Dspring.datasource.url=jbc:postgresql://localhost:5432/app
   
#### 5. Запустить тесты:
   Для запуска тестов с базами данных выполнить команды:  
   * gradlew test -Dbs.url=jdbc:postgres://localhost:5432/app
   * gradlew test -Dbs.url=jdbc:mysql://localhost:3306/app

#### 6. Сформировать отчеты командой:  
   * gradlew allureReport

#### 7. Открыть отчеты в браузере командой:  
   * gradlew allureServe

### Prerequisites
##### Для использования необходимо установить:

- IntelliJ IDEA
- Git
- Google Chrome браузер
- Docker
