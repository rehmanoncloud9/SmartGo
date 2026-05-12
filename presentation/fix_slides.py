import re
import sys

file_path = r'e:\SmartGo\smartGo\presentation\SmartGo_Final_Presentation.html'

try:
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
except Exception as e:
    print(f"Error reading file: {e}")
    sys.exit(1)

parts = content.split('<div class="slide-wrap">')

if len(parts) < 32:
    print(f"Unexpected number of parts: {len(parts)}. Expected at least 32.")
    sys.exit(1)

header = parts[0]
slides = parts[1:]

new_slides = []
for i, slide in enumerate(slides):
    if (i + 1) not in [28, 30]:
        new_slides.append(slide)

total_slides = len(new_slides)
for i in range(len(new_slides)):
    new_num = f"{i+1:02d} / {total_slides}"
    new_slides[i] = re.sub(r'<div class="sn">\d+\s*/\s*\d+</div>', f'<div class="sn">{new_num}</div>', new_slides[i])

new_content = header + '<div class="slide-wrap">'.join([''] + new_slides)

try:
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    print(f"Successfully removed slides 28 and 30. Total slides now: {total_slides}")
except Exception as e:
    print(f"Error writing file: {e}")
    sys.exit(1)
