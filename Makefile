# ----------------------------------------------------------------------
#  Clustered Data Warehouse - Developer Automation
# ----------------------------------------------------------------------

APP_NAME = clustered-data-warehouse
VERSION  = 0.0.1-SNAPSHOT
JAR_FILE = target/$(APP_NAME)-$(VERSION).jar

# Default goal
.DEFAULT_GOAL := help

# --  Build and Dependencies --

.PHONY: build
build: ## Clean and build the project (skipping tests for speed)
	@echo "Building the application..."
	./mvnw clean package -DskipTests

.PHONY: install
install: ## Install dependencies and build
	./mvnw clean install

# -- Testing and Quality --

.PHONY: test
test: ## Run unit tests
	@echo " Running Unit Tests..."
	./mvnw clean test

.PHONY: coverage
coverage: ## Generate Jacoco coverage report
	@echo "Generating Coverage Report..."
	./mvnw test jacoco:report
	@echo " Report generated at target/site/jacoco/index.html"

# -- Run Application --

.PHONY: run
run: ## Run locally using Spring Boot Maven plugin
	@echo "Starting application locally..."
	mvn spring-boot:run

.PHONY: run-jar
run-jar: build ## Build and run the JAR file
	@echo "Starting application from JAR..."
	java -jar $(JAR_FILE)

# -- Docker Operations --

.PHONY: docker-up
docker-up: ## Start App & Database using Docker Compose
	@echo "Starting Docker services..."
	docker-compose up --build -d
	@echo "Services are up! API: http://localhost:8080"

.PHONY: docker-down
docker-down: ## Stop Docker services
	@echo "Stopping Docker services..."
	docker-compose down

.PHONY: docker-logs
docker-logs: ## Tail Docker logs
	docker-compose logs -f

# -- Maintenance --

.PHONY: clean
clean: ## Remove target directory
	@echo "Cleaning project..."
	mvn clean

# -- Help --

.PHONY: help
help: ## Show this help menu
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'