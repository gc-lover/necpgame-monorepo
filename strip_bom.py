import os

def remove_bom(filepath):
    try:
        with open(filepath, 'rb') as f:
            content = f.read()
        
        if content.startswith(b'\xef\xbb\xbf'):
            print(f"Removing BOM from: {filepath}")
            with open(filepath, 'wb') as f:
                f.write(content[3:])
            return True
        return False
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
        return False

def main():
    root_dir = "services"
    count = 0
    for dirpath, dirnames, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename == "go.mod":
                filepath = os.path.join(dirpath, filename)
                if remove_bom(filepath):
                    count += 1
    print(f"Removed BOM from {count} files.")

if __name__ == "__main__":
    main()
