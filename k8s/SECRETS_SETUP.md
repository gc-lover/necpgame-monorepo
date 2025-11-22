# Настройка Secrets для Kubernetes

Перед деплоем необходимо заполнить реальные значения в Secrets.

## Файл: `k8s/secrets-common.yaml`

### 1. Database Secrets

Замените `CHANGE_ME` на реальный пароль PostgreSQL:

```yaml
stringData:
  url: "postgresql://necpgame:ВАШ_ПАРОЛЬ@postgres:5432/necpgame?sslmode=disable"
  password: "ВАШ_ПАРОЛЬ"
```

**Безопасный способ создания Secret:**

```bash
kubectl create secret generic database-secrets \
  --from-literal=url="postgresql://necpgame:ВАШ_ПАРОЛЬ@postgres:5432/necpgame?sslmode=disable" \
  --from-literal=host="postgres" \
  --from-literal=port="5432" \
  --from-literal=database="necpgame" \
  --from-literal=username="necpgame" \
  --from-literal=password="ВАШ_ПАРОЛЬ" \
  --namespace=necpgame
```

### 2. Redis Secrets

Замените `CHANGE_ME` на реальный пароль Redis (если используется):

```yaml
stringData:
  url: "redis://:ВАШ_ПАРОЛЬ@redis:6379"
  password: "ВАШ_ПАРОЛЬ"
```

**Безопасный способ создания Secret:**

```bash
kubectl create secret generic redis-secrets \
  --from-literal=url="redis://:ВАШ_ПАРОЛЬ@redis:6379" \
  --from-literal=host="redis" \
  --from-literal=port="6379" \
  --from-literal=password="ВАШ_ПАРОЛЬ" \
  --namespace=necpgame
```

### 3. JWT Secrets

Замените `CHANGE_ME_JWT_SECRET` на реальный JWT secret:

```yaml
stringData:
  secret: "ВАШ_JWT_SECRET_ЗДЕСЬ"
  issuer: "http://keycloak:8080/realms/necpgame"
```

**Безопасный способ создания Secret:**

```bash
kubectl create secret generic jwt-secrets \
  --from-literal=secret="ВАШ_JWT_SECRET_ЗДЕСЬ" \
  --from-literal=issuer="http://keycloak:8080/realms/necpgame" \
  --namespace=necpgame
```

## Генерация безопасных паролей

```bash
# Генерация пароля для PostgreSQL
openssl rand -base64 32

# Генерация JWT secret
openssl rand -hex 32
```

## Проверка Secrets

После создания проверьте:

```bash
kubectl get secrets -n necpgame
kubectl describe secret database-secrets -n necpgame
```

## Важно

- **НЕ коммитьте** файлы с реальными паролями в Git
- Используйте `kubectl create secret` вместо редактирования YAML
- Используйте внешние системы управления секретами (HashiCorp Vault, AWS Secrets Manager) для продакшена
- Регулярно ротируйте пароли и секреты

