# Git LFS Setup для Unreal Engine 5

## Проблема

Бинарные файлы Unreal Engine (`.uasset`, `.umap`, текстуры, аудио) не должны храниться в обычном Git, так как:
- Они очень большие (текстуры могут быть 100+ MB)
- Невозможно делать diff/merge
- Раздувают размер репозитория
- Замедляют все операции (clone, pull, push)

## Решение: Git LFS

Git LFS (Large File Storage) хранит бинарные файлы отдельно, а в Git хранит только указатели.

### 1. Установка Git LFS

#### Windows
```powershell
# Через winget
winget install GitHub.GitLFS

# Или через Chocolatey
choco install git-lfs

# Или скачать с https://git-lfs.github.com/
```

#### Linux/Mac
```bash
# Ubuntu/Debian
sudo apt-get install git-lfs

# macOS
brew install git-lfs

# Arch Linux
sudo pacman -S git-lfs
```

### 2. Инициализация Git LFS

```bash
# Один раз в системе
git lfs install

# Проверка
git lfs version
```

### 3. Миграция существующих файлов

WARNING **ВАЖНО**: Если `.uasset` файлы уже закоммичены в Git, их нужно мигрировать:

```bash
# Перейти в корень проекта
cd c:\NECPGAME

# Мигрировать все коммиты (это переписывает историю!)
git lfs migrate import --include="*.uasset,*.umap,*.upk,*.uexp,*.ubulk,*.ufont" --everything

# Если хотите мигрировать только определенные ветки
git lfs migrate import --include="*.uasset,*.umap" --include-ref=refs/heads/main --include-ref=refs/heads/develop
```

WARNING **Внимание**: Это переписывает историю Git! Все разработчики должны сделать `git clone` заново.

### 4. Настройка завершена

`.gitattributes` уже настроен для работы с LFS:
- Все `.uasset`, `.umap`, `.upk`, `.uexp`, `.ubulk`, `.ufont` файлы
- Текстуры (`.png`, `.jpg`, `.tga`, `.dds`, `.exr`, `.hdr`)
- Аудио (`.wav`, `.mp3`, `.ogg`, `.flac`)
- Видео (`.mp4`, `.avi`, `.mov`, `.webm`)
- 3D модели (`.fbx`, `.obj`, `.blend`)

### 5. Работа с LFS

После настройки работа идет как обычно:

```bash
# Добавить файлы
git add client/UE5/NECPGAME/Content/*.uasset

# Коммит
git commit -m "Add UE5 assets"

# Push (LFS автоматически загружает большие файлы)
git push
```

### 6. Проверка LFS

```bash
# Посмотреть, какие файлы отслеживаются LFS
git lfs ls-files

# Посмотреть статус LFS
git lfs status

# Посмотреть информацию о LFS в репозитории
git lfs env
```

### 7. Клонирование репозитория с LFS

Другим разработчикам нужно установить Git LFS перед клонированием:

```bash
# Установить Git LFS
git lfs install

# Клонировать репозиторий (LFS файлы скачаются автоматически)
git clone https://github.com/necpgame/NECPGAME.git
```

## Альтернативы

Если Git LFS не подходит (например, дорого или ограничения GitHub):

1. **Perforce (P4)** - индустриальный стандарт для игровых студий
2. **Plastic SCM** - хорошая альтернатива с поддержкой больших бинарных файлов
3. **Отдельное хранилище** - хранить ассеты отдельно (S3, NAS) и не коммитить их

## GitHub LFS Limits

Бесплатный тариф GitHub:
- 1 GB хранилища LFS
- 1 GB трафика/месяц

Платные тарифы:
- GitHub Pro: $5/месяц за 50 GB хранилища + 50 GB трафика
- Дополнительно: $5/месяц за каждые 50 GB

**Рекомендация**: Для игрового проекта с UE5 лучше использовать:
- GitHub Enterprise (если бюджет позволяет)
- Или Perforce/Plastic SCM для ассетов
- Или отдельное хранилище (S3, Azure Blob) + DVC (Data Version Control)

## Дополнительные ресурсы

- [Git LFS официальная документация](https://git-lfs.github.com/)
- [GitHub LFS документация](https://docs.github.com/en/repositories/working-with-files/managing-large-files)
- [Unreal Engine + Git LFS best practices](https://www.unrealengine.com/en-US/blog/using-git-source-control-with-unreal-engine)

