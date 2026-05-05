const fs = require('fs');

let content = fs.readFileSync('e:/SmartGo/presentation/2-presentation.html', 'utf8');

let slideCount = 0;
// First pass: count slides
content.replace(/<div class="slide"/g, () => {
    slideCount++;
    return '';
});

let currentSlide = 0;

// Second pass: replace all id="sX"
content = content.replace(/id="s\d+"/g, () => {
    currentSlide++;
    return 'id="s' + currentSlide + '"';
});

currentSlide = 0;
// Third pass: replace all SLIDE X comments
content = content.replace(/SLIDE \d+ /g, () => {
    currentSlide++;
    return 'SLIDE ' + currentSlide + ' ';
});

currentSlide = 0;
// Fourth pass: replace all <div class="sn">XX / YY</div>
content = content.replace(/<div class="sn">\d+\s*\/\s*\d+<\/div>/g, () => {
    currentSlide++;
    let numStr = currentSlide < 10 ? '0' + currentSlide : currentSlide.toString();
    return '<div class="sn">' + numStr + ' / ' + slideCount + '</div>';
});

fs.writeFileSync('e:/SmartGo/presentation/2-presentation.html', content, 'utf8');
console.log('Renumbered successfully. Total slides: ' + slideCount);
