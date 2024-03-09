# Дипломный проект по профессии «Тестировщик ПО»

[План автоматизации](docs/Plan.md)

[Отчет по итогам тестирования](docs/Report.md)

[Отчет по итогам автоматизации](docs/Summary.md)

---------------------
### Инструкция по запуску
#### 1. Склонировать репозиторий  <code>git clone <code>https://github.com/elenalev2686/aqa-shopDW</code>
#### 2. Перейти в папку aqa-shopDW
#### 3. Запустить контейнеры docker: Для работы с базами данных mysql, postgres выполнить команду:  
   <code>docker-compose up --build</code>
   После прогона тестов остановить контейнеры:  
   <code>docker-compose down</code>
#### 4. Запустить приложение:
   Для запуска приложения с базами данных mysql, postgres выполнить команду:  
   <code>java -jar aqa-shop.jar</code>
#### 5. Запустить тесты:
   Для запуска тестов с базами данных выполнить команду:  
   <code>gradlew test -Ddb</code>
#### 6. Сформировать отчеты командой:  
   <code>gradlew allureReport</code>
#### 7. Открыть отчеты в браузере командой:  
   <code>gradlew allureServe</code>
### Prerequisites
##### Для использования необходимо установить:
- IntelliJ IDEA
- Git
- Google Chrome браузер
- Docker
