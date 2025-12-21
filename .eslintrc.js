module.exports = {
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
  "plugins": [
    "no-unicode"
  ],
  "rules": {
    // Запрет всех эмодзи и специальных Unicode символов
    "no-unicode/no-unicode": [
      "error",
      {
        "forbidden": [
          // Эмодзи диапазоны (основные блоки)
          "\\u{1F300}-\\u{1F5FF}", // Misc Symbols and Pictographs
          "\\u{1F600}-\\u{1F64F}", // Emoticons
          "\\u{1F680}-\\u{1F6FF}", // Transport and Map
          "\\u{1F900}-\\u{1F9FF}", // Supplemental Symbols and Pictographs
          "\\u{2600}-\\u{26FF}",   // Misc symbols
          "\\u{2700}-\\u{27BF}",   // Dingbats
          "\\u{1f926}-\\u{1f937}", // Gestures
          "\\u{1f1e0}-\\u{1f1ff}", // Flags

          // Специальные символы
          "\\u{00A9}", // © copyright
          "\\u{00AE}", // ® registered
          "\\u{2122}", // ™ trademark
          "\\u{00B0}", // ° degree
          "\\u{00A7}", // § section
          "\\u{00B6}", // ¶ pilcrow

          // Математические символы
          "\\u{221E}", // ∞ infinity
          "\\u{2260}", // ≠ not equal
          "\\u{2264}", // ≤ less than or equal
          "\\u{2265}", // ≥ greater than or equal
          "\\u{2248}", // ≈ approximately equal
          "\\u{2211}", // ∑ summation
          "\\u{220F}", // ∏ product
          "\\u{2206}", // ∆ delta

          // Стрелки и символы
          "\\u{2190}", // ← left arrow
          "\\u{2192}", // → right arrow
          "\\u{2191}", // ↑ up arrow
          "\\u{2193}", // ↓ down arrow
          "\\u{21D2}", // ⇒ right double arrow
          "\\u{21D0}", // ⇐ left double arrow

          // Карточные масти
          "\\u{2660}", // ♠ spade
          "\\u{2661}", // ♡ heart
          "\\u{2662}", // ♢ diamond
          "\\u{2663}", // ♣ club
          "\\u{2664}", // ♤ spade (white)
          "\\u{2665}", // ♥ heart (black)
          "\\u{2666}", // ♦ diamond (black)
          "\\u{2667}", // ♧ club (white)

          // Геометрические фигуры
          "\\u{25A0}", "\\u{25A1}", "\\u{25AA}", "\\u{25AB}", // Squares
          "\\u{25B2}", "\\u{25B3}", "\\u{25B4}", "\\u{25B5}", // Triangles
          "\\u{25C6}", "\\u{25C7}", "\\u{25C8}", "\\u{25C9}", // Diamonds
          "\\u{25CB}", "\\u{25CC}", "\\u{25CD}", "\\u{25CE}", "\\u{25CF}", // Circles

          // Блочные символы
          "\\u{2580}-\\u{259F}", // Block elements

          // Коробочные символы
          "\\u{2500}-\\u{257F}", // Box drawing

          // Braille
          "\\u{2800}-\\u{28FF}"
        ],
        "message": "Forbidden Unicode character {{character}} ({{code}}) found. Use only ASCII characters (0-127)."
      }
    ]
  },
  "overrides": [
    {
      // Отключаем правило для бинарных файлов и изображений
      "files": [
        "*.png", "*.jpg", "*.jpeg", "*.gif", "*.svg", "*.ico",
        "*.woff", "*.woff2", "*.ttf", "*.eot",
        "*.pdf", "*.doc", "*.docx", "*.xls", "*.xlsx"
      ],
      "rules": {
        "no-unicode/no-unicode": "off"
      }
    }
  ]
};
