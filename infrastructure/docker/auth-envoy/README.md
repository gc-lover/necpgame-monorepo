## Auth/Envoy Perimeter (HTTP/3)

1) Сгенерировать сертификаты для Envoy
```bash
bash scripts/certs/generate-envoy-certs.sh
```

2) Запустить Keycloak и Envoy
```bash
cd infrastructure/docker/auth-envoy
docker compose up -d
```

3) Проверить
- Envoy admin: https://localhost:8443/health (самоподписанный сертификат)
- Keycloak dev: http://localhost:8080


