# Frontend - Sistema de Gestión de Asignaturas

## 📋 Descripción

Frontend web desarrollado en **HTML5**, **CSS3** y **JavaScript vanilla** para el Sistema de Gestión de Asignaturas. Se integra con el backend Spring Boot mediante API REST con autenticación básica.

## 🎨 Características

- **Diseño moderno y responsive** con gradientes y animaciones
- **Autenticación de usuarios** (Login y Registro)
- **Gestión por roles**:
  - **RECTOR**: Crear, editar y eliminar asignaturas
  - **DOCENTE**: Actualizar horarios de asignaturas
  - **ESTUDIANTE**: Visualizar asignaturas
- **Interfaz intuitiva** con validaciones en tiempo real
- **Almacenamiento local** de sesión (localStorage)

## 📁 Estructura de Archivos

```
src/main/resources/static/
├── index.html           # Página de inicio de sesión
├── register.html        # Página de registro
├── dashboard.html       # Dashboard principal
├── css/
│   └── styles.css      # Estilos globales
├── js/
│   ├── auth.js         # Lógica de autenticación (login)
│   ├── register.js     # Lógica de registro
│   └── dashboard.js    # Lógica del dashboard y gestión de asignaturas
└── README_FRONTEND.md  # Esta documentación
```

## 🚀 Cómo usar

### 1. Iniciar el servidor backend

Asegúrate de que el servidor Spring Boot esté ejecutándose en `http://localhost:8080`

### 2. Acceder al frontend

Abre tu navegador y ve a:
```
http://localhost:8080/index.html
```

O simplemente:
```
http://localhost:8080/
```

### 3. Registro de usuarios

1. Haz clic en "Regístrate aquí" en la página de login
2. Completa el formulario con:
   - Usuario (único)
   - Nombre y Apellido
   - Contraseña (mínimo 4 caracteres)
   - Rol: Estudiante, Docente o Rector
3. Haz clic en "Registrarse"
4. Serás redirigido al login automáticamente

### 4. Iniciar sesión

1. Ingresa tu usuario y contraseña
2. Haz clic en "Iniciar Sesión"
3. Serás redirigido al dashboard según tu rol

### 5. Usar el dashboard

#### Como RECTOR:
- **Crear asignatura**: Haz clic en "+ Nueva Asignatura"
  - Completa el formulario con nombre, salón, descripción y horarios
  - **Asigna un docente** desde el selector (puedes dejarlo sin asignar)
- **Editar asignatura**: Haz clic en "✏️ Editar" en una tarjeta
  - Modifica cualquier campo incluyendo el **docente asignado**
- **Eliminar asignatura**: Haz clic en "🗑️ Eliminar" y confirma

#### Como DOCENTE:
- **Ver todas las asignaturas**: Puedes ver todas las asignaturas del sistema
- **Actualizar horarios**: El botón "🕒 Actualizar Horarios" solo aparece en las asignaturas donde tú eres el docente encargado
  - Solo puedes modificar horarios de **las asignaturas que te fueron asignadas**
  - El backend valida que seas el docente asignado antes de permitir cambios

#### Como ESTUDIANTE:
- **Visualizar asignaturas**: Puedes ver todas las asignaturas del sistema
- **Ver información del docente**: Cada tarjeta muestra qué docente está asignado

## 🔧 Configuración

### Cambiar la URL de la API

Si tu backend está en un puerto diferente, edita los archivos JavaScript:

**auth.js**, **register.js** y **dashboard.js**:
```javascript
const API_URL = 'http://localhost:8080/api';
```

Cámbialo por tu URL, por ejemplo:
```javascript
const API_URL = 'http://localhost:9090/api';
```

## 🎨 Personalización de Estilos

Los colores y estilos están definidos con variables CSS en `styles.css`:

```css
:root {
    --primary-color: #4f46e5;     /* Color principal */
    --success-color: #10b981;     /* Color de éxito */
    --danger-color: #ef4444;      /* Color de peligro */
    /* ... más variables ... */
}
```

Modifica estas variables para cambiar el esquema de colores de toda la aplicación.

## 📱 Responsive Design

El frontend es completamente responsive y se adapta a diferentes tamaños de pantalla:
- **Desktop**: Vista completa con grids de tarjetas
- **Tablet**: Grids adaptados
- **Mobile**: Vista de columna única

## 🔐 Seguridad

- Las contraseñas se envían al backend donde son encriptadas con BCrypt
- Las credenciales se almacenan en localStorage codificadas en Base64
- Todas las peticiones a endpoints protegidos usan autenticación HTTP Basic
- El frontend valida la sesión en cada carga de página

## 🐛 Solución de Problemas

### No puedo acceder al frontend
- Verifica que el servidor Spring Boot esté ejecutándose
- Verifica que la configuración de seguridad permita acceso a recursos estáticos

### Error de CORS
- El frontend está servido desde el mismo dominio que el backend, no debería haber problemas de CORS
- Si usas un servidor separado, configura CORS en Spring Boot

### Las peticiones fallan
- Abre la consola del navegador (F12) para ver errores
- Verifica que la URL de la API sea correcta
- Verifica que el usuario tenga permisos para la operación

## 🎯 Funcionalidades Implementadas

✅ Login con validación
✅ Registro con selección de rol
✅ Dashboard según rol
✅ CRUD completo de asignaturas (Rector)
✅ **Asignación de docentes a asignaturas (Rector)**
✅ **Visualización de todas las asignaturas (Todos los roles)**
✅ Actualización de horarios solo en asignaturas asignadas (Docente)
✅ Botón de edición solo visible en asignaturas propias del docente
✅ Información del docente visible en cada asignatura
✅ Mensajes de error y éxito
✅ Validaciones en formularios y backend
✅ Confirmación de eliminación
✅ Cierre de sesión

## 📞 Soporte

Para reportar problemas o sugerencias, contacta al equipo de desarrollo.
