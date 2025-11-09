---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:35
**api-readiness-notes:** UI Login Screen. Вход, регистрация, восстановление пароля. ~360 строк.
---

# UI Login Screen - Экраны входа

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:35  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Login UI  
**Размер:** ~360 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Login Screen

```
┌──────────────────────────────────────────────────────┐
│                                                       │
│               ⚡ NECPGAME ⚡                          │
│           CYBERPUNK MMORPG SHOOTER                    │
│                                                       │
│         ┌────────────────────────────┐               │
│         │ Username: [____________]   │               │
│         │ Password: [____________]   │               │
│         │                            │               │
│         │      [LOGIN]               │               │
│         │                            │               │
│         │  [Forgot Password?]        │               │
│         │                            │               │
│         │  No account?               │               │
│         │  [CREATE ACCOUNT]          │               │
│         └────────────────────────────┘               │
│                                                       │
└──────────────────────────────────────────────────────┘
```

**Features:**
- Email/Username + Password
- Remember me checkbox
- Forgot password link
- Create account link
- OAuth buttons (Steam, Google, Discord)

**API Calls:**
- POST /api/v1/auth/login
- POST /api/v1/auth/register
- POST /api/v1/auth/forgot-password

---

## Register Screen

```
┌──────────────────────────────────────────────────────┐
│ CREATE ACCOUNT                              [← Back] │
├──────────────────────────────────────────────────────┤
│                                                       │
│ Email:    [________________________]                 │
│ Username: [________________________]                 │
│ Password: [________________________]                 │
│ Confirm:  [________________________]                 │
│                                                       │
│ ☑️ I agree to Terms of Service                       │
│ ☑️ I am 18+ years old                                │
│                                                       │
│ [CREATE ACCOUNT]                                     │
│                                                       │
└──────────────────────────────────────────────────────┘
```

**Validation:**
- Email format
- Username 3-20 chars, unique
- Password 8+ chars, letters + numbers
- Terms acceptance

---

## Связанные документы

- `.BRAIN/05-technical/ui/game-start/server-selection.md` - Server (микрофича 2/3)
- `.BRAIN/05-technical/ui/game-start/character-select.md` - Characters (микрофича 3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:35)** - Микрофича 1/3 (split from ui-game-start.md)
