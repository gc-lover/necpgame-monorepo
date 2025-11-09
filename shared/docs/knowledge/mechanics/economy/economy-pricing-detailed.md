---

- **Status:** approved
- **Last Updated:** 2025-11-09 03:52
---

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 04:03  
**api-readiness-notes:** Перепроверено 2025-11-09 04:03. Формулы ценообразования, коэффициенты редкости, качества, спроса/предложения, региональные и фракционные модификаторы описаны детально; документ готов к пакетированию задач economy-service.



# Детальная Система Ценообразования

**Версия:** 2.0.0  
**Дата:** 2025-11-06  
**Статус:** Ready for API

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/pricing

---

## 💰 БАЗОВОЕ ЦЕНООБРАЗОВАНИЕ

### Formula

```
Item Price = Base Value × Rarity Multiplier × Quality Modifier × Supply/Demand Factor × Region Modifier × Faction Modifier
```

---

## 📊 RARITY MULTIPLIERS

| Rarity | Multiplier | Vendor Sell | Vendor Buy |
|--------|------------|-------------|------------|
| **Common** | ×1.0 | 100% | 150% |
| **Uncommon** | ×3.0 | 90% | 180% |
| **Rare** | ×10.0 | 80% | 250% |
| **Epic** | ×35.0 | 70% | 400% |
| **Legendary** | ×100.0 | 60% | 800% |
| **Artifact** | ×500.0 | Cannot vendor | Player-only |

**Примеры:**
```
Base weapon value: 1,000 €$

Common: 1,000 €$
Uncommon: 3,000 €$
Rare: 10,000 €$
Epic: 35,000 €$
Legendary: 100,000 €$
```

---

## 🎲 QUALITY MODIFIERS

### Crafted Items

**Quality Roll:** ±20% variance

```
Perfect Roll (+20%): Price ×1.2
Good Roll (+10%): Price ×1.1
Average Roll (±5%): Price ×1.0
Poor Roll (-10%): Price ×0.9
Bad Roll (-20%): Price ×0.8
```

**Critical Craft Bonus:**
- +1 Rarity tier: Use higher tier pricing
- Special affix: +30% price

---

## 📈 SUPPLY & DEMAND

### Dynamic Market

**Demand Factors:**
```
High Demand (+50% price):
- Meta weapons (PvP popular)
- New patch items
- Raid-required gear
- Event items

Low Demand (-30% price):
- Nerfed items
- Oversupplied market
- Outdated gear
```

**Supply Factors:**
```
Low Supply (+40% price):
- Rare boss drops
- Limited production
- Faction war shortages

High Supply (-25% price):
- Common zone drops
- Mass production
- Oversaturation
```

**Server-Wide Tracking:**
- Items sold last 7 days
- Items listed on AH
- Production rates
- Algorithm adjusts prices every 6 hours

---

## 🗺️ REGIONAL PRICING

### Regional Modifiers

**Night City (Base):** ×1.0

**NUSA Regions:** ×1.1  
- Higher wealth
- Corporate presence
- +10% all prices

**Badlands:** ×0.7  
- Lower wealth
- Nomad economy
- -30% prices, limited selection

**Pacifica:** ×0.5-1.5 (volatile)  
- Black market
- No regulation
- Extreme variance

**Asian Enclaves (Japantown):** ×1.3  
- Premium goods
- Import costs
- Luxury market

---

## 🏛️ FACTION MODIFIERS

### Reputation Discounts

**Friendly (Rep 500+):** -10% buy, +10% sell  
**Honored (Rep 1000+):** -20% buy, +15% sell  
**Exalted (Rep 2000+):** -30% buy, +20% sell

**Hostile (Rep -500):** +50% buy, -50% sell  
**Hated (Rep -1000):** Cannot trade (banned)

---

## 🏪 VENDOR TYPES & PRICING

### NPC Vendors

**General Store:**
- Sells: Common-Uncommon
- Prices: Standard +20%
- Stock: Unlimited basics
- Refresh: Daily

**Specialty Shops:**
- Gunsmiths: Weapons/mods
- Ripperdocs: Implants
- Netrunners: Cyberdecks/programs
- Prices: Standard +30%
- Stock: Limited rare items
- Refresh: Weekly

**Black Market:**
- Sells: Rare-Legendary (illegal)
- Prices: -10% to +100% (variable)
- Stock: Random, limited
- Risk: Scams possible (10% chance fake)

---

### Player Vendors

**Player Shops:**
- Tax: 5% listing fee
- Can set own prices
- Faction territory: Additional taxes (2-10%)

**Recommended Margins:**
- Flipping: Buy low, sell +15-30%
- Crafting: Materials cost +40-80% profit
- Farming: Time investment +100-200%

---

## 💹 AUCTION HOUSE

### Bidding System

**Listing Fee:** 2% of starting bid

**Auction Types:**

**1. Standard Auction:**
- Duration: 24-72 hours
- Bids increase price
- Highest bidder wins
- Fee: 5% of final price (seller pays)

**2. Buyout:**
- Set buyout price
- Instant purchase option
- Fee: 3% (if buyout used)

**3. Bid-Only (Rare+):**
- No buyout
- Competition drives price
- Fee: 8% (high-value items)

---

### AH Pricing Strategy

**Undercutting:**
- Check current lowest
- List at -1-5%
- Fast sale

**Market Value:**
- Check average last 7 days
- List at average
- Standard sale time

**Premium Listing:**
- Rare/perfect rolls
- List at +20-50% average
- Wait for right buyer

---

## 🚢 TRADING ROUTES

### Regional Trade

**Example: Badlands → Night City**

**Buy in Badlands (cheap):**
- Raw ore: 10 €$ per unit
- Scrap: 2 €$ per unit
- Nomad crafts: 50 €$

**Sell in Night City (expensive):**
- Raw ore: 18 €$ (+80% profit)
- Scrap: 4 €$ (+100%)
- Nomad crafts: 90 €$ (+80%)

**Costs:**
- Transport: 100 €$ (fuel, time)
- Risk: Wraiths ambush (20% chance)
- Time: 30 minutes

**Profit:** ~500-1,000 €$ per run (with 100 units)

---

### Faction Arbitrage

**Example: Militech → Arasaka zones**

**Contraband Trade:**
- Buy Militech gear (cheap in Militech zones)
- Smuggle to Arasaka zones
- Sell at premium (+150%)

**Risks:**
- Caught by faction patrols (30%)
- Confiscation of goods
- Reputation loss (-100)
- Possible arrest/fine

**Reward:**
- 200-500% profit margins
- Rare items access
- Thrill of smuggling

---

## 📉 MARKET MANIPULATION

### Legal Methods

**Cornering Market:**
1. Buy all supply of resource
2. Relist at higher price
3. Profit when demand hits

**Limit:** Max 30% of market supply (anti-monopoly)

**Production Monopoly:**
1. Control rare recipe
2. Be sole producer
3. Set prices

**Limit:** Recipe sharing mechanics prevent total monopoly

---

### Illegal Methods

**Price Fixing:**
- Agreement between players
- Fix prices artificially
- **Detection:** Algorithm detects (ban risk)

**Wash Trading:**
- Trade with self to fake demand
- **Detection:** IP/account tracking (ban)

**Exploit Abuse:**
- Use bugs for profit
- **Penalty:** Ban + rollback

---

## 💎 SPECIAL CURRENCIES

### Crafting Vouchers

**Earned:** Daily/weekly quests  
**Usage:** Reduce crafting costs

**Types:**
- Material Voucher: -20% material cost
- Time Voucher: -50% craft time
- Success Voucher: +10% success rate

**Cannot Trade:** Bind on acquire

---

### Event Tokens

**Source:** Seasonal events  
**Usage:** Event-exclusive items

**Examples:**
- Halloween Token → Spooky cosmetics
- New Year Token → Firework effects
- Corp War Token → Faction gear

**Expiry:** End of season (spend or lose)

---

## 📊 PRICE EXAMPLES

### Weapons

| Item | Rarity | Base | Vendor Buy | Player Market |
|------|--------|------|------------|---------------|
| Budget Pistol | Common | 500 | 750 | 600-700 |
| Lexington | Uncommon | 2,500 | 4,500 | 3,000-3,800 |
| Custom Sniper | Rare | 15,000 | 37,500 | 18,000-25,000 |
| Malorian 3516 | Legendary | 200,000 | Cannot | 300k-1M |

### Implants

| Item | Rarity | Installation + Parts | Total Cost |
|------|--------|---------------------|------------|
| Basic Cyber-Eyes | Uncommon | 5,000 + 2,000 | 7,000 |
| Gorilla Arms | Rare | 25,000 + 8,000 | 33,000 |
| Sandevistan | Legendary | 200,000 + 50,000 | 250,000 |

### Resources

| Item | Tier | Unit Price | Bulk (100+) |
|------|------|------------|-------------|
| Scrap Metal | 1 | 2 | 1.5 (-25%) |
| Steel Ingot | 1 | 20 | 16 |
| Titanium Alloy | 2 | 150 | 120 |
| Neural Matrix | 3 | 2,500 | 2,000 |
| Quantum Core | 5 | 50,000 | No bulk |

---

## ⚖️ ECONOMIC BALANCE

### Wealth Tiers

**New Player (Lvl 1-10):**
- Wealth: 0-10,000 €$
- Income: 500-2,000 €$/day
- Can afford: T1 gear

**Mid-Game (Lvl 11-30):**
- Wealth: 10k-500k €$
- Income: 5,000-20,000 €$/day
- Can afford: T2-T3 gear

**End-Game (Lvl 31-50):**
- Wealth: 500k-5M €$
- Income: 30k-100k €$/day
- Can afford: T4 gear, some legendary

**Elite (Lvl 51+):**
- Wealth: 5M-100M+ €$
- Income: 100k-1M+ €$/day
- Can afford: Multiple legendary builds

---

## ✅ Готовность

- ✅ Pricing formulas определены
- ✅ Regional pricing создана
- ✅ Faction modifiers прописаны
- ✅ Market dynamics (supply/demand)
- ✅ Auction House mechanics
- ✅ Trade routes примеры
- ✅ Economic balance targets

**Для API готово!**
