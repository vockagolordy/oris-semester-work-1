function updateGoalProgress(goalId, current, target) {
    const percentage = (current / target) * 100;
    const progressBar = document.getElementById('progress-' + goalId);
    if (progressBar) {
        progressBar.style.width = percentage + '%';
        progressBar.textContent = Math.round(percentage) + '%';
    }
}