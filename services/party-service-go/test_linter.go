package main

import (
	"fmt"  // This import is unused - should trigger linter error
	"os"   // This import is unused - should trigger linter error
)

func main() {
	// Empty function - unused imports should be detected
	var unusedVar int = 42  // Unused variable
	_ = unusedVar  // This makes it used, but fmt and os are still unused
}





