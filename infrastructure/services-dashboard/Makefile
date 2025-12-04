# Issue: Services dashboard
.PHONY: dev build run

dev:
	go run main.go

build:
	CGO_ENABLED=0 go build -ldflags="-w -s" -o services-dashboard .

run: build
	./services-dashboard

