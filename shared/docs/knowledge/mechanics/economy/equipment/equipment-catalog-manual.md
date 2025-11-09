# Equipment Catalog (Manual Snapshot)

---
- **Status:** in-review
- **Last Updated:** 2025-11-09 04:35
---
**api-readiness:** needs-work  
**api-readiness-check-date:** 2025-11-09 04:35  
**api-readiness-notes:** Частичный каталог без параметров стоимости, дропа, процедурных связок и статистики. Требуется синхронизация с экономическими коэффициентами перед постановкой API задач.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/equipment

---

## 📊 Сводка по категориям

| Категория | Количество | Корпорации/фракции | Комментарий |
|-----------|------------|--------------------|-------------|
| Weapons | 8 | Arasaka, Militech, Kang-Tao, Tsunami, Mox, Zetatech, NeurON | Охватывают ballistic, smart и энерговарианты |
| Armor | 6 | Trauma Team, Arasaka, Mox, Militech, Biotechnica, NeurON | Включает культовые образы и модульные фреймы |
| Cyberware | 6 | Arasaka, NetWatch, Zetatech, Biotechnica, Kang-Tao, Trauma Team | Баланс активных и пассивных модулей |
| Support Gear | 4 | Zetatech, Militech, Mox, Trauma Team | Тактические дроны, турели, медпод |
| **Итого** | **24** | 7 крупных корпораций + Mox Syndicate | Базовая выборка без процедурной генерации |

---

## Weapons — 8 единиц

| Название | Корпорация | Тип | Редкость | Ключевая особенность |
|----------|------------|-----|----------|-----------------------|
| Hannya-57 | Arasaka | Smart Assault Rifle | Rare | «Data Spike» — повышение hack speed при попадании |
| Sabre IX | Militech | LMG | Epic | «Siege Sync» — авто-турели при подавлении |
| Pulsevein | Kang-Tao | SMG | Uncommon | «Dual Mode» — мгновенное переключение между lethal/non-lethal |
| Seastorm-12 | Tsunami Defense | Rail Shotgun | Epic | «EMP Buckshot» — отключение щитов в радиусе |
| Velvet Fang | Mox Syndicate | Heavy Pistol | Rare | «Glam Strike» — бонус критов в скрытности |
| Malorian Arms 3516b | Malorian (Johnny Continuum) | Smart Pistol | Legendary | «Rebel Auto-Aim» |
| Vector Null | Zetatech | Smart Sniper | Legendary | «Trace Lock» — блокирует укрытия и дроны |
| Synwave Bow | NeurON Forge | Tech Bow | Artifact | «Phase Arrow» — перевод цели в цифровую фазу |

---

## Armor — 6 единиц

| Название | Корпорация | Тип | Редкость | Ключевая особенность |
|----------|------------|-----|----------|-----------------------|
| Guardian Mk4 | Trauma Team Armory | Tactical Vest | Rare | «Revive Protocol» — ускоренное лечение союзников |
| Arasaka Kage Cloak | Arasaka | Tactical Suit | Legendary | «Shadow Split» — голографические двойники |
| Mox Bloom Dress | Mox Syndicate | Light Armor | Legendary | «Chroma Veil» — клубная невидимость |
| Vanguard Shell | Militech | Heavy Armor | Epic | «Siege Mode» — усиление стабильности оружия |
| Toxin Weave | Biotechnica Black Ops | Dermal Armor | Rare | «Nanite Purge» — отражает яды |
| Modular Frame S9 | NeurON Forge | Exo-Shell | Uncommon | «Slot Cascade» — доп. слоты под импланты |

---

## Cyberware — 6 единиц

| Название | Корпорация | Тип | Редкость | Ключевая особенность |
|----------|------------|-----|----------|-----------------------|
| Arasaka Neural Mesh | Arasaka | Neural | Epic | «Hack Resonance» — ускорение взлома + защита |
| NetWatch Ægis | NetWatch + Zetatech | Cyberdeck | Artifact | «Firewall Collapse» |
| Falcon Sandevistan Mk.V | Falcon Ops (Legacy) | Speedware | Legendary | «Bullet Time Surge» |
| Reflex Driver QX | Kang-Tao | Limb | Rare | «Counter Burst» — автоответ при уклонении |
| Synapse Deck Prime | Zetatech | Neural | Legendary | «Protocol Chain» — мультивзлом целей |
| Nanite Forge Core | Biotechnica Black Ops | Dermal | Epic | «Adaptive Regen» — локальный хил |

---

## Support Gear — 4 единицы

| Название | Корпорация | Тип | Редкость | Ключевая особенность |
|----------|------------|-----|----------|-----------------------|
| Ghost Sparrow Drone | Zetatech | Recon Drone | Rare | «Holo Mapping» — подсветка врагов |
| Bulwark Auto-Turret | Militech | Deployable | Epic | «Shield Wall» — создаёт фронтальный барьер |
| Echo Beacon | Mox Syndicate | Tactical Tool | Rare | «Charm Pulse» — дезориентирует толпу |
| Pulse Medpod | Trauma Team Armory | Support Pod | Epic | «Field Surgery» — восстанавливает здоровье и humanity |

---

## Итоги и рекомендации

- 24 предмета вручную распределены по ключевым категориям и брендам.
- В каталоге отражены как новые авторские позиции, так и культовое снаряжение серии Cyberpunk.
- Следующий шаг: синхронизировать список с экономическими параметрами (стоимость, редкость, веса дропа) и подготовить шаблон для будущей автоматизации без изменения базовых документов.

