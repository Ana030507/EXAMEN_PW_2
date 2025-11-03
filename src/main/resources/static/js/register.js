// Configuración de la API
const API_URL = 'http://localhost:8080/api';

// Verificar si ya hay sesión
document.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        window.location.href = 'dashboard.html';
    }
});

// Manejo del formulario de registro
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const nombre = document.getElementById('nombre').value.trim();
    const apellido = document.getElementById('apellido').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const rol = document.getElementById('rol').value;
    
    // Validaciones
    if (!username || !nombre || !apellido || !password || !confirmPassword) {
        showError('Por favor completa todos los campos obligatorios');
        return;
    }
    
    if (password !== confirmPassword) {
        showError('Las contraseñas no coinciden');
        return;
    }
    
    if (password.length < 4) {
        showError('La contraseña debe tener al menos 4 caracteres');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username,
                nombre,
                apellido,
                password,
                rol
            })
        });
        
        if (response.ok) {
            const user = await response.json();
            showSuccess('¡Registro exitoso! Redirigiendo al login...');
            
            // Limpiar formulario
            document.getElementById('registerForm').reset();
            
            // Redirigir al login después de 2 segundos
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 2000);
        } else {
            const error = await response.text();
            if (response.status === 400) {
                showError('El nombre de usuario ya está en uso');
            } else {
                showError('Error en el registro: ' + error);
            }
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión. Verifica que el servidor esté ejecutándose.');
    }
});

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    const successDiv = document.getElementById('successMessage');
    
    successDiv.classList.remove('show');
    errorDiv.textContent = message;
    errorDiv.classList.add('show');
    
    setTimeout(() => {
        errorDiv.classList.remove('show');
    }, 5000);
}

function showSuccess(message) {
    const errorDiv = document.getElementById('errorMessage');
    const successDiv = document.getElementById('successMessage');
    
    errorDiv.classList.remove('show');
    successDiv.textContent = message;
    successDiv.classList.add('show');
}
