# Ogen Migration Guide - Part 3: Troubleshooting

**See Part 1 for overview and Part 2 for migration steps**

---

## WARNING Breaking Changes

### 1. Handler Signatures

**Changed:**
- Parameters: now typed structs, not individual args
- Return: typed response interface, not void + `ResponseWriter`
- Context: always first parameter

**Example:**
```go
// OLD
func GetPlayer(w http.ResponseWriter, r *http.Request, id string)

// NEW
func GetPlayer(ctx context.Context, params api.GetPlayerParams) (api.GetPlayerRes, error)
```

### 2. Field Names (Case Sensitive!)

ogen uses **exact** OpenAPI naming:
```yaml
# OpenAPI
comboId: ...   # ← lowercase 'i'
characterId: ...

# ogen generates
params.ComboId    # ← Exact match!
params.CharacterId
```

**Common mistakes:**
- ❌ `ComboID` (uppercase) → OK `ComboId` (lowercase)
- ❌ `CharacterID` → OK `CharacterId`

**Fix:** Check generated `oas_schemas_gen.go` for exact names!

### 3. Optional Fields

**OLD (oapi-codegen):**
```go
type Player struct {
    Name *string `json:"name,omitempty"`
}

// Usage
if player.Name != nil {
    fmt.Println(*player.Name)
}
```

**NEW (ogen):**
```go
type Player struct {
    Name OptString `json:"name"`
}

// Usage
if player.Name.IsSet() {
    fmt.Println(player.Name.Value)
}

// Create
player.Name = api.NewOptString("John")
```

### 4. Arrays

**OLD:**
```go
Combos: &[]api.Combo{}  // Pointer to slice
```

**NEW:**
```go
Combos: []api.Combo{}  // Direct slice
```

### 5. Response Types

**OLD:**
```go
return &api.ComboCatalogResponse{...}, nil
```

**NEW (check interface name!):**
```go
// Must implement api.GetComboCatalogRes interface!
return &api.ComboCatalogResponse{...}, nil
```

---

## 🔧 Common Issues & Solutions

### Issue 1: "does not implement Handler"

**Error:**
```
*Handlers does not implement api.Handler (wrong type for method GetPlayer)
    have GetPlayer("net/http".ResponseWriter, *"net/http".Request, string)
    want GetPlayer(context.Context, api.GetPlayerParams) (api.GetPlayerRes, error)
```

**Solution:** Update ALL handler signatures to typed responses:
```go
func (h *Handlers) GetPlayer(ctx context.Context, params api.GetPlayerParams) (api.GetPlayerRes, error) {
    // ...
}
```

### Issue 2: "external references are disabled"

**Error:**
```
resolve "schemas.yaml#/components/schemas/Player": external references are disabled
```

**Solution:** Bundle OpenAPI spec:
```bash
npx --yes @redocly/cli bundle proto/openapi/{service}.yaml -o openapi-bundled.yaml
ogen --target pkg/api --package api --clean openapi-bundled.yaml
```

### Issue 3: Field name mismatches

**Error:**
```
params.ComboID undefined (but does have field ComboId)
```

**Solution:** ogen uses EXACT OpenAPI naming. Check `oas_schemas_gen.go`:
```bash
grep "type.*Params struct" pkg/api/oas_schemas_gen.go -A 10
```

### Issue 4: Optional field access

**Error:**
```
invalid operation: req.TeamCoordination != nil (mismatched types api.OptNilInt and untyped nil)
```

**Solution:** Use ogen optional API:
```go
// OLD
if req.TeamCoordination != nil {
    score = *req.TeamCoordination
}

// NEW
if req.TeamCoordination.IsSet() {
    score = req.TeamCoordination.Value
}
```

### Issue 5: Missing SecurityHandler

**Error:**
```
not enough arguments in call to api.NewServer
    want (api.Handler, api.SecurityHandler, ...api.ServerOption)
```

**Solution:** Create SecurityHandler:
```go
type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
    // Validate JWT
    return ctx, nil
}

// Usage
secHandler := &SecurityHandler{}
server, err := api.NewServer(handlers, secHandler)
```

---

## 🚨 Common Mistakes to Avoid

1. **Don't mix oapi-codegen and ogen types** - full migration only
2. **Don't forget SecurityHandler** - api.NewServer requires it
3. **Don't use pointers for slices** - ogen uses direct slices
4. **Don't use `*int` for optionals** - use `OptInt`
5. **Don't benchmark wrong code** - use REAL handlers, not helpers
6. **Don't skip bundle step** - ogen doesn't support $ref by default

---

## 💡 Pro Tips

1. **Use PoC as template** - `services/combat-combos-service-ogen-go/`
2. **Generate first, code later** - see what ogen creates
3. **Check field names** - grep `oas_schemas_gen.go` for exact names
4. **Keep same business logic** - only change handler/response layer
5. **Run benchmarks early** - catch regressions fast
6. **Test with real DB** - connection pooling still matters

---

## 📚 Documentation

**ogen official:**
- https://ogen.dev/docs/intro
- https://github.com/ogen-go/ogen

**Performance context:**
- `.cursor/performance/01-memory-concurrency-db.md`
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`

**Related:**
- Issue #1590 - ogen research
- Issue #1578 - proof that pooling fails with oapi-codegen

---

## 🎉 Expected Results

**Per 10k RPS service:**

**Before (oapi-codegen):**
- Memory: 65 MB/sec
- Allocations: 250k allocs/sec
- GC pauses: frequent

**After (ogen):**
- Memory: 3.2 MB/sec (62 MB saved!)
- Allocations: 50k allocs/sec (200k saved!)
- GC pauses: minimal

**For entire backend (10 services × 10k RPS):**
- **620 MB/sec memory savings**
- **2M allocs/sec reduction**
- **Massive GC pressure relief**

**This is WHY we migrate!** 🚀

