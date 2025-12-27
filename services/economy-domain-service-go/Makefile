build:
	go build -o bin/economy-service .

run:
	go run main.go

test:
	go test ./...

deps:
	go mod tidy
	go mod download

fmt:
	go fmt ./...

docker-build:
	docker build -t economy-service .

docker-run:
	docker run -p 8080:8080 economy-service

setup: deps build docker-build
