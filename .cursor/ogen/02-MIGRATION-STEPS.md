# Ogen Migration Guide - Part 2: Migration Steps

**See Part 1 for overview and benchmarks**

---

## 📋 Migration Checklist (Per Service)

### Phase 1: Preparation (1 hour)

**1. Verify OpenAPI Spec:**
```bash
cd proto/openapi/
redocly lint {service}.yaml

# If split into modules, bundle first:
npx --yes @redocly/cli bundle {service}.yaml -o /tmp/{service}-bundled.yaml
```

**2. Create PoC Branch:**
```bash
git checkout -b feat/migrate-{service}-to-ogen
```

**3. Backup Current Service:**
```bash
cd services/
cp -r {service}-go {service}-go-backup
```

### Phase 2: Code Generation (30 min)

**1. Install ogen:**
```bash
go install github.com/ogen-go/ogen/cmd/ogen@latest
```

**2. Update go.mod:**
```go
require (
    github.com/ogen-go/ogen v1.18.0
    go.opentelemetry.io/otel v1.38.0
    go.opentelemetry.io/otel/metric v1.38.0
    go.opentelemetry.io/otel/trace v1.38.0
    golang.org/x/sync v0.18.0
    golang.org/x/net v0.47.0
)
```

**3. Bundle OpenAPI (if needed):**
```bash
cd services/{service}-go/
npx --yes @redocly/cli bundle ../../proto/openapi/{service}.yaml -o openapi-bundled.yaml
```

**4. Generate with ogen:**
```bash
# Remove old oapi-codegen files
rm -rf pkg/api/*

# Generate with ogen
ogen --target pkg/api --package api --clean openapi-bundled.yaml
```

**Generated files (~20 files):**
- `oas_interfaces_gen.go` - Handler interface
- `oas_schemas_gen.go` - Typed structs
- `oas_handlers_gen.go` - HTTP handlers
- `oas_json_gen.go` - Fast JSON marshaling
- `oas_server_gen.go` - Server setup
- etc.

### Phase 3: Handler Migration (2-4 hours)

**Key Differences:**

#### oapi-codegen (OLD):
```go
// Interface{} based - SLOW!
type Handlers struct { service *Service }

func (h *Handlers) GetPlayer(w http.ResponseWriter, r *http.Request, id string) {
    player, err := h.service.GetPlayer(r.Context(), id)
    if err != nil {
        respondError(w, 500, err.Error())  // ← interface{} boxing!
        return
    }
    respondJSON(w, 200, player)  // ← interface{} boxing!
}

// Helper with interface{} - causes 25 allocs!
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
    json.Marshal(data)  // ← Reflection!
}
```

#### ogen (NEW):
```go
// Typed responses - FAST!
type Handlers struct { service *Service }

func (h *Handlers) GetPlayer(ctx context.Context, params api.GetPlayerParams) (api.GetPlayerRes, error) {
    player, err := h.service.GetPlayer(ctx, params.ID.String())
    if err != nil {
        return &api.GetPlayerNotFound{}, nil  // ← Typed error!
    }
    return player, nil  // ← Typed response! No interface{}!
}

// No helper needed - ogen marshals typed structs directly!
// Result: 5 allocs instead of 25!
```

**Migration Steps:**

**1. Update Handler Signatures:**

OLD:
```go
func (h *Handlers) GetComboCatalog(w http.ResponseWriter, r *http.Request, params api.GetComboCatalogParams)
```

NEW:
```go
func (h *Handlers) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (api.GetComboCatalogRes, error)
```

**2. Return Typed Responses:**

OLD:
```go
catalog, err := h.service.GetComboCatalog(ctx, params)
if err != nil {
    respondError(w, 500, err.Error())
    return
}
respondJSON(w, 200, catalog)
```

NEW:
```go
catalog, err := h.service.GetComboCatalog(ctx, params)
if err != nil {
    return &api.GetComboCatalogInternalServerError{}, err
}
return catalog, nil  // Typed! No interface{}!
```

**3. Handle Errors with Typed Responses:**

OLD:
```go
if err == ErrNotFound {
    respondError(w, 404, "Not found")
    return
}
```

NEW:
```go
if err == ErrNotFound {
    return &api.GetPlayerNotFound{}, nil  // Typed 404!
}
```

**4. Remove Helper Functions:**

DELETE (no longer needed):
- `respondJSON()` - ogen marshals directly
- `respondError()` - use typed error responses
- `responsePool` - ogen handles this better internally

### Phase 4: Service Layer (1-2 hours)

**Update return types for ogen compatibility:**

OLD:
```go
func (s *Service) GetPlayer(ctx context.Context, id string) (*api.Player, error) {
    player := &api.Player{
        Id: &idPtr,           // Pointers everywhere
        Name: &namePtr,
    }
    return player, nil
}
```

NEW (ogen uses OptX for optional):
```go
func (s *Service) GetPlayer(ctx context.Context, id string) (*api.Player, error) {
    player := &api.Player{
        ID:   uuid.MustParse(id),           // Direct types
        Name: api.NewOptString("Player"),   // ogen optional
    }
    return player, nil
}
```

**Common ogen types:**
- `OptInt` instead of `*int`
- `OptString` instead of `*string`
- `OptNilInt` for nullable int
- Use `api.NewOptX()` to create optionals
- Use `.IsSet()` and `.Value` to read

### Phase 5: Server Setup (30 min)

**OLD (oapi-codegen + router):**
```go
router := mux.NewRouter()
handlers := NewHandlers(service)
```

**NEW (ogen built-in server on ServeMux):**
```go
handlers := NewHandlers(service)
securityHandler := &SecurityHandler{}

ogenServer, err := api.NewServer(handlers, securityHandler)
if err != nil {
    log.Fatal(err)
}

router := http.NewServeMux()
router.Handle("/api/v1/", ogenServer)
```

**Security Handler (required):**
```go
type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
    // Validate JWT token
    claims, err := validateJWT(t.Token)
    if err != nil {
        return ctx, err
    }
    
    // Add to context
    return context.WithValue(ctx, "user_id", claims.UserID), nil
}
```

### Phase 6: Testing (1 hour)

**1. Create Benchmarks:**

```go
// File: server/handlers_bench_test.go
func BenchmarkOgenGetPlayer(b *testing.B) {
    repo, _ := NewRepository("postgres://test")
    service := NewService(repo)
    handlers := NewHandlers(service)

    ctx := context.Background()
    params := api.GetPlayerParams{ID: uuid.New()}

    b.ReportAllocs()
    b.ResetTimer()

    for i := 0; i < b.N; i++ {
        _, _ = handlers.GetPlayer(ctx, params)
    }
}
```

**2. Run Benchmarks:**
```bash
go test -run=^$ -bench=BenchmarkOgen -benchmem ./server -benchtime=3s
```

**Expected results:**
- Latency: <500 ns/op
- Memory: <1000 B/op
- Allocations: 3-7 allocs/op

**3. Integration Tests:**
```bash
go test ./... -v
```

### Phase 7: Deployment (1 hour)

**1. Update Makefile:**

```makefile
# Remove oapi-codegen targets
# generate-types, generate-server, generate-spec

# Add ogen target
generate-api:
	@echo "Generating API with ogen..."
	npx --yes @redocly/cli bundle ../../proto/openapi/$(SERVICE).yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml
	@echo "OK ogen generation complete"

build:
	@echo "Building $(SERVICE)..."
	go build -o $(SERVICE) .
	@echo "OK Build complete"

test:
	go test ./... -v

bench:
	go test -run=^$$ -bench=. -benchmem ./server -benchtime=3s
```

**2. Update Dockerfile (NO changes needed):**
```dockerfile
# Works the same!
FROM golang:1.24-alpine AS builder
COPY . .
RUN make generate-api  # Now uses ogen
RUN CGO_ENABLED=0 go build .
```

**3. Create PR:**
```bash
git add .
git commit -m "[backend] perf: migrate {service} to ogen

Migrated from oapi-codegen to ogen for performance.

Performance improvements:
- Latency: 90% faster
- Memory: 95% less
- Allocations: 80% fewer

Related Issue: #1590"

git push origin feat/migrate-{service}-to-ogen
```

---

## 🚀 Performance Validation

### Before Handoff (REQUIRED):

```bash
# 1. Build passes
go build ./...

# 2. Tests pass
go test ./...

# 3. Benchmarks meet targets
go test -bench=. -benchmem ./server

# Expected gains vs oapi-codegen:
# - Latency: >70% faster
# - Memory: >80% less
# - Allocations: >70% fewer
```

### Performance Targets:

**Acceptable (minimum):**
- Latency: <1000 ns/op
- Allocations: <10 allocs/op
- Memory: <2000 B/op

**Good:**
- Latency: <500 ns/op
- Allocations: <7 allocs/op
- Memory: <1000 B/op

**Excellent:**
- Latency: <300 ns/op
- Allocations: <5 allocs/op
- Memory: <500 B/op

---

## OK Success Criteria

**Service ready to handoff when:**

- [ ] Build passes (`go build ./...`)
- [ ] Tests pass (`go test ./...`)
- [ ] Benchmarks show >70% improvement
- [ ] All handlers use typed responses
- [ ] SecurityHandler implemented
- [ ] No `interface{}` in hot path
- [ ] PR created with benchmark results

---

**Next:** See Part 3 for code templates and Part 4 for troubleshooting

