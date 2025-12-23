module.exports = {
    root: true,
    "env": {
        "browser": true,
        "es2021": true,
        "node": true
    },
    "extends": [
        "eslint:recommended"
    ],
    "parserOptions": {
        "ecmaVersion": "latest",
        "sourceType": "module"
    },
    "rules": {
        // Запрет нерегулярных пробелов и Unicode символов
        "no-irregular-whitespace": "error",

        // Предупреждение: только эмодзи запрещены, кириллица разрешена
        "no-restricted-syntax": [
            "warn",
            {
                "selector": "Program",
                "message": "EMOJI FORBIDDEN: No emoji or special Unicode symbols. Cyrillic text (Russian) is ALLOWED."
            }
        ]
    },
    "overrides": [
        {
            // Отключаем правила для бинарных файлов и изображений
            "files": [
                "*.png", "*.jpg", "*.jpeg", "*.gif", "*.svg", "*.ico",
                "*.woff", "*.woff2", "*.ttf", "*.eot",
                "*.pdf", "*.doc", "*.docx", "*.xls", "*.xlsx"
            ],
            "rules": {
                "no-irregular-whitespace": "off",
                "no-restricted-syntax": "off"
            }
        }
    ]
};
