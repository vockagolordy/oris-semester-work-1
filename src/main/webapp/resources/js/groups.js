function showAddMemberForm() {
    document.getElementById('addMemberForm').classList.remove('d-none');
}

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}