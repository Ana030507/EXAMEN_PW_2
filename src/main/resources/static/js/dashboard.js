// Configuración de la API
const API_URL = 'http://localhost:8080/api';
let currentUser = null;
let editingAsignaturaId = null;
let docentesList = [];

// Verificar autenticación al cargar
document.addEventListener('DOMContentLoaded', () => {
    currentUser = JSON.parse(localStorage.getItem('user'));
    
    if (!currentUser) {
        window.location.href = 'index.html';
        return;
    }
    
    initDashboard();
    loadAsignaturas();
});

async function initDashboard() {
    // Mostrar información del usuario
    document.getElementById('userName').textContent = `${currentUser.nombre} ${currentUser.apellido}`;
    
    // Mostrar rol principal
    const roles = Array.isArray(currentUser.roles) ? currentUser.roles : [];
    const mainRole = roles.length > 0 ? roles[0].replace('ROLE_', '') : 'Usuario';
    document.getElementById('userRole').textContent = mainRole;
    
    // Mostrar secciones según el rol
    if (roles.includes('ROLE_RECTOR')) {
        document.getElementById('rectorSection').style.display = 'block';
        // Cargar lista de docentes para el selector
        await loadDocentes();
    }
    
    if (roles.includes('ROLE_DOCENTE')) {
        document.getElementById('docenteSection').style.display = 'block';
    }
    
    // Event listeners para formularios
    document.getElementById('formAsignatura').addEventListener('submit', handleSaveAsignatura);
    document.getElementById('formHorarioDocente').addEventListener('submit', handleUpdateHorario);
}

// ========== CARGAR DOCENTES ==========

async function loadDocentes() {
    try {
        const response = await fetchWithAuth(`${API_URL}/usuarios/docentes`);
        
        if (response.ok) {
            docentesList = await response.json();
            updateDocenteSelector();
        }
    } catch (error) {
        console.error('Error al cargar docentes:', error);
    }
}

function updateDocenteSelector() {
    const selector = document.getElementById('docenteId');
    selector.innerHTML = '<option value="">Sin asignar</option>';
    
    docentesList.forEach(docente => {
        const option = document.createElement('option');
        option.value = docente.id;
        option.textContent = `${docente.nombre} ${docente.apellido} (${docente.username})`;
        selector.appendChild(option);
    });
}

// ========== FUNCIONES DE ASIGNATURAS ==========

async function loadAsignaturas() {
    try {
        const response = await fetchWithAuth(`${API_URL}/asignaturas`);
        
        if (response.ok) {
            const asignaturas = await response.json();
            displayAsignaturas(asignaturas);
        } else {
            showError('Error al cargar las asignaturas');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al cargar asignaturas');
    }
}

function displayAsignaturas(asignaturas) {
    const container = document.getElementById('asignaturasContainer');
    
    if (asignaturas.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>No hay asignaturas</h3>
                <p>Aún no se han creado asignaturas en el sistema</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = asignaturas.map(asignatura => createAsignaturaCard(asignatura)).join('');
}

function createAsignaturaCard(asignatura) {
    const roles = Array.isArray(currentUser.roles) ? currentUser.roles : [];
    const isRector = roles.includes('ROLE_RECTOR');
    const isDocente = roles.includes('ROLE_DOCENTE') && !isRector;
    const isDocenteEncargado = asignatura.docenteEncargado && asignatura.docenteEncargado.id === currentUser.id;
    
    const docenteInfo = asignatura.docenteEncargado 
        ? `${asignatura.docenteEncargado.nombre} ${asignatura.docenteEncargado.apellido}`
        : 'Sin asignar';
    
    return `
        <div class="asignatura-card">
            <h4>${asignatura.nombre || 'Sin nombre'}</h4>
            <p>${asignatura.descripcion || 'Sin descripción'}</p>
            
            <div class="asignatura-info">
                <div class="info-item">
                    <span class="info-label">Salón:</span>
                    <span class="info-value">${asignatura.salon || 'No asignado'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Docente:</span>
                    <span class="info-value">${docenteInfo}</span>
                </div>
                ${asignatura.horarioInicio ? `
                <div class="info-item">
                    <span class="info-label">Horario:</span>
                    <span class="info-value">${asignatura.horarioInicio} - ${asignatura.horarioFin}</span>
                </div>
                ` : ''}
            </div>
            
            <div class="card-actions">
                ${isRector ? `
                    <button onclick="editAsignatura(${asignatura.id})" class="btn btn-secondary">
                        ✏️ Editar
                    </button>
                    <button onclick="deleteAsignatura(${asignatura.id})" class="btn btn-danger">
                        🗑️ Eliminar
                    </button>
                ` : ''}
                ${isDocente && isDocenteEncargado ? `
                    <button onclick="showUpdateHorarioForm(${asignatura.id}, '${asignatura.nombre}')" class="btn btn-primary">
                        🕒 Actualizar Horarios
                    </button>
                ` : ''}
            </div>
        </div>
    `;
}

// ========== RECTOR: CREAR/EDITAR ASIGNATURA ==========

function showCreateForm() {
    editingAsignaturaId = null;
    document.getElementById('formTitle').textContent = 'Nueva Asignatura';
    document.getElementById('formAsignatura').reset();
    document.getElementById('asignaturaId').value = '';
    updateDocenteSelector();
    document.getElementById('asignaturaForm').style.display = 'block';
    
    // Scroll al formulario
    document.getElementById('asignaturaForm').scrollIntoView({ behavior: 'smooth' });
}

async function editAsignatura(id) {
    try {
        const response = await fetchWithAuth(`${API_URL}/asignaturas`);
        
        if (response.ok) {
            const asignaturas = await response.json();
            const asignatura = asignaturas.find(a => a.id === id);
            
            if (asignatura) {
                editingAsignaturaId = id;
                document.getElementById('formTitle').textContent = 'Editar Asignatura';
                document.getElementById('asignaturaId').value = asignatura.id;
                document.getElementById('nombre').value = asignatura.nombre || '';
                document.getElementById('salon').value = asignatura.salon || '';
                document.getElementById('descripcion').value = asignatura.descripcion || '';
                document.getElementById('horarioInicio').value = asignatura.horarioInicio || '';
                document.getElementById('horarioFin').value = asignatura.horarioFin || '';
                
                // Seleccionar el docente encargado
                updateDocenteSelector();
                document.getElementById('docenteId').value = asignatura.docenteEncargado ? asignatura.docenteEncargado.id : '';
                
                document.getElementById('asignaturaForm').style.display = 'block';
                document.getElementById('asignaturaForm').scrollIntoView({ behavior: 'smooth' });
            }
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error al cargar la asignatura');
    }
}

async function handleSaveAsignatura(e) {
    e.preventDefault();
    
    const id = document.getElementById('asignaturaId').value;
    const nombre = document.getElementById('nombre').value.trim();
    const salon = parseInt(document.getElementById('salon').value);
    const descripcion = document.getElementById('descripcion').value.trim();
    const horarioInicio = document.getElementById('horarioInicio').value;
    const horarioFin = document.getElementById('horarioFin').value;
    const docenteId = document.getElementById('docenteId').value;
    
    const asignatura = {
        nombre,
        salon,
        descripcion,
        horarioInicio: horarioInicio || null,
        horarioFin: horarioFin || null
    };
    
    try {
        let response;
        let asignaturaId = id;
        
        if (id) {
            // Editar
            response = await fetchWithAuth(`${API_URL}/asignaturas/${id}`, {
                method: 'PUT',
                body: JSON.stringify(asignatura)
            });
        } else {
            // Crear
            response = await fetchWithAuth(`${API_URL}/asignaturas`, {
                method: 'POST',
                body: JSON.stringify(asignatura)
            });
            
            if (response.ok) {
                const nuevaAsignatura = await response.json();
                asignaturaId = nuevaAsignatura.id;
            }
        }
        
        if (response.ok) {
            // Asignar docente si se seleccionó uno
            if (asignaturaId) {
                await asignarDocenteAAsignatura(asignaturaId, docenteId);
            }
            
            showSuccess(id ? 'Asignatura actualizada exitosamente' : 'Asignatura creada exitosamente');
            cancelForm();
            loadAsignaturas();
        } else {
            const error = await response.text();
            showError('Error al guardar la asignatura: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al guardar la asignatura');
    }
}

async function asignarDocenteAAsignatura(asignaturaId, docenteId) {
    try {
        const url = docenteId 
            ? `${API_URL}/asignaturas/${asignaturaId}/docente?docenteId=${docenteId}`
            : `${API_URL}/asignaturas/${asignaturaId}/docente`;
        
        await fetchWithAuth(url, {
            method: 'PATCH'
        });
    } catch (error) {
        console.error('Error al asignar docente:', error);
    }
}

function cancelForm() {
    document.getElementById('asignaturaForm').style.display = 'none';
    document.getElementById('formAsignatura').reset();
    editingAsignaturaId = null;
}

async function deleteAsignatura(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta asignatura?')) {
        return;
    }
    
    try {
        const response = await fetchWithAuth(`${API_URL}/asignaturas/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showSuccess('Asignatura eliminada exitosamente');
            loadAsignaturas();
        } else {
            showError('Error al eliminar la asignatura');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al eliminar la asignatura');
    }
}

// ========== DOCENTE: ACTUALIZAR HORARIOS ==========

function showUpdateHorarioForm(id, nombre) {
    document.getElementById('horarioAsignaturaId').value = id;
    document.getElementById('horarioAsignaturaNombre').value = nombre;
    document.getElementById('horarioDocenteInicio').value = '';
    document.getElementById('horarioDocenteFin').value = '';
    document.getElementById('docenteHorarioForm').style.display = 'block';
    
    document.getElementById('docenteHorarioForm').scrollIntoView({ behavior: 'smooth' });
}

async function handleUpdateHorario(e) {
    e.preventDefault();
    
    const id = document.getElementById('horarioAsignaturaId').value;
    const inicio = document.getElementById('horarioDocenteInicio').value;
    const fin = document.getElementById('horarioDocenteFin').value;
    
    if (!inicio || !fin) {
        showError('Por favor completa ambos horarios');
        return;
    }
    
    try {
        const response = await fetchWithAuth(`${API_URL}/asignaturas/${id}/horarios`, {
            method: 'PATCH',
            body: JSON.stringify({
                inicio,
                fin
            })
        });
        
        if (response.ok) {
            showSuccess('Horarios actualizados exitosamente');
            cancelDocenteForm();
            loadAsignaturas();
        } else {
            const error = await response.text();
            showError('Error al actualizar horarios: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al actualizar horarios');
    }
}

function cancelDocenteForm() {
    document.getElementById('docenteHorarioForm').style.display = 'none';
    document.getElementById('formHorarioDocente').reset();
}

// ========== UTILIDADES ==========

async function fetchWithAuth(url, options = {}) {
    const credentials = localStorage.getItem('credentials');
    
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Basic ${credentials}`
        }
    };
    
    return fetch(url, { ...defaultOptions, ...options });
}

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
    
    setTimeout(() => {
        successDiv.classList.remove('show');
    }, 5000);
}

function logout() {
    if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
        localStorage.removeItem('user');
        localStorage.removeItem('credentials');
        window.location.href = 'index.html';
    }
}
