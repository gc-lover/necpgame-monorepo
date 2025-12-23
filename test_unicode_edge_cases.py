# Test edge cases that might be confused with Cyrillic
print("Regular Cyrillic: Привет")
print("Mathematical: ∀∂∈ℝ∪")  # Mathematical operators
print("Latin lookalikes: аɑ")  # Cyrillic 'а' vs Latin 'ɑ'
print("Combining chars: с\u0301")  # Cyrillic 'с' with combining acute
print("Fullwidth: ａｂｃ")  # Fullwidth Latin
print("Superscript: ᵃᵇᶜ")  # Superscript Latin
print("Subscript: ₐ ᵦ ᶜ")  # Subscript Latin
