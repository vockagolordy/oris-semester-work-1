function confirmAction(message) {
    return confirm(message || 'Вы уверены?');
}

function animateProgressBars() {
    const progressBars = document.querySelectorAll('.goal-progress');
    progressBars.forEach(bar => {
        const targetWidth = bar.getAttribute('data-progress');
        bar.style.width = '0%';
        setTimeout(() => {
            bar.style.width = targetWidth + '%';
        }, 100);
    });
}

document.addEventListener('DOMContentLoaded', animateProgressBars);