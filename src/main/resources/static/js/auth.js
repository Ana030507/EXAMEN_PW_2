// Configuración de la API
const API_URL = 'http://localhost:8080/api';

// Verificar si ya hay sesión
document.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        window.location.href = 'dashboard.html';
    }
});

// Manejo del formulario de login
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    
    if (!username || !password) {
        showError('Por favor completa todos los campos');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            const user = await response.json();
            
            // Guardar usuario en localStorage
            localStorage.setItem('user', JSON.stringify(user));
            
            // Guardar credenciales para autenticación básica
            const credentials = btoa(`${username}:${password}`);
            localStorage.setItem('credentials', credentials);
            
            // Redirigir al dashboard
            window.location.href = 'dashboard.html';
        } else {
            const error = await response.text();
            showError('Usuario o contraseña incorrectos');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión. Verifica que el servidor esté ejecutándose.');
    }
});

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.textContent = message;
    errorDiv.classList.add('show');
    
    setTimeout(() => {
        errorDiv.classList.remove('show');
    }, 5000);
}
