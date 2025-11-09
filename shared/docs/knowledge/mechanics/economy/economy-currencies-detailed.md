# Детальная Система Валют

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:32  
**api-readiness-notes:** Перепроверено 2025-11-09 03:32; подтверждены валютные уровни, конверсия и интеграции, блокеров для задач economy-service нет.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/currencies

**Версия:** 2.0.0  
**Дата:** 2025-11-06  
**Статус:** Ready for API

---

- **Status:** created
- **Last Updated:** 2025-11-09 03:23
---

---

## 💰 ВАЛЮТНАЯ СИСТЕМА

### Философия

1. **Евродоллар** — мировая валюта (как доллар в реальности)
2. **Региональные** — локальные валюты с курсами
3. **Faction Scrip** — валюта фракций (репутация)
4. **Premium** — монетизация (no P2W)
5. **Crypto** — киберпанк элемент

---

## 🌍 ОСНОВНАЯ ВАЛЮТА

### Eurodollar (€$)

**Lore:** Доминирующая валюта после краха старых экономик  
**Symbol:** €$ или ED  
**Decimal:** 2 знака (€$1,234.56)

**Usage:**
- Основная торговля
- NPC vendors
- Player-to-Player
- Auction House
- Crafting costs

**Earning:**
- Quest rewards: 100-100,000 ED
- Enemy drops: 10-500 ED
- Selling items: variable
- Mission contracts: 1,000-50,000 ED

**Sinks (траты):**
- Equipment repairs: 50-5,000 ED
- Ammo: 1-50 ED per unit
- Fast travel: 10-500 ED
- Housing/rent: 1,000-50,000 ED/month
- Cyberware installation: 5,000-500,000 ED
- Bribes: 100-100,000 ED

---

## 🗺️ РЕГИОНАЛЬНЫЕ ВАЛЮТЫ

### 1. Dollar (NUSA regions)

**Symbol:** $  
**Regions:** NUSA territories, некоторые корпо-зоны  
**Exchange Rate:** 1 $ = 0.95 €$

**Characteristics:**
- Стабильная
- Принимается в Night City
- Corpo-preferred

**Bonuses:**
- -5% цены у NUSA vendors
- +10% reputation gains с NUSA factions

---

### 2. Yen (¥) — Asian Regions

**Symbol:** ¥  
**Regions:** Japantown, Asian corporate zones  
**Exchange Rate:** 100 ¥ = 1 €$

**Characteristics:**
- Высоко ценится в Tyger Claws территориях
- Arasaka предпочитает yen

**Bonuses:**
- -10% prices в Japantown
- +15% rep с Tyger Claws, Arasaka

---

### 3. Nuevo Peso (₱) — Latino Regions

**Symbol:** ₱  
**Regions:** Heywood, Santo Domingo  
**Exchange Rate:** 20 ₱ = 1 €$

**Characteristics:**
- Популярна в gang territories
- Valentinos принимают охотно

**Bonuses:**
- -15% prices у Latino vendors
- +20% rep с Valentinos
- Уникальные товары доступны

---

### 4. Street Cred (SC) — Reputation Currency

**Symbol:** SC  
**Nature:** Нельзя купить, только заработать  
**Earning:** Quests, rep, achievements

**Usage:**
- Unlock exclusive vendors
- Faction-specific items
- Special missions
- Ripperdoc premium services

**Levels:**
- 0-100: Nobody
- 101-300: Known
- 301-600: Respected
- 601-1000: Legend

**Cannot Convert:** SC не конвертируется в €$

---

## 💎 FACTION CURRENCIES

### 1. Corpo Scrip

**Factions:** Arasaka, Militech, Biotechnica  
**Earning:** Corporate missions, espionage  
**Spending:** Corpo-exclusive gear, access, info

**Types:**

**Arasaka Credits (AC):**
- Premium cyber ware
- Japanese weapons
- Corporate intel

**Militech Vouchers (MV):**
- Military-grade equipment
- Tactical gear
- Weapon mods

**Biotech Points (BP):**
- Medical implants
- Humanity restoration
- Bio-mods

**Exchange:** Can trade between corpo currencies (1:1 base, ±20% fees)

---

### 2. Gang Tokens

**Gangs:** 6th Street, Valentinos, Tyger Claws, etc.  
**Earning:** Gang missions, territory control  
**Spending:** Gang-exclusive items, services

**Examples:**

**6th Street Chips:**
- Patriotic weapons
- American armor
- Safe house access

**Voodoo Marks:**
- Advanced cyberdecks
- Blackwall intel
- Netrunner programs

**Valentine Pesos:**
- Custom lowriders
- Latino cultural items
- Family connections

---

## 💻 CRYPTO CURRENCIES (Киберпанк элемент)

### BitCoin 2.0 (₿2)

**Lore:** Возрождённый после краха  
**Nature:** Decentralized, anonymous  
**Volatility:** High (±30% daily)

**Usage:**
- Black market trades
- Anonymous transactions
- Crypto-only vendors
- Money laundering

**Risk:**
- NetWatch traces crypto
- Volatile value
- Can lose in hacks

---

### DataCoin (DC)

**Nature:** Data-backed currency  
**Backing:** Information value  
**Stability:** Medium

**Usage:**
- Netrunner economy
- Information brokers
- Hack contracts

---

## 💸 PREMIUM CURRENCY (Монетизация)

### Neuro Credits (NC)

**Nature:** Real-money currency  
**Purchase:** $1 USD = 100 NC

**Usage (NO P2W):**
- **Cosmetics ONLY:**
  - Weapon skins
  - Armor appearances
  - Vehicle paints
  - Apartment decor
  - Emotes, dances
- **Convenience:**
  - Extra bank slots
  - Auction House listings (+5)
  - Fast travel unlock
  - Name change
- **Battle Pass:**
  - Season Pass access
  - Cosmetic tracks

**Cannot Buy:**
- ❌ Weapons (stats)
- ❌ Armor (stats)
- ❌ Implants
- ❌ Levels/XP
- ❌ In-game currencies (ED, SC)

---

## 🔄 CURRENCY EXCHANGE

### Exchange Rates (Dynamic)

**Base Rates:**
```
1 €$ (Eurodollar) = базовая единица
0.95 €$ = 1 $ (Dollar)
0.01 €$ = 1 ¥ (Yen)
0.05 €$ = 1 ₱ (Peso)
```

**Dynamic Factors:**
- Region economy (+5%)
- World events (±15%)
- Faction wars (±20%)
- Supply/demand (±10%)

**Exchange Fees:**
- NPC Exchangers: 5-10%
- Player Exchangers: 2-8% (set by player)
- Black Market: 1-15% (variable)

---

## 💱 CURRENCY EXCHANGE LOCATIONS

### Official Exchanges

**Downtown Financial District:**
- All currencies
- 5% fee (lowest)
- Secure, tracked

**Regional Banks:**
- Local currency focus
- 7% fee
- Faction bonuses

### Black Market

**Pacifica, Badlands:**
- Anonymous
- Variable fees (1-15%)
- Crypto accepted
- Risk: scams possible

---

## 📊 CURRENCY CAPS & LIMITS

### Storage Limits

**Personal Wallet:**
- €$ limit: 1,000,000 (carry limit)
- Street Cred: No limit
- Faction currencies: 100,000 each

**Bank Storage:**
- €$ limit: 10,000,000
- Insurance: 95% (if robbed)
- Interest: 0.5%/month

**Crypto Wallet:**
- Bitcoin 2.0: 1,000 ₿2
- DataCoin: No limit
- Risk: Can be hacked

---

## ⚖️ ECONOMIC BALANCE

### Inflation Control

**Money Sinks:**
- Equipment repairs (5% of wealth/month)
- Rent (2% of wealth/month)
- Ammo (combat players: 3%/month)
- Fast travel (1%/month)
- Taxes (faction/region: 2-5%/month)

**Money Sources:**
- Quests: Primary (60%)
- Combat/Loot: Secondary (25%)
- Trading: Advanced (10%)
- Production: Endgame (5%)

**Target:** Player wealth grows 10%/month (healthy economy)

---

## ✅ Готовность

- ✅ 10+ валют определены
- ✅ Курсы обмена созданы
- ✅ Premium currency (no P2W)
- ✅ Faction currencies
- ✅ Crypto элемент
- ✅ Balance sinks/sources

**Следующий шаг:** Детальный каталог ресурсов!
