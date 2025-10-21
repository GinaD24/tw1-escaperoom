document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registroForm');
    const btn = document.getElementById('btn-registrarme');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const fechaInput = document.getElementById('fechaNacimiento');

    function checkForm() {
        const requiredFields = form.querySelectorAll('input[required]');

        const allRequiredFilled = Array.from(requiredFields).every(field => {
            if (field.value.trim() === '') return false;

            if (field === fechaInput) {
                const selectedDate = new Date(field.value);
                const today = new Date();
                today.setHours(0, 0, 0, 0);

                if (selectedDate > today) {
                    field.classList.add('border-red-400', 'ring-2', 'ring-red-400/50');
                    return false;
                } else {
                    field.classList.remove('border-red-400', 'ring-2', 'ring-red-400/50');
                }
            }
            return true;
        });

        const isPasswordLongEnough = passwordInput.value.length >= 8;

        btn.disabled = !(allRequiredFilled && isPasswordLongEnough);

        if (btn.disabled) {
            btn.classList.add('opacity-50', 'cursor-not-allowed');
        } else {
            btn.classList.remove('opacity-50', 'cursor-not-allowed');
        }
    }

    form.addEventListener('input', checkForm);
    checkForm();
});
